package cn.changjiahong.bamboo.http

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.*
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *
 * @author ChangJiahong
 * @date 2022/6/29
 */

class ResponseCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Flow<Response<T>>> {
    override fun adapt(call: Call<T>): Flow<Response<T>> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            continuation.resume(response)
                        }
                    })
                    continuation.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType
}


class BodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<T>> {
    override fun adapt(call: Call<T>): Flow<T> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            try {
                                if (response.isSuccessful) {
                                    val body = response.body()
                                    if (body == null) {
                                        val invocation =
                                            call.request().tag(Invocation::class.java)!!
                                        val method = invocation.method()
                                        val e = KotlinNullPointerException(
                                            "Response from " +
                                                    method.declaringClass.name +
                                                    '.' +
                                                    method.name +
                                                    " was null but response body type was declared as non-null"
                                        )
                                        continuation.resumeWithException(e)
                                    } else {
                                        continuation.resume(body)
                                    }
                                } else {
                                    continuation.resumeWithException(HttpException(response))
                                }
                            } catch (e: Exception) {
                                continuation.resumeWithException(e)
                            }
                        }
                    })
                    continuation.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType
}