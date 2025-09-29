package io.mosip.injivcrenderer

import com.fasterxml.jackson.databind.ObjectMapper
import io.mosip.injivcrenderer.constants.CredentialFormat
import io.mosip.injivcrenderer.exceptions.VcRendererExceptions
import io.mosip.injivcrenderer.templateEngine.svg.svgListToPdfBase64
import io.mosip.injivcrenderer.utils.PlaceholderReplacementHelper
import io.mosip.injivcrenderer.utils.RenderMethodHelper
import io.mosip.injivcrenderer.utils.TemplateHelper

class InjiVcRenderer(private val traceabilityId: String) {

    private val mapper = ObjectMapper()
    private val templateHelper = TemplateHelper(traceabilityId)
    private val renderMethodHelper = RenderMethodHelper(traceabilityId)
    private val placeholderReplacementHelper = PlaceholderReplacementHelper(traceabilityId)

    /**
     * Renders SVG templates defined in the VC's renderMethod section.
     * Supports fetching templates from URLs and data URIs.
     * Replaces placeholders in the templates with values from the VC JSON.
     *
     * @param credentialFormat The format of the credential. Currently only LDP_VC is supported.
     * @param wellKnownJson Optional well-known JSON.
     * @param vcJsonString The Verifiable Credential as a JSON string.
     * @return A list of rendered SVG strings.
     */
    @JvmOverloads
    fun renderVC(
        credentialFormat: CredentialFormat,
        wellKnownJson: String? = null,
        vcJsonString: String
    ): List<Any> {

        if (credentialFormat != CredentialFormat.LDP_VC) {
            throw VcRendererExceptions.UnsupportedCredentialFormat(
                traceabilityId = traceabilityId,
                className = this::class.simpleName
            )
        }

        val vcJsonNode = mapper.readTree(vcJsonString)
        val renderMethodArray = renderMethodHelper.parseRenderMethod(vcJsonNode)

        return renderMethodArray.flatMap { renderMethodElement ->
            templateHelper.extractSVG(renderMethodElement).map { rawSvg ->
                placeholderReplacementHelper.replaceSvgPlaceholders(rawSvg, vcJsonNode, renderMethodElement, vcJsonString)
            }
        }
    }

    /** Converts a list of SVG strings to a PDF Base64 string */
    fun convertSvgToPdf(svgList: List<String>) = svgListToPdfBase64(svgList)
}
