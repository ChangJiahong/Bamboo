package cn.changjiahong.bamboo.http

import android.net.Uri
import cn.changjiahong.bamboo.base.utils.fromJson
import cn.changjiahong.bamboo.base.utils.sign
import cn.changjiahong.bamboo.base.utils.toJson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.IOException

/**
 *
 * @author ChangJiahong
 * @date 2022/7/12
 */
class ParamsSignInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        if ("POST" == request.method) {
            // 把已有的post参数添加到新的构造器
            if (request.body is FormBody) {
                // FormBody和url不太一样，若需添加公共参数，需要新建一个构造器
                val bodyBuilder = FormBody.Builder();
                val formBody = request.body as FormBody
                val params = HashMap<String, String>()
                for (i in 0 until formBody.size) {
                    params[formBody.name(i)] = formBody.value(i)
                    bodyBuilder.add(formBody.name(i), formBody.value(i))
                }

                val sign = params.sign()

                bodyBuilder.add(PublicHttpParams.SIGN, sign)
                requestBuilder.post(bodyBuilder.build())
            } else if (request.body != null && request.body!!.contentType()?.subtype == "json") {
                val json = HttpBaseParamsLoggingInterceptor.bodyToString(request.body)
                val mp = HashMap<String, Any>()
                json.fromJson<Map<String, Any>>()?.let { mp.putAll(it) }
                val sign = mp.sign()
                mp[PublicHttpParams.SIGN] = sign
                requestBuilder.post(
                    mp.toMap().toJson().toRequestBody("application/json".toMediaType())
                )
            } else if (request.body is MultipartBody) {
                val multipartBody = MultipartBody.Builder()
                multipartBody.setType(MultipartBody.FORM)
                val params = HashMap<String, String>()
                val oldBody = request.body as MultipartBody
                val (files, _params) = getMultipartBody(oldBody)
                params.putAll(_params)
                for (i in 0 until oldBody.size) {
                    multipartBody.addPart(oldBody.part(i))
                }
                val sign = params.sign()
                multipartBody.addPart(
                    MultipartBody.Part.createFormData(
                        PublicHttpParams.SIGN,
                        null,
                        sign.toRequestBody("text/plain".toMediaType())
                    )
                )

                requestBuilder.post(multipartBody.build())

            }
        }
        return chain.proceed(requestBuilder.build())
    }


    fun getMultipartBody(requestBody: MultipartBody): Pair<Map<String, String>, Map<String, String>> {
        val params = mutableMapOf<String, String>()
        val files = mutableMapOf<String, String>()
        requestBody.parts.forEach {
            val body = it.body
            it.headers?.let {
                val header = it.value(0)
                val split = header.replace(" ", "").replace("\"", "").split(";")
                when (split.size) {
                    2 -> {
                        //文本参数
                        val keys = split[1].split("=")
                        if (keys.size > 1 && body.contentLength() < 1024) {
                            val key = keys[1]
                            val buffer = Buffer()
                            body.writeTo(buffer)
                            val value = buffer.readUtf8()
                            params[key] = value
                        }
                    }
                    3 -> {
                        //文件
                        val fileKeys = split[1].split("=")
                        val fileKey = if (fileKeys.size > 1) {
                            fileKeys[1]
                        } else ""
                        val nameValue = split[2].split("=")
                        val fileName = if (nameValue.size > 1) nameValue[1] else ""
                        files[fileKey] = fileName
                    }
                }
            }
        }
        return Pair(files, params)
    }
}