package io.mosip.injivcrenderer.utils

import com.fasterxml.jackson.databind.JsonNode
import io.mosip.injivcrenderer.constants.Constants.DIGEST_MULTIBASE
import io.mosip.injivcrenderer.constants.Constants.ID
import io.mosip.injivcrenderer.constants.Constants.RENDER_METHOD
import io.mosip.injivcrenderer.constants.Constants.RENDER_SUITE
import io.mosip.injivcrenderer.constants.Constants.SHA_256
import io.mosip.injivcrenderer.constants.Constants.SVG_MUSTACHE
import io.mosip.injivcrenderer.constants.Constants.TEMPLATE
import io.mosip.injivcrenderer.constants.Constants.TEMPLATE_RENDER_METHOD
import io.mosip.injivcrenderer.constants.Constants.TYPE
import io.mosip.injivcrenderer.exceptions.VcRendererExceptions
import io.mosip.injivcrenderer.networkManager.NetworkManager
import io.mosip.injivcrenderer.networkManager.TemplateResponse
import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class TemplateHelper(private val traceabilityId: String) {

    private val className = TemplateHelper::class.simpleName

    fun extractSVG(renderMethod: JsonNode): List<String> {
        validateSvgMustacheRenderSuite(renderMethod)
        validateTemplateRenderMethodType(renderMethod)

        val templateValue = renderMethod.path(TEMPLATE)

        val templateId = templateValue.path(ID).asText(null)
            ?: throw VcRendererExceptions.MissingTemplateIdException(traceabilityId, className)
        val digestMultibase = templateValue.path(DIGEST_MULTIBASE).asText(null)

        val templateResponse = NetworkManager(traceabilityId).fetch(templateId)

        if (templateResponse.body.isEmpty()) {
            throw VcRendererExceptions.SvgFetchException(
                traceabilityId,
                this::class.simpleName,
                "Empty response body"
            )
        }

        if (digestMultibase != null && !validateDigestMultibase(templateResponse.body, digestMultibase)) {
            throw VcRendererExceptions.MultibaseValidationException(
                traceabilityId = traceabilityId,
                className = className,
                exceptionMessage = "Mismatch between fetched SVG and provided digestMultibase"
            )
        }
        return extractSVGList(templateResponse)

    }

    /*** If contentType is application/xml, extract SVGs from the pageSet, else return the body as a single-item list ***/
    private fun extractSVGList(templateResponse: TemplateResponse): List<String> =
        if (templateResponse.isXmlTemplate()) {
            XMLHelper(traceabilityId).getSVGListFromPageSet(templateResponse.body)
        } else {
            listOf(templateResponse.body)
        }

    fun validateDigestMultibase(svgString: String, digestMultibase: String): Boolean {
        if (!digestMultibase.startsWith("u")) throw VcRendererExceptions.MultibaseValidationException(traceabilityId, className, "digestMultibase must start with 'u'")
        val encodedPart = digestMultibase.substring(1)

        val decoded = base64UrlNoPadDecode(encodedPart)
        if (decoded.size != 34)
            throw VcRendererExceptions.MultibaseValidationException(traceabilityId, className, "Invalid multihash length")
        if (decoded[0] != 0x12.toByte() || decoded[1] != 0x20.toByte())
            throw VcRendererExceptions.MultibaseValidationException(traceabilityId, className, "Unsupported multihash prefix")

        val expectedHash = decoded.copyOfRange(2, 34)
        val actualHash = MessageDigest.getInstance(SHA_256).digest(svgString.toByteArray(Charsets.UTF_8))

        return actualHash.contentEquals(expectedHash)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun base64UrlNoPadDecode(input: String): ByteArray {
        return Base64.UrlSafe.decode(input)
    }

    private fun isSvgMustacheRenderSuite(renderMethod: JsonNode): Boolean {
        val renderSuite = renderMethod.path(RENDER_SUITE).asText("")
        return renderSuite == SVG_MUSTACHE
    }

    private fun validateSvgMustacheRenderSuite(renderMethod: JsonNode) {
        if (!isSvgMustacheRenderSuite(renderMethod)) {
            throw VcRendererExceptions.InvalidRenderSuiteException(
                traceabilityId = traceabilityId,
                className = this::class.simpleName
            )
        }
    }

    private fun isTemplateRenderMethodType(renderMethod: JsonNode): Boolean {
        val type = renderMethod.path(TYPE).asText("")
        return type == TEMPLATE_RENDER_METHOD
    }

    private fun validateTemplateRenderMethodType(renderMethod: JsonNode) {
        if (!isTemplateRenderMethodType(renderMethod)) {
            throw VcRendererExceptions.InvalidRenderMethodTypeException(
                traceabilityId = traceabilityId,
                className = this::class.simpleName
            )
        }
    }

    fun parseRenderMethod(jsonObject: JsonNode): List<JsonNode> {
        val renderMethodValue = jsonObject.path(RENDER_METHOD)

        return when {
            renderMethodValue.isArray -> {
                val elements = renderMethodValue.toList()
                if (elements.isEmpty() || elements.any { !it.isObject || it.size() == 0 }) {
                    throw VcRendererExceptions.InvalidRenderMethodException(traceabilityId, className)
                }
                elements
            }

            renderMethodValue.isObject -> {
                if (renderMethodValue.size() == 0) {
                    throw VcRendererExceptions.InvalidRenderMethodException(traceabilityId, className)
                }
                listOf(renderMethodValue)
            }

            else -> throw VcRendererExceptions.InvalidRenderMethodException(traceabilityId, className)
        }
    }
}