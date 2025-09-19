package io.mosip.injivcrenderer.pdf


/**
 * Fill the PDF form with the given values and return as Base64 string.
 *
 * @param inputBytes PDF file bytes
 * @param values Map of field names to field values
 * @return Base64-encoded filled PDF
 */
expect fun fillPdfToBase64(inputBytes: ByteArray, values: Map<String, String>): String


/**
 * Extract all form field names from a PDF.
 *
 * @param inputBytes PDF file bytes
 * @return Map of fieldName -> current field value (empty string if none)
 */
expect fun extractPdfFields(inputBytes: ByteArray): Map<String, String>