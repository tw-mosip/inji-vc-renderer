package io.mosip.injivcrenderer.utils
import io.mosip.injivcrenderer.constants.Constants.PAGE
import io.mosip.injivcrenderer.constants.Constants.PAGESET
import io.mosip.injivcrenderer.constants.Constants.SVG
import io.mosip.injivcrenderer.exceptions.VcRendererExceptions
import org.w3c.dom.Element
import java.io.ByteArrayInputStream
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import io.mosip.injivcrenderer.extensions.getElementsByTagNameIgnoreCase

class XMLHelper(private val traceabilityId: String) {

    fun getSVGListFromPageSet(xml: String): List<String> {
        try {
            val svgList = mutableListOf<String>()
            val factory = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
            val builder = factory.newDocumentBuilder()
            val document = builder.parse(ByteArrayInputStream(xml.toByteArray(Charsets.UTF_8)))

            validatePageSetRoot(document.documentElement)

            val pages = document.getElementsByTagNameIgnoreCase(PAGE)
            validatePagesExist(pages.length)

            for (i in 0 until pages.length) {
                val page = pages.item(i) as Element
                val svgNode = getSvgNodeFromPage(page, i)
                val writer = StringWriter()
                TransformerFactory.newInstance().newTransformer().apply {
                    setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
                    setOutputProperty(OutputKeys.INDENT, "no")
                }.transform(DOMSource(svgNode), StreamResult(writer))

                svgList.add(writer.toString())
            }
            return svgList
        } catch (e: Exception) {
            throw VcRendererExceptions.PageSetParsingException(traceabilityId, this::class.simpleName, e.message ?: "Error parsing PageSet XML")
        }
    }

    private fun validatePageSetRoot(root: Element) {
        if (!root.nodeName.equals(PAGESET, ignoreCase = true)) {
            throw VcRendererExceptions.PageSetParsingException(
                traceabilityId,
                this::class.simpleName,
                "Root element must be <Pageset>"
            )
        }
    }

    private fun validatePagesExist(pagesLength: Int) {
        if (pagesLength == 0) {
            throw VcRendererExceptions.PageSetParsingException(
                traceabilityId,
                this::class.simpleName,
                "<Pageset> must contain at least one <Page>"
            )
        }
    }

    private fun getSvgNodeFromPage(page: Element, pageIndex: Int): Element {
        val svgNodes = page.getElementsByTagNameIgnoreCase(SVG)
        if (svgNodes.length == 0) {
            throw VcRendererExceptions.PageSetParsingException(
                traceabilityId,
                this::class.simpleName,
                "<Page> at index $pageIndex does not contain a <svg> element"
            )
        }
        return svgNodes.item(0) as Element
    }

}