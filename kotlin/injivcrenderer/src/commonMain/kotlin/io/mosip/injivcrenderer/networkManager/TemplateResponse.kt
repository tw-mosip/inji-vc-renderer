package io.mosip.injivcrenderer.networkManager

import io.mosip.injivcrenderer.constants.ContentType

data class TemplateResponse(val contentType: ContentType, val body: String) {
    fun isXmlTemplate(): Boolean {
        return contentType == ContentType.XML
    }
}
