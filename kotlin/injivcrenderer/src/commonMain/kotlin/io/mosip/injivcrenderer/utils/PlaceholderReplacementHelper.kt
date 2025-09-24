package io.mosip.injivcrenderer.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.mosip.injivcrenderer.constants.Constants.QR_CODE_FALLBACK_IMAGE_ID
import io.mosip.injivcrenderer.constants.Constants.QR_CODE_IMAGE_ID
import io.mosip.injivcrenderer.constants.Constants.QR_CODE_PLACEHOLDER
import io.mosip.injivcrenderer.constants.Constants.QR_IMAGE_PREFIX
import io.mosip.injivcrenderer.constants.Constants.RENDER_PROPERTY
import io.mosip.injivcrenderer.constants.Constants.TEMPLATE
import io.mosip.injivcrenderer.qrCode.QrCodeGenerator
import io.mosip.injivcrenderer.qrCode.QrCodeGenerator.Companion.DEFAULT_FALLBACK_QR_BASE64
import io.mosip.injivcrenderer.templateEngine.svg.JsonPointerResolver

class PlaceholderReplacementHelper(private val traceabilityId: String) {

    private val mapper = ObjectMapper()

    fun replaceSvgPlaceholders(
        svg: String,
        vcJsonNode: JsonNode,
        renderMethodElement: JsonNode,
        wellKnownJson: String?,
        vcJsonString: String
    ): String {
        val withQrCode = replaceQrCodePlaceholder(svg, vcJsonString)
        val withWellKnown = replaceWellKnownPlaceholders(withQrCode, wellKnownJson)
        return replaceVcPlaceholders(withWellKnown, vcJsonNode, renderMethodElement)
    }

    private fun replaceWellKnownPlaceholders(svg: String, wellKnownJson: String?): String {
        val wellKnownJsonNode: JsonNode =
            if (!wellKnownJson.isNullOrEmpty()) mapper.readTree(wellKnownJson)
            else mapper.createObjectNode()

        return JsonPointerResolver(traceabilityId).replacePlaceholders(
            svgTemplate = svg,
            jsonNode = wellKnownJsonNode,
            isLabelPlaceholder = true
        )
    }

    private fun replaceVcPlaceholders(svg: String, vcJsonNode: JsonNode, element: JsonNode): String {
        val renderProperties =
            element.path(TEMPLATE).path(RENDER_PROPERTY)
                .takeIf { it.isArray }
                ?.map { it.asText() }

        return JsonPointerResolver(traceabilityId).replacePlaceholders(
            svgTemplate = svg,
            jsonNode = vcJsonNode,
            renderProperties = renderProperties
        )
    }

    private fun replaceQrCodePlaceholder(svg: String, vcJsonString: String): String {
        return if (!svg.contains(QR_CODE_PLACEHOLDER)) {
            svg
        } else {
            val qrBase64 = try {
                QrCodeGenerator(traceabilityId).generateQRCodeImage(vcJsonString)
            } catch (e: Exception) {
                println("[$traceabilityId] QR generation failed: ${e.message}")
                null
            }

            val finalQrBase64 = qrBase64.takeUnless { it.isNullOrEmpty() } ?: DEFAULT_FALLBACK_QR_BASE64
            val qrImageTag = "$QR_IMAGE_PREFIX,$finalQrBase64"

            val imageId = if (qrBase64.isNullOrEmpty()) QR_CODE_FALLBACK_IMAGE_ID else QR_CODE_IMAGE_ID

            return svg
                .replace(QR_CODE_PLACEHOLDER, qrImageTag)
                .replace(QR_CODE_IMAGE_ID, imageId)
        }
    }
}