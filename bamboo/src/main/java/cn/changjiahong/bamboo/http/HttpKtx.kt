package cn.changjiahong.bamboo.http

import cn.changjiahong.bamboo.base.utils.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * http组件默认初始化
 */
fun httpDefaultInit(iuApi: IUApi){
    val baseParamsLoggingInterceptor = HttpBaseParamsLoggingInterceptor.Builder()
        .addParamsMap(PublicHttpParams.getStaticParams())
        .build()
    val okHttpClient = OkHttpClient.Builder().addInterceptor(baseParamsLoggingInterceptor)
        .addInterceptor(ParamsSignInterceptor())
        .build()

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val retrofit = Retrofit.Builder()
        .baseUrl(iuApi.baseApi)
        .addCallAdapterFactory(FlowCallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()
    RetrofitManager.registerRetrofit(iuApi.baseApi, retrofit)
}