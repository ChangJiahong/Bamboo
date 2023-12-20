package cn.changjiahong.bamboo.base.utils

/**
 *
 * @author ChangJiahong
 * @date 2023/3/7
 */

fun <T, Q, F> List<T>.descartes(data: List<Q>, transform: (T, Q) -> F?): List<F> {
    val list = mutableListOf<F>()
    forEach { t ->
        data.forEach { q ->
            transform(t, q)?.run {
                list.add(this)
            }
        }
    }
    return list.toList()
}

fun <T, Q, F> List<T>.descartesIndex(
    data: List<Q>,
    transform: (Pair<T, Int>, Pair<Q, Int>) -> F?
): List<F> {
    val list = mutableListOf<F>()
    forEachIndexed { indexT, t ->
        data.forEachIndexed { indexQ, q ->
            transform(Pair(t, indexT), Pair(q, indexQ))?.run {
                list.add(this)
            }
        }
    }
    return list.toList()
}

fun <T, F> List<T>.transform(transform: (T) -> F?): List<F> {
    val list = mutableListOf<F>()
    forEach {
        transform(it)?.run {
            list.add(this)
        }
    }
    return list.toList()
}

fun <T, F> List<T>.transformIndex(transform: (T, Int) -> F?): List<F> {
    val list = mutableListOf<F>()
    forEachIndexed { index, it ->
        transform(it, index)?.run {
            list.add(this)
        }
    }
    return list.toList()
}
