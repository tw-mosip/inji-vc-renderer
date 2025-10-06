package io.mosip.injivcrenderer.networkManager

import io.mosip.injivcrenderer.constants.ContentType
import io.mosip.injivcrenderer.exceptions.VcRendererExceptions
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkManager(
    private val traceabilityId: String,
    private val client: OkHttpClient = OkHttpClient()
) {
    fun fetch(url: String): TemplateResponse {
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

                val contentType = ContentType.fromType(response.header("Content-Type"), traceabilityId, this::class.simpleName)
                val body = response.body?.string()
                TemplateResponse(contentType, body.orEmpty())
            }
        } catch (e: VcRendererExceptions.SvgFetchException) {
            throw e
        } catch (e: Exception) {
            throw VcRendererExceptions.SvgFetchException(
                traceabilityId,
                this::class.simpleName,
                e.message ?: "Unexpected error"
            )
        }
    }
}
