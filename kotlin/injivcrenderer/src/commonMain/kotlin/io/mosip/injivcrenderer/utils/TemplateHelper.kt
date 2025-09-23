package io.mosip.injivcrenderer.utils

import com.fasterxml.jackson.databind.JsonNode
import io.mosip.injivcrenderer.constants.Constants.DIGEST_MULTIBASE
import io.mosip.injivcrenderer.constants.Constants.ID
import io.mosip.injivcrenderer.constants.Constants.TEMPLATE
import io.mosip.injivcrenderer.exceptions.VcRendererExceptions
import io.mosip.injivcrenderer.networkManager.NetworkManager
import io.mosip.injivcrenderer.networkManager.TemplateResponse

class TemplateHelper(private val traceabilityId: String) {

    private val className = TemplateHelper::class.simpleName

    fun extractSVG(renderMethod: JsonNode): List<String> {
        RenderMethodHelper(traceabilityId).validateSvgMustacheRenderSuite(renderMethod)
        RenderMethodHelper(traceabilityId).validateTemplateRenderMethodType(renderMethod)

        val templateValue = renderMethod.path(TEMPLATE)

        val templateId = templateValue.path(ID).asText(null)
            ?: throw VcRendererExceptions.MissingTemplateIdException(traceabilityId, className)
        val digestMultibase = templateValue.path(DIGEST_MULTIBASE).asText(null)

        val templateResponse = NetworkManager(traceabilityId).fetch(templateId)

        if (digestMultibase != null && !DigestMultibaseHelper(traceabilityId).validateDigestMultibase(templateResponse.body, digestMultibase)) {
            throw VcRendererExceptions.MultibaseValidationException(
                traceabilityId = traceabilityId,
                className = className,
                exceptionMessage = "Mismatch between fetched SVG and provided digestMultibase"
            )
        }
        return extractSVGList(templateResponse)

    }

    private fun extractSVGList(templateResponse: TemplateResponse): List<String> =
        if (templateResponse.isXmlTemplate()) {
            XMLHelper(traceabilityId).getSVGListFromPageSet(templateResponse.body)
        } else {
            listOf(templateResponse.body)
        }
}