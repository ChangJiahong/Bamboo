package cn.changjiahong.bamboo.base.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import cn.changjiahong.bamboo.common.event_bus.Event
import com.hzsoft.lib.log.KLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.reflect.KClass


/**
 *
 * @author ChangJiahong
 * @date 2023/12/31
 */
abstract class BaseService : Service(), IService {


    private val serviceJob = SupervisorJob() + Dispatchers.Main.immediate
    val serviceScope = CoroutineScope(serviceJob)


    override fun onCreate() {
        super.onCreate()
        KLog.d("BaseService", "${this::class.java.canonicalName} onCreate")
        BamServiceManager.registerService(this)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        KLog.d("BaseService", "${this::class.java.canonicalName} onDestroy")
        BamServiceManager.unRegisterService(this)
        EventBus.getDefault().unregister(this)
        serviceJob.cancel()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMainEventBus(event: Event) {

    }

    fun postEvent(event: Event) {
        EventBus.getDefault().post(event)
    }

    /**
     * 判断服务是否在运行
     * @return
     * 服务名称为全路径 例如com.ghost.WidgetUpdateService
     */
    open fun isRunService(service: Service): Boolean {
        val manager = baseContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (ser in manager.getRunningServices(Int.MAX_VALUE)) {
            if (service::class.java.canonicalName == ser.service.className) {
                return true
            }
        }
        return false
    }
}