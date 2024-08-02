package io.mosip.injivcrenderer
import java.text.SimpleDateFormat
import java.util.*

class InjiVcRenderer: InjiVcRendererInterface {


    fun getValueFromData(key: String, data: Map<String, Any?>): Any? {
        val keys = key.split("/")
        var value: Any? = data
        for (k in keys) {
            if (value is Map<*, *>) {
                value = (value as Map<String, Any?>)[k]
            } else {
                return null
            }
        }
        return value
    }

    fun formatDate(dateString: String): String {
        val date =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(dateString)
        if (date != null) {
            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            return formatter.format(date)
        }
        return ""
    }

    fun isValidDateTime(dateString: String): Boolean {
        return try {
            val date =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(dateString)
            date != null && date.year in 0..9999
        } catch (e: Exception) {
            false
        }
    }

    override fun replaceSVGTemplatePlaceholders(template: String, data: Map<String, Any>): String {
        val regex = Regex("\\{\\{(.*?)\\}\\}")
        return regex.replace(template) { match ->
            val key = match.groups[1]?.value?.trim('/') ?: ""
            val value = getValueFromData(key, data)
            if (value is String && isValidDateTime(value)) {
                formatDate(value as String)
            } else {
                value?.toString() ?: ""
            }
        }
    }
}