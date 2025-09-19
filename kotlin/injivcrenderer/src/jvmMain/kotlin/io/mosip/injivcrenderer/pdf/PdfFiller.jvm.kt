package io.mosip.injivcrenderer.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64

/**
 * Fill the PDF form with the given values and return as Base64 string.
 *
 * @param inputBytes PDF file bytes
 * @param values Map of field names to field values
 * @return Base64-encoded filled PDF
 */

actual fun fillPdfToBase64(inputBytes: ByteArray, values: Map<String, String>): String {
    val inputStream = ByteArrayInputStream(inputBytes)
    val document = PDDocument.load(inputStream)
    val acroForm: PDAcroForm = document.documentCatalog.acroForm
        ?: throw IllegalArgumentException("PDF does not have an AcroForm")

    values.forEach { (fieldName, value) ->
        val field = acroForm.getField(fieldName)
        field?.setValue(value)
            ?: println("Warning: Field '$fieldName' not found")
    }

    // Optional: flatten the fields
    acroForm.flatten()

    val outputStream = ByteArrayOutputStream()
    document.save(outputStream)
    document.close()

    return Base64.getEncoder().encodeToString(outputStream.toByteArray())
}

/**
 * Extract all form field names from a PDF.
 *
 * @param inputBytes PDF file bytes
 * @return Map of fieldName -> current field value (empty string if none)
 */
actual fun extractPdfFields(inputBytes: ByteArray): Map<String, String> {
    val document = PDDocument.load(ByteArrayInputStream(inputBytes))
    val acroForm: PDAcroForm = document.documentCatalog.acroForm
        ?: throw IllegalArgumentException("PDF does not have an AcroForm")

    val result = acroForm.fields.associate { field ->
        field.partialName to (field.valueAsString ?: "")
    }

    document.close()
    return result
}