package io.mosip.injivcrenderer.common

expect fun encodeToBase64Url(data: ByteArray): String

expect fun encodeToBase64(data: ByteArray): String