package io.mosip.injivcrenderer.constants

import io.mosip.injivcrenderer.exceptions.VcRendererExceptions

enum class ContentType(val type: String) {
    SVG("image/svg+xml"),
    XML("application/xml");

    companion object {
        fun fromType(type: String?, traceabilityId: String, className: String?): ContentType =
            entries.firstOrNull { it.type.equals(type, ignoreCase = true) }
                ?: throw VcRendererExceptions.SvgFetchException(
                    traceabilityId = traceabilityId,
                    className = className,
                    exceptionMessage = "Unsupported content type: $type"
                )
    }
}