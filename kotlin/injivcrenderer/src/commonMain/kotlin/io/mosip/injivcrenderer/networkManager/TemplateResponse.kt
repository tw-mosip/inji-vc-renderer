package io.mosip.injivcrenderer.networkManager

import io.mosip.injivcrenderer.constants.Constants.PAGESET
import io.mosip.injivcrenderer.constants.ContentType

data class TemplateResponse(val contentType: ContentType, val body: String) {
    fun isXmlWithPageSet(): Boolean {
        return contentType == ContentType.XML &&
                body.trimStart().lowercase().startsWith("<$PAGESET")
    }
}
