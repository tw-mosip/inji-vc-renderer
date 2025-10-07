package io.mosip.injivcrenderer.templateEngine.svg


import android.graphics.Canvas
import android.graphics.Picture
import android.graphics.pdf.PdfDocument
import com.caverock.androidsvg.SVG
import io.mosip.injivcrenderer.common.encodeToBase64
import java.io.ByteArrayOutputStream

actual fun svgListToPdfBase64(svgList: List<String>): String {
    val pdfDocument = PdfDocument()

    svgList.forEachIndexed { index, svgString ->
        val svg = SVG.getFromString(svgString)
        val picture: Picture = svg.renderToPicture()
        val pageInfo = PdfDocument.PageInfo.Builder(
            picture.width,
            picture.height,
            index + 1
        ).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        canvas.drawPicture(picture)
        pdfDocument.finishPage(page)
    }

    val outputStream = ByteArrayOutputStream()
    pdfDocument.writeTo(outputStream)
    pdfDocument.close()

    return encodeToBase64(outputStream.toByteArray())
}