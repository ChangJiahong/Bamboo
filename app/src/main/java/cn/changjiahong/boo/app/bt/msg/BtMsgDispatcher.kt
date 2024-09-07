package cn.changjiahong.boo.app.bt.msg

import cn.changjiahong.bamboo.common.dispatcher.CustomDispatcher
import cn.changjiahong.bamboo.common.dispatcher.ObserverNotify
import cn.changjiahong.boo.app.bt.msg.BtMsg

/**
 *
 * @author ChangJiahong
 * @date 2024/1/1
 */
object BtMsgDispatcher : CustomDispatcher<BtMsg>() {

    init {

        BtMsgType.DEVICE_INFO - DeviceInfoBtMsg::class.java

    }


    inline fun <reified T : BtMsg> registerNotification(notification: BtMsgObserverNotify<T>) {
        registerNotification(T::class.java, notification)
    }

}

interface BtMsgObserverNotify<T : BtMsg> : ObserverNotify<T, BtMsg>