package io.mosip.injivcrenderer

interface InjiVcRendererInterface {
    fun replaceSVGTemplatePlaceholders(template: String, data: Map<String, Any>): String
}