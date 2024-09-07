package cn.changjiahong.bamboo.base.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Process
import android.text.TextUtils
import cn.changjiahong.bamboo.BuildConfig
import cn.changjiahong.bamboo.base.lan.LanguageManager
import cn.changjiahong.bamboo.base.manager.ActivityStackManager
import com.alibaba.android.arouter.launcher.ARouter
import com.hzsoft.lib.log.KLog
import java.io.BufferedReader
import java.io.FileReader


/**
 *
 * @author ChangJiahong
 * @date 2023/3/7
 */
abstract class BambooApplication : Application() {
    private val TAG = "BambooApplication"

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var _context: Context? = null
        val context: Context
            get() = _context!!
    }

    override fun onCreate() {
        super.onCreate()
        _context = this

        // 注册ActivityStackManager
        registerActivityLifecycleCallbacks(ActivityStackManager.instance)

        if (isMainProcess()) {
            initOnlyMainProcess()
        }
    }


    /**
     * 主线程中初始化内容
     */
    protected open fun initOnlyMainProcess() {
        KLog.init(BuildConfig.DEBUG)

        //初始化ARouter框架
        val isDebugARouter = BuildConfig.DEBUG //ARouter调试开关
        if (isDebugARouter) {
            //下面两行必须写在init之前，否则这些配置在init中将无效
            ARouter.openLog()
            //开启调试模式（如果在InstantRun模式下运行，必须开启调试模式！
            // 线上版本需要关闭，否则有安全风险）
            ARouter.openDebug()
        }
        //官方推荐放到Application中初始化
        ARouter.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        _context = this
        super.attachBaseContext(base)
        base?.run { LanguageManager.attachBaseContext(base) }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LanguageManager.attachBaseContext(this);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    open fun getProcessName(pid: Int): String? {
        try {
            BufferedReader(FileReader("/proc/$pid/cmdline")).use { reader ->
                var processName = reader.readLine()
                if (!TextUtils.isEmpty(processName)) {
                    processName = processName.trim { it <= ' ' }
                }
                return processName
            }
        } catch (e: Exception) {
            KLog.w(TAG, "获取当前进程名称", e)
        }
        return null
    }

    open fun isMainProcess(): Boolean {
        // 获取当前进程名
        val processName = getProcessName(Process.myPid())
        return processName == null || processName.isEmpty() || processName == applicationContext.packageName
    }

    private fun Map<String, Application>.execute(block: Application.() -> Unit) {
        forEach { (_, app) ->
            app.apply { block }
        }
    }
}
