package io.mosip.injivcrenderer.constants

import kotlin.test.Test
import kotlin.test.assertNull

class CredentialFormatTest {
    @Test
    fun `CredentialFormat fromValue should return null for an unsupported format`() {
        val unsupportedFormat = CredentialFormat.fromValue("mso_mdoc")

        assertNull(unsupportedFormat)
    }
}