package io.mosip.injivcrenderer

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale

object DateUtils {

    private const val DATE_FORMAT = "yyyy-MM-dd"

    fun formatDate(dateString: String): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return try {
            val date = dateFormat.parse(dateString)
            val formattedDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            formattedDate.format(date)
        } catch (e: Exception) {
            "" // Handle parsing exception (e.g., invalid date format)
        }
    }

    fun isValidDateTime(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        dateFormat.isLenient = false // Strict parsing

        return try {
            val date = dateFormat.parse(dateString)
            // Validate year within a reasonable range (e.g., between 0 and 9999)
            val calendar = Calendar.getInstance()
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            year >= 0 && year <= 9999
        } catch (e: Exception) {
            false // Return false if parsing fails or validation condition not met
        }
    }
}