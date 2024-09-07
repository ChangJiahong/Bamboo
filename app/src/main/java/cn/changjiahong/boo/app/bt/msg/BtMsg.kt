package cn.changjiahong.boo.app.bt.msg


/**
 *
 * @author ChangJiahong
 * @date 2024/1/1
 */

abstract class BtMsg(val type: String)

class DefaultBtMsg(val data: String, type: String = BtMsgType.DEFAULT) : BtMsg(type) {}

data class DeviceInfoBtMsg(val deviceName: String) : BtMsg(BtMsgType.DEVICE_INFO)
