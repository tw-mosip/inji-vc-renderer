package io.mosip.injivcrenderer

import io.mosip.injivcrenderer.common.encodeToBase64Url
import io.mosip.injivcrenderer.constants.Constants.SHA_256
import java.security.MessageDigest

class TestUtils {
    fun generateDigestMultibase(svgString: String): String {
        val svgBytes = svgString.toByteArray(Charsets.UTF_8)
        val hash = MessageDigest.getInstance(SHA_256).digest(svgBytes)
        val multihash = byteArrayOf(0x12, 0x20) + hash
        return "u" + encodeToBase64Url(multihash)
    }
}