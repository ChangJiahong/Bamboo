package cn.changjiahong.bamboo.base.repository

import cn.changjiahong.bamboo.base.utils.MoshiUtil
import cn.changjiahong.bamboo.base.utils.fromJson
import cn.changjiahong.bamboo.base.utils.genericType
import cn.changjiahong.bamboo.http.RetrofitManager
import cn.changjiahong.bamboo.http.exp.ApiExp
import cn.changjiahong.bamboo.http.exp.StatusMsg
import cn.changjiahong.bamboo.http.model.RestResponse
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.rawType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import kotlin.reflect.jvm.javaType

/**
 *
 * @author ChangJiahong
 * @date 2023/3/9
 */
abstract class IRepository(val baseApi: String) {
}



inline fun <reified T, reified F : Any> IRepository.requestService(crossinline block: T.() -> Flow<RestResponse<F>>) =
    flow {
        RetrofitManager.getRetrofit(baseApi)
            .create(T::class.java).run {
                block()
                    .onStart {
//                        ConfigRepositoryImpl().getBuvid()
                    }
                    .catch { cause ->
                        cause.printStackTrace()
                        when (cause) {
                            is HttpException -> throw ApiExp(
                                StatusMsg.HttpError,
                                cause.message() ?: "",
                                cause.response()
                            )
                            else -> throw ApiExp(StatusMsg.UnknownError, cause.message ?: "")
                        }
                    }.flowOn(Dispatchers.IO).collect {
                        if (it.code == StatusMsg.OK.code) {
                            if (it.data != null) {
                                emit(it.data)
                            } else {
                                // 发送默认值
                                val value: Any? = when (val type = genericType<F>()) {
                                    Integer::class.java ,Int::class -> 0
                                    String::class -> ""
                                    Boolean::class -> false
                                    Byte::class -> 0.toByte()
                                    Char::class -> Char.MIN_VALUE
                                    Double::class -> 0.0
                                    Float::class -> 0f
                                    Long::class -> 0L
                                    Short::class -> 0.toShort()
                                   else -> {
                                       if (Collection::class.java.isAssignableFrom(type.rawType)) {
                                           "[]".fromJson<F>(type)
                                       } else {
                                           "{}".fromJson<F>(type)
                                       }
                                   }
                               }
                                if (value!=null) {
                                    emit(value as F)
                                }else{
                                    throw ApiExp(StatusMsg.DeserializationError)
                                }
                            }
                        } else {
                            throw ApiExp(StatusMsg.valueOfCode(it.code), it.msg, it.data)
                        }
                    }
            }
    }
