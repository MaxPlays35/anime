@file:JvmName("OkHttpUtils")

package com.radolyn.anime.parser

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Response
import com.radolyn.anime.parser.util.ContinuationCallCallback

suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
    val callback = ContinuationCallCallback(this, continuation)
    enqueue(callback)
    continuation.invokeOnCancellation(callback)
}

val Response.mimeType: String?
    get() = body?.contentType()?.run { "$type/$subtype" }

val Response.contentDisposition: String?
    get() = header("Content-Disposition")