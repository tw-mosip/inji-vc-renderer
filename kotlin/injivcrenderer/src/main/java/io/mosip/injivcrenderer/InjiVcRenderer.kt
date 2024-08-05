package io.mosip.injivcrenderer
import io.mosip.injivcrenderer.DateUtils.formatDate
import io.mosip.injivcrenderer.DateUtils.isValidDateTime
import org.json.JSONObject

class InjiVcRenderer: InjiVcRendererInterface {

    // Method to get value from nested JSON data
    fun getValueFromData(key: String, data: JSONObject): Any? {
        val keys = key.split("/")
        var value: Any? = data
        for (k in keys) {
            if (value is JSONObject) {
                value = value.opt(k)
            } else {
                return null
            }
        }
        return value
    }



    // Method to replace placeholders in the template
    override fun replaceSVGTemplatePlaceholders(svgTemplate: String, vcJsonData: String): String {
        val jsonObject = JSONObject(vcJsonData)
        val regex = Regex("\\{\\{(.*?)\\}\\}")
        return regex.replace(svgTemplate) { match ->
            val key = match.groups[1]?.value?.trim() ?: ""
            val value = getValueFromData(key, jsonObject)
            when {
                value is String && isValidDateTime(value) -> formatDate(value)
                value != null -> value.toString()
                else -> ""
            }
        }
    }
}
