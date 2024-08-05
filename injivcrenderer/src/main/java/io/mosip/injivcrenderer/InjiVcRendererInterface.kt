package io.mosip.injivcrenderer

interface InjiVcRendererInterface {
    fun replaceSVGTemplatePlaceholders(template: String, vcJsonString: String): String
}