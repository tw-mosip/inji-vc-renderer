package io.mosip.injivcrenderer
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class InjiVcRenderer {

    // Method to get value from nested JSON data
    private fun getValueFromData(key: String, data: JSONObject): Any? {
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

    // Method to format date
    private fun formatDate(dateString: String): String {
        val date =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(dateString)
        if (date != null) {
            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            return formatter.format(date)
        }
        return ""
    }

    // Method to check if date is valid
    private fun isValidDateTime(dateString: String): Boolean {
        return try {
            val date =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(dateString)
            date != null && date.year in 0..9999
        } catch (e: Exception) {
            false
        }
    }

    // Method to replace placeholders in the template
    fun replaceSVGTemplatePlaceholders(template: String, jsonData: String): String {
        val jsonObject = JSONObject(jsonData)
        val regex = Regex("\\{\\{(.*?)\\}\\}")
        return regex.replace(template) { match ->
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
