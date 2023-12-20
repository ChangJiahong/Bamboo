package cn.changjiahong.bamboo.base.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 *
 * @author ChangJiahong
 * @date 2023/3/12
 */

public fun <T> Flow<T>.collectIn(
    scope: CoroutineScope, collector: FlowCollector<T>
) = scope.launch {
    collect(collector)
}

/**
 * 一定有数据，传入默认保底数据
 */
public fun <T> Flow<T>.mustCollectIn(
    scope: CoroutineScope, default: T, collector: FlowCollector<T>
) = scope.launch {
    var empty = true
    collect {
        empty = false
        collector.emit(it)
    }
    if (empty) {
        collector.emit(default)
    }
}
