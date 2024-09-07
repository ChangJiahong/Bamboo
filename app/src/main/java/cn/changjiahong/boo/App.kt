package cn.changjiahong.boo

import android.content.Intent
import cn.changjiahong.bamboo.base.app.Bamboo
import cn.changjiahong.bamboo.base.app.BambooApplication
import cn.changjiahong.bamboo.base.bluetooth.BTDevicesManager
import cn.changjiahong.bamboo.base.router.startService
import cn.changjiahong.bamboo.base.utils.moshi.KotlinJsonAdapterFactory
import cn.changjiahong.bamboo.http.*
import cn.changjiahong.boo.app.bt.BtService
import com.hzsoft.lib.log.KLog
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 *
 * @author ChangJiahong
 * @date 2023/3/7
 */
class App : BambooApplication() {

    override fun onCreate() {
        Bamboo.registerModuleKoin("App",AppKoin().module)
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)

            modules(Bamboo.koinModules)

            KLog.d("Application", "依赖注入加载")
            Bamboo.koins.forEach { (key, value) ->
                KLog.d("Application", "$key -- $value")
            }
            KLog.d("Application", "依赖注入加载")
        }

//        startService(Intent(this,BtService::class.java))
        startService<BtService>()
    }


    override fun initOnlyMainProcess() {
        super.initOnlyMainProcess()

        // 注册http
        /*
         http 初始化
          */
        httpDefaultInit(UApi)

        BTDevicesManager.registerReceiver(this)
    }
}