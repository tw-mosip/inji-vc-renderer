package io.mosip.svgrenderer

interface SvgRendererInterface {
    fun replaceSVGTemplatePlaceholders(template: String, data: Map<String, Any>): String
}