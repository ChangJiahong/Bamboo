package cn.changjiahong.bamboo.http

import cn.changjiahong.bamboo.base.lan.LanguageManager
import cn.changjiahong.bamboo.base.utils.DeviceTools
import cn.changjiahong.bamboo.common.StringPairs
import java.util.*

/**
 * 公共参数
 * @author ChangJiahong
 * @date 2022/6/29
 */
object PublicHttpParams {
    const val VERSION_CODE = "version_code"
    const val VERSION_NAME = "version_name"
    const val PLATFORM = "platform"
    const val PLATFORM_INFO = "platform_info"
    const val OS_VERSION = "os_version"
    const val CHANNEL = "channel"
    const val APP_NAME = "app_name"
    const val SYSTEM_LANG = "system_lang"
    const val SYSTEM_COUNTRY = "system_country"
    const val TIMESTAMP = "timestamp"
    const val DEVICE_ID = "device_id"
    const val BUVID = "buvid"
    const val SIGN = "sign"
    const val UID = "uid"
    const val TOKEN = "token"
    const val SIGN_TYPE = "sign_type" // 1用户私钥,0客户端私钥


    private const val SET_BUVID = "buvid"

    const val SET_NAME_APP = "app_setting"
    const val SET_NAME_LOGIN = "login_info"

    const val SET_ENTER_MAIN_CONFIG = "enter_main_config"

    fun getStaticParams() = StringPairs<String>().apply {
        VERSION_CODE - DeviceTools.getVersionCode().toString()
        VERSION_NAME - DeviceTools.getVersionName()
        PLATFORM - "Android"
        PLATFORM_INFO - DeviceTools.getModel()
        OS_VERSION - DeviceTools.getSysRelease()
//        CHANNEL - DeviceTools.getChannel().toString()
        APP_NAME - DeviceTools.getAppName()
        SYSTEM_LANG - LanguageManager.getCurrentLan().toLanguageTag()
        SYSTEM_COUNTRY - LanguageManager.getSystemLan().country
        DEVICE_ID - DeviceTools.getDeviceId()
    }.pairs

    fun getDynamicParams() = StringPairs<String>().apply {
        TIMESTAMP - (Calendar.getInstance().time.time / 1000).toString()
//        val appSPSetting = SPSetting(SET_NAME_APP)
//        val loginInfo = SPSetting(SET_NAME_LOGIN)
//        val buvidJson = appSPSetting.getString(SET_BUVID, "")
//        if (buvidJson.isNotEmpty()) {
//            val buvid = Gson().fromJson(buvidJson, Buvid::class.java)
//            BUVID - buvid.buvid
//        }
//        val uInfo = loginInfo.get<UInfo>("login_info")
//        if (uInfo != null) {
//            TOKEN - uInfo.token
//            UID - uInfo.uid
//        }

    }.pairs

}