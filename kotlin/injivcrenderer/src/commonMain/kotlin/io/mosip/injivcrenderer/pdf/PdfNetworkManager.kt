package io.mosip.injivcrenderer.pdf


import io.mosip.injivcrenderer.exceptions.VcRendererExceptions
import okhttp3.OkHttpClient
import okhttp3.Request

class PdfNetworkManager(
    private val traceabilityId: String,
    private val client: OkHttpClient = OkHttpClient()
) {
    fun fetchPdfAsBytes(url: String): ByteArray {
        val request = Request.Builder().url(url).build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw VcRendererExceptions.SvgFetchException(
                        traceabilityId,
                        this::class.simpleName,
                        "Unexpected response code $response"
                    )
                }

                response.body?.bytes()
                    ?: throw VcRendererExceptions.SvgFetchException(
                        traceabilityId,
                        this::class.simpleName,
                        "Empty PDF response body"
                    )
            }
        } catch (e: Exception) {
            println("::::::::Exception during fetching pdf from url: $url :::::::::: $e")
            throw e
        }
    }
}
