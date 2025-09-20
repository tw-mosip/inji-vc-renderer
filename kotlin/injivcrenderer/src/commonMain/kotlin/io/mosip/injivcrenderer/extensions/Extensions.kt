package io.mosip.injivcrenderer.extensions

import io.mosip.injivcrenderer.exceptions.VcRendererExceptions
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

fun Node.getElementsByTagNameIgnoreCase(tagName: String): NodeList {
    val allElements = when (this.nodeType) {
        Node.DOCUMENT_NODE -> (this as Document).getElementsByTagName("*")
        Node.ELEMENT_NODE -> (this as Element).getElementsByTagName("*")
        else -> return object : NodeList {
            override fun getLength() = 0
            override fun item(index: Int) = null
        }
    }

    val filtered = (0 until allElements.length)
        .map { allElements.item(it) }
        .filter { it.nodeName.equals(tagName, ignoreCase = true) }

    return object : NodeList {
        override fun getLength(): Int = filtered.size
        override fun item(index: Int): Node? = filtered.getOrNull(index)
    }
}

fun Document.validatePageset(traceabilityId: String, className: String) {
    val pageSetNodes = this.getElementsByTagNameIgnoreCase("Pageset")
    if (pageSetNodes.length == 0) {
        throw VcRendererExceptions.PageSetParsingException(traceabilityId, className)
    }

    for (i in 0 until pageSetNodes.length) {
        val pageSet = pageSetNodes.item(i) as? Element ?: continue
        val pages = pageSet.getElementsByTagNameIgnoreCase("Page")
        if (pages.length == 0) {
            throw VcRendererExceptions.MissingPageElement(traceabilityId, className)
        }
    }
}