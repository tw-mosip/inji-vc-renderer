package io.mosip.injivcrenderer.common

import java.util.Base64.getEncoder
import java.util.Base64.getUrlEncoder

actual fun encodeToBase64Url(data: ByteArray): String {
    return getUrlEncoder().encodeToString(data)
}

actual fun encodeToBase64(data: ByteArray): String {
    return getEncoder().encodeToString(data)
}