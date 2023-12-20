package cn.changjiahong.bamboo.http

import cn.changjiahong.bamboo.base.utils.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * @description
 * @author ChangJiahong
 * @date 2021/9/12
 */
object RetrofitManager {

    @JvmStatic
    private val retrofitMap = HashMap<String, Retrofit>()

    fun getRetrofit(url: String): Retrofit {
        return retrofitMap[url] ?: createRetrofit(url)
    }

    private fun createRetrofit(url: String): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(OkHttpClient())
            .build()
        registerRetrofit(url, retrofit)
        return retrofit
    }

    fun registerRetrofit(url: String){
        if (!retrofitMap.containsKey(url)){
            createRetrofit(url)
        }
    }

    fun registerRetrofit(url: String, retrofit: Retrofit) {
        retrofitMap[url] = retrofit
    }

}