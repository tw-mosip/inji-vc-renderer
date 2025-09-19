package io.mosip.injivcrenderer

import io.mosip.injivcrenderer.constants.Constants.RENDER_PROPERTY
import io.mosip.injivcrenderer.constants.Constants.TEMPLATE
import io.mosip.injivcrenderer.templateEngine.svg.JsonPointerResolver
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.mosip.injivcrenderer.constants.CredentialFormat
import io.mosip.injivcrenderer.exceptions.VcRendererExceptions
import io.mosip.injivcrenderer.pdf.PdfNetworkManager
import io.mosip.injivcrenderer.pdf.extractPdfFields
import io.mosip.injivcrenderer.pdf.fillPdfToBase64
import io.mosip.injivcrenderer.utils.Utils
import kotlin.io.encoding.ExperimentalEncodingApi

class InjiVcRenderer(private val traceabilityId: String) {

    private val mapper = ObjectMapper()

    /**
     * Renders SVG templates defined in the VC's renderMethod section.
     * Supports fetching templates from URLs and data URIs.
     * Replaces placeholders in the templates with values from the VC JSON.
     *
     * @param credentialFormat The format of the credential. Currently only LDP_VC is supported.
     * @param wellKnownJson Optional well-known JSON for additional placeholders for labels.
     * @param vcJsonString The Verifiable Credential as a JSON string.
     * @return A list of rendered SVG strings. Empty list if no valid render methods found or on error. Return is List<Any> to accommodate future extensions.
     */
    @JvmOverloads
    fun renderVC(credentialFormat: CredentialFormat, wellKnownJson: String? = null, vcJsonString: String): List<Any> {
        return try {

            if (credentialFormat != CredentialFormat.LDP_VC) {
                throw VcRendererExceptions.UnsupportedCredentialFormat(
                    traceabilityId = traceabilityId,
                    className = this::class.simpleName
                )
            }
            var wellKnownJsonNode: JsonNode = mapper.createObjectNode()
            val vcJsonNode: JsonNode = mapper.readTree(vcJsonString)
            val renderMethodArray = Utils(traceabilityId).parseRenderMethod(vcJsonNode, traceabilityId)

            val results = mutableListOf<String>()
            for (element in renderMethodArray) {

                var svgTemplate = Utils(traceabilityId).extractSvgTemplate(element, vcJsonString)

                // Replace label placeholders first using well-known JSON
                if(!wellKnownJson.isNullOrEmpty()) {
                    wellKnownJsonNode = mapper.readTree(wellKnownJson)

                }
                svgTemplate = JsonPointerResolver(traceabilityId).replacePlaceholders(
                    svgTemplate = svgTemplate,
                    jsonNode = wellKnownJsonNode,
                    isLabelPlaceholder = true
                )

                // Replace value placeholders using VC JSON
                val renderProperties =
                    element.path(TEMPLATE).path(RENDER_PROPERTY).takeIf { it.isArray }?.map { it.asText() }

                val renderedSvg = JsonPointerResolver(traceabilityId).replacePlaceholders(
                    svgTemplate = svgTemplate,
                    jsonNode = vcJsonNode,
                    renderProperties = renderProperties)
                results.add(renderedSvg)

            }
            results
        } catch (vcRendererException : VcRendererExceptions) {
            throw vcRendererException
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun renderVCForPdf(vcJsonString: String): String {
        return try {
            var pdfBytes = PdfNetworkManager(traceabilityId).fetchPdfAsBytes("https://aad51d8abe3a.ngrok-free.app/templates/filler-new.pdf")
            println(pdfBytes)
            val values = mapOf(
                "Text1" to "Mary Smith",
                "Text2" to "Male",
                "Text3" to "1990-01-01",
                "Text4" to "Gomti Nagar",
                "Text5" to "Lucknow",
                "Text6" to "Uttar Pradesh"
            )

            val fieldNames = extractPdfFields(pdfBytes)
            println(fieldNames)

            // Returns Base64 string of filled PDF
            val result = fillPdfToBase64(pdfBytes, values)
            return result
        } catch (vcRendererException : VcRendererExceptions) {
            throw vcRendererException
        }
    }



}