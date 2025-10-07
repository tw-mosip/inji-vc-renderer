package io.mosip.injivcrenderer.common

import android.annotation.SuppressLint
import android.os.Build
import android.util.Base64.NO_PADDING
import android.util.Base64.NO_WRAP
import android.util.Base64.encodeToString
import java.util.Base64.getUrlEncoder

actual fun encodeToBase64Url(data: ByteArray): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        javaBase64UrlEncode(data)
    } else {
        androidBase64UrlEncode(data)
    }

}

@SuppressLint("NewApi")
private fun javaBase64UrlEncode(data: ByteArray): String =
    getUrlEncoder().encodeToString(data)

private fun androidBase64UrlEncode(data: ByteArray): String {
    return encodeToString(data, NO_PADDING)
}

actual fun encodeToBase64(data: ByteArray): String {
    return encodeToString(data, NO_WRAP)
}