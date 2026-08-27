@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source inner/extract.rs

package io.github.kotlinmania.indexmap.inner

import io.github.kotlinmania.indexmap.Bucket
import kotlin.native.HiddenFromObjC

internal fun <K, V> Core<K, V>.extract(range: IntRange): ExtractCore<K, V> {
    val start = range.first.coerceAtLeast(0)
    val end = (range.last + 1).coerceAtMost(len())
    return ExtractCore(this, start, start, end)
}

/**
 * An extraction iterator over a range of Core entries.
 */
@HiddenFromObjC
internal class ExtractCore<K, V> internal constructor(
    internal val map: Core<K, V>,
    internal var newLen: Int,
    internal var current: Int,
    internal var end: Int,
) {
    public fun extractIf(pred: (Bucket<K, V>) -> Boolean): Bucket<K, V>? {
        while (current < end) {
            val entry = map.entries[current]
            if (pred(entry)) {
                current += 1
                val extracted = map.entries.removeAt(current - 1)
                end -= 1
                map.rebuildHashTable()
                return extracted
            } else {
                current += 1
                newLen += 1
            }
        }
        return null
    }

    public fun remaining(): Int = (end - current).coerceAtLeast(0)
}
