package cn.changjiahong.bamboo.common.event_bus

/**
 *
 * @author ChangJiahong
 * @date 2022/7/17
 */
data class Event(val type: EventType, val data: Any)

interface EventType

enum class MsgEvent : EventType {
    UNREAD_COUNT
}
