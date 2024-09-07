package cn.changjiahong.boo.app.bt.msg

import cn.changjiahong.bamboo.common.event_bus.EventType

/**
 *
 * @author ChangJiahong
 * @date 2024/1/1
 */
enum class BtEvent : EventType {
    // 连接成功
    CONNECT_SUCCESS,
    // 连接中
    CONNECTING,
    //
    DIS_CONNECT
}