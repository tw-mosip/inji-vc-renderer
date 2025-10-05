package io.mosip.injivcrenderer.templateEngine.svg

import io.mosip.injivcrenderer.common.encodeToBase64
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.fop.svg.PDFTranscoder
import org.apache.pdfbox.multipdf.PDFMergerUtility
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


actual fun svgListToPdfBase64(svgList: List<String>): String {
    val mergedPdf = PDFMergerUtility()
    val tempPdfStreams = mutableListOf<ByteArrayInputStream>()

    try {
        svgList.forEach { svg ->
            val transcoder = PDFTranscoder()
            val outputStream = ByteArrayOutputStream()
            val input = TranscoderInput(ByteArrayInputStream(svg.toByteArray()))
            val output = TranscoderOutput(outputStream)

            transcoder.transcode(input, output)
            outputStream.flush()

            tempPdfStreams.add(ByteArrayInputStream(outputStream.toByteArray()))
            outputStream.close()
        }

        val finalOutput = ByteArrayOutputStream()
        mergedPdf.destinationStream = finalOutput
        tempPdfStreams.forEach { mergedPdf.addSource(it) }
        mergedPdf.mergeDocuments(null)

        return encodeToBase64(finalOutput.toByteArray())
    } finally {
        tempPdfStreams.forEach { it.close() }
    }
}