package cn.changjiahong.bamboo.http

import cn.changjiahong.bamboo.base.utils.fromJson
import cn.changjiahong.bamboo.base.utils.toJson
import cn.changjiahong.bamboo.koin.A
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject
import java.io.IOException

/**
 * 公共参数注入
 * @author ChangJiahong
 * @date 2022/6/29
 */

class HttpBaseParamsLoggingInterceptor private constructor() : Interceptor {
    var queryParamsMap: MutableMap<String, String> = HashMap()
    var paramsMap: MutableMap<String, String> = HashMap()
    var headerParamsMap: MutableMap<String, String> = HashMap()
    var headerLinesList: MutableList<String> = ArrayList()


    val POST = "POST"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()

        // process 标头参数注入
        val headerBuilder: Headers.Builder = request.headers.newBuilder()
        if (headerParamsMap.isNotEmpty()) {
            headerParamsMap.forEach { (key, value) ->
                headerBuilder.add(key, value)
            }
        }
        if (headerLinesList.size > 0) {
            for (line in headerLinesList) {
                headerBuilder.add(line)
            }
        }
        requestBuilder.headers(headerBuilder.build())
        // process 标头参数结束


        //进程查询参数注入任何它是什么 GET 或 POST
        if (queryParamsMap.isNotEmpty()) {
            injectParamsIntoUrl(request, requestBuilder, queryParamsMap)
        }
        // process查询参数结束

        // 注入动态获取的公共参数
        paramsMap.putAll(PublicHttpParams.getDynamicParams())

        // process post body inject
        if (request.method == POST && request.body is FormBody) {
            val formBodyBuilder = FormBody.Builder()
            val oldFormBody = request.body as FormBody
            for (i in 0 until oldFormBody.size) {
                formBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i))
            }
            if (paramsMap.isNotEmpty()) {
                paramsMap.forEach { (key, value) ->
                    formBodyBuilder.add(key, value)
                }
            }
            requestBuilder.post(formBodyBuilder.build())
        } else if (request.body!=null && request.body!!.contentType()?.subtype == "json") {
            val json = bodyToString(request.body)
            val mp = HashMap<String,Any>()
            json.fromJson<Map<String,Any>>()?.let { mp.putAll(it) }
            if (paramsMap.isNotEmpty()) {
                paramsMap.forEach { (key, value) ->
                    mp[key] = value
                }
            }
            requestBuilder.post(mp.toMap().toJson().toRequestBody("application/json".toMediaType()))
        } else if (request.method == POST && request.body is MultipartBody){
            val multipartBody = MultipartBody.Builder()
            multipartBody.setType(MultipartBody.FORM)
            val oldBody = request.body as MultipartBody
//            val params = HashMap<String, String>()
            for (i in 0 until oldBody.size) {
                multipartBody.addPart(oldBody.part(i))
            }
            if (paramsMap.isNotEmpty()) {
                paramsMap.forEach { (key, value) ->
//                    params[key] = value
                    multipartBody.addPart(MultipartBody.Part.createFormData(key,null,value.toRequestBody("text/plain".toMediaType())))
                }
            }
            requestBuilder.post(multipartBody.build())
        } else {
            val formBodyBuilder = FormBody.Builder()
            if (paramsMap.isNotEmpty()) {
                paramsMap.forEach { (key, value) ->
                    formBodyBuilder.add(key, value)
                }
            }
            requestBuilder.post(formBodyBuilder.build())
//            injectParamsIntoUrl(request, requestBuilder, paramsMap)
        }
        request = requestBuilder.build()
        return chain.proceed(request)
    }

    // func to inject params into url
    private fun injectParamsIntoUrl(
        request: Request,
        requestBuilder: Request.Builder,
        paramsMap: Map<String, String>
    ) {
        val httpUrlBuilder = request.url.newBuilder()
        if (paramsMap.isNotEmpty()) {
            paramsMap.forEach { (key, value) ->
                httpUrlBuilder.addQueryParameter(key, value)
            }
        }
        requestBuilder.url(httpUrlBuilder.build())
    }

    class Builder {
        var interceptor = HttpBaseParamsLoggingInterceptor()
        fun addParam(key: String, value: String): Builder {
            interceptor.paramsMap[key] = value
            return this
        }

        fun addParamsMap(paramsMap: Map<String, String>?): Builder {
            interceptor.paramsMap.putAll(paramsMap!!)
            return this
        }

        fun addHeaderParam(key: String, value: String): Builder {
            interceptor.headerParamsMap[key] = value
            return this
        }

        fun addHeaderParamsMap(headerParamsMap: Map<String, String>?): Builder {
            interceptor.headerParamsMap.putAll(headerParamsMap!!)
            return this
        }

        fun addHeaderLine(headerLine: String): Builder {
            val index = headerLine.indexOf(":")
            require(index != -1) { "Unexpected header: $headerLine" }
            interceptor.headerLinesList.add(headerLine)
            return this
        }

        fun addHeaderLinesList(headerLinesList: List<String>): Builder {
            for (headerLine in headerLinesList) {
                val index = headerLine.indexOf(":")
                require(index != -1) { "Unexpected header: $headerLine" }
                interceptor.headerLinesList.add(headerLine)
            }
            return this
        }

        fun addQueryParam(key: String, value: String): Builder {
            interceptor.queryParamsMap[key] = value
            return this
        }

        fun addQueryParamsMap(queryParamsMap: Map<String, String>?): Builder {
            interceptor.queryParamsMap.putAll(queryParamsMap!!)
            return this
        }

        fun build(): HttpBaseParamsLoggingInterceptor {
            return interceptor
        }

    }

    companion object {
        fun bodyToString(request: RequestBody?): String {
            return try {
                val buffer = Buffer()
                if (request != null) request.writeTo(buffer) else return ""
                buffer.readUtf8()
            } catch (e: IOException) {
                "did not work"
            }
        }
    }
}
