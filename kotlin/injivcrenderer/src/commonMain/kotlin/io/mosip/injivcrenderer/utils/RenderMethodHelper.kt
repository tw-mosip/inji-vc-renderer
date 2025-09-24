package io.mosip.injivcrenderer.utils


import com.fasterxml.jackson.databind.JsonNode
import io.mosip.injivcrenderer.constants.Constants.RENDER_METHOD
import io.mosip.injivcrenderer.constants.Constants.RENDER_SUITE
import io.mosip.injivcrenderer.constants.Constants.SVG_MUSTACHE
import io.mosip.injivcrenderer.constants.Constants.TEMPLATE_RENDER_METHOD
import io.mosip.injivcrenderer.constants.Constants.TYPE
import io.mosip.injivcrenderer.exceptions.VcRendererExceptions

class RenderMethodHelper(private val traceabilityId: String) {
    private val className = RenderMethodHelper::class.simpleName

    private fun isSvgMustacheRenderSuite(renderMethod: JsonNode): Boolean {
        val renderSuite = renderMethod.path(RENDER_SUITE).asText("")
        return renderSuite == SVG_MUSTACHE
    }

    fun validateSvgMustacheRenderSuite(renderMethod: JsonNode) {
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

    fun validateTemplateRenderMethodType(renderMethod: JsonNode) {
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