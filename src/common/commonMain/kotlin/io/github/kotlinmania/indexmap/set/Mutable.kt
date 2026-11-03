@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source set/mutable.rs

package io.github.kotlinmania.indexmap.set

import io.github.kotlinmania.indexmap.IndexSet
import kotlin.native.HiddenFromObjC

/**
 * Opt-in mutable access to [IndexSet] values.
 *
 * These methods expose mutable references to the value as it is stored
 * in the set.
 * You are allowed to modify the values in the set if the modification
 * does not change the value's hash and equality.
 *
 * If values are modified erroneously, you can no longer look them up.
 * This is sound (memory safe) but a logical error hazard.
 */
@HiddenFromObjC
public interface MutableValues<T> {
    public interface Value

    public interface Sealed

    /**
     * Return item index and mutable reference to the value.
     *
     * Computes in average O(1) time.
     */
    public fun getFullMut2(value: T): Pair<Int, T>?

    /**
     * Return mutable reference to the value at an index.
     *
     * Valid indices are `0 <= index < len()`.
     *
     * Computes in O(1) time.
     */
    public fun getIndexMut2(index: Int): T?

    /**
     * Scan through each value in the set and keep those where the
     * closure [keep] returns `true`.
     *
     * The values are visited in order, and remaining values keep their order.
     *
     * Computes in average O(n) time.
     */
    public fun retain2(keep: (T) -> Boolean)
}

/**
 * Opt-in mutable access to [IndexSet] values.
 *
 * See [MutableValues] for more information.
 */
public fun <T> IndexSet<T>.getFullMut2(value: T): Pair<Int, T>? =
    map.getFullMut2(value)?.let { it.first to it.second }

/**
 * Return mutable reference to the value at an index.
 */
public fun <T> IndexSet<T>.getIndexMut2(index: Int): T? =
    map.getIndexMut2(index)?.first

/**
 * Scan through each value in the set and keep those where the closure returns true.
 */
public fun <T> IndexSet<T>.retain2(keep: (T) -> Boolean) {
    map.retain2 { key, _ -> keep(key) }
}
