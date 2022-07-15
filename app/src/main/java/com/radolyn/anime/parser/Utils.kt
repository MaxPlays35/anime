package parser

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import kotlin.coroutines.resumeWithException

const val baseUrl = "https://animego.org"

private val httpClient: OkHttpClient = OkHttpClient()

suspend fun getRequest(url: String): Document {
    val request = Request.Builder()
        .url(url)
        .addHeader(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.66 Safari/537.36 Edg/103.0.1264.44"
        )
        .build()
    val response = httpClient.newCall(request).await()
    if (!response.isSuccessful) {
        throw Exception("Unexpected code ${response.code}")
    }

    return Jsoup.parse(response.body?.string())
}

suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
    val callback = ContinuationCallCallback(this, continuation)
    enqueue(callback)
    continuation.invokeOnCancellation(callback)
}

internal class ContinuationCallCallback(
    private val call: Call,
    private val continuation: CancellableContinuation<Response>,
) : Callback, CompletionHandler {

    override fun onResponse(call: Call, response: Response) {
        if (continuation.isActive) {
            continuation.resume(response, onCancellation = null)
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        if (!call.isCanceled() && continuation.isActive) {
            continuation.resumeWithException(e)
        }
    }

    override fun invoke(cause: Throwable?) {
        runCatching {
            call.cancel()
        }.onFailure { e ->
            cause?.addSuppressed(e)
        }
    }
}
