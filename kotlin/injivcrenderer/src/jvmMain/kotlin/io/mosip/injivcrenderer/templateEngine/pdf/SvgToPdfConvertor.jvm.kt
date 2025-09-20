package io.mosip.injivcrenderer.templateEngine.pdf
import org.apache.fop.svg.PDFTranscoder
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64


actual fun svgListToPdfBase64(svgList: List<String>): String {
    val outputStream = ByteArrayOutputStream()
    val transcoder = PDFTranscoder()

    svgList.forEach { svg ->
        val input = TranscoderInput(ByteArrayInputStream(svg.toByteArray()))
        val output = TranscoderOutput(outputStream)
        transcoder.transcode(input, output)
    }

    outputStream.flush()
    val pdfBytes = outputStream.toByteArray()
    outputStream.close()

    return Base64.getEncoder().encodeToString(pdfBytes)
}