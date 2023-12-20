package cn.changjiahong.bamboo.base.utils

import android.content.pm.PackageManager
import android.os.Build
import cn.changjiahong.bamboo.base.app.BambooApplication

/**
 *
 * @author ChangJiahong
 * @date 2022/6/29
 */
object DeviceTools {
    fun getVersionCode(): Int {
        var ver = 0
        try {
            ver = BambooApplication.context.packageManager.getPackageInfo(
                BambooApplication.context.packageName, 0
            ).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ver
    }

    fun getVersionName(): String {
        var ver = ""
        try {
            ver = BambooApplication.context.packageManager.getPackageInfo(
                BambooApplication.context.packageName, 0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ver
    }

    fun getChannel(): Int {
        var channel = 0
        val context = BambooApplication.context
        try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            val bundle = ai.metaData
            // 初始化登录信息
            channel = bundle.getInt("TD_CHANNEL_ID")
            if (channel == 0) {
                val channelStr = bundle.getString("TD_CHANNEL_ID")
                if (channelStr != null) {
                    channel = channelStr.toInt()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return channel
    }

    /**
     * 获取手机型号
     *
     * @return String 手机型号
     */
    fun getModel(): String {
        return Build.MODEL ?: "UNKNOWN"
    }

    /**
     * 获取操作系统的版本号
     *
     * @return String 系统版本号
     */
    fun getSysRelease(): String {
        return Build.VERSION.RELEASE ?: "UNKNOWN"
    }

    fun getAppName(): String {
        var appName = ""
        try {
            val context = BambooApplication.context
            appName =
                BambooApplication.context.packageManager.getApplicationLabel(context.applicationInfo)
                    .toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return appName
    }

    /**
     * 设备id 唯一性
     */
    fun getDeviceId(): String{
        var device_id = ""

        return device_id
    }
}