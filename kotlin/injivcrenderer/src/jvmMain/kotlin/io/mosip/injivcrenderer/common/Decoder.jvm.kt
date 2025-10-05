package io.mosip.injivcrenderer.common

import java.util.Base64

actual fun decodeFromBase64Url(content: String): ByteArray {
    return Base64.getUrlDecoder().decode(content.toByteArray())
}