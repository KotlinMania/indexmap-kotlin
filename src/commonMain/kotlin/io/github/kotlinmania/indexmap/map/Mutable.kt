@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source map/mutable.rs

package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.IndexMap
import kotlin.native.HiddenFromObjC

/**
 * Opt-in mutable access to [IndexMap] keys.
 *
 * These methods expose mutable references to the key as it is stored
 * in the map.
 * You are allowed to modify the keys in the map if the modification
 * does not change the key's hash and equality.
 *
 * If keys are modified erroneously, you can no longer look them up.
 * This is sound (memory safe) but a logical error hazard.
 */
@HiddenFromObjC
public interface MutableKeys<K, V> {
    /**
     * Return item index, mutable reference to key and value.
     *
     * Computes in average O(1) time.
     */
    public fun getFullMut2(key: K): Triple<Int, K, V>?

    /**
     * Return mutable reference to key and value at an index.
     *
     * Valid indices are `0 <= index < len()`.
     *
     * Computes in O(1) time.
     */
    public fun getIndexMut2(index: Int): Pair<K, V>?

    /**
     * Return an iterator over the key-value pairs of the map, in their order.
     */
    public fun iterMut2(): List<Pair<K, V>>

    /**
     * Scan through each key-value pair in the map and keep those where the
     * closure [keep] returns `true`.
     *
     * The elements are visited in order, and remaining elements keep their
     * order.
     *
     * Computes in average O(n) time.
     */
    public fun retain2(keep: (K, V) -> Boolean)
}

/**
 * Opt-in mutable access to [Entry] keys.
 *
 * These methods expose mutable references to the key as it is stored
 * in the map.
 * You are allowed to modify the keys in the map if the modification
 * does not change the key's hash and equality.
 */
@HiddenFromObjC
public interface MutableEntryKey<K> {
    /**
     * Gets a mutable reference to the entry's key, either within the map if occupied,
     * or else the new key that was used to find the entry.
     */
    public fun key(): K

    public fun keyMut(): K = key()

    public fun replaceKey(newKey: K): K
}
