package cn.changjiahong.bamboo.base.app

import android.content.Context
import androidx.startup.Initializer
import cn.changjiahong.bamboo.http.FlowCallAdapterFactory
import cn.changjiahong.bamboo.http.HttpBaseParamsLoggingInterceptor
import cn.changjiahong.bamboo.http.RetrofitManager
import cn.changjiahong.bamboo.koin.BaseModule
import okhttp3.OkHttpClient
import org.apache.http.params.HttpParams
import org.koin.ksp.generated.module
import retrofit2.Retrofit


/**
 * module 自动注册 初始化入口
 * @author ChangJiahong
 * @date 2023/3/7
 */
class BambooInit : Initializer<Unit> {
    override fun create(context: Context) {
        // 注册 koin
        Bamboo.registerModuleKoin("Bamboo",BaseModule().module)


    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}