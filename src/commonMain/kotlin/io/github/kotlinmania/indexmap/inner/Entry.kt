@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source inner/entry.rs

package io.github.kotlinmania.indexmap.inner

import io.github.kotlinmania.indexmap.Bucket
import io.github.kotlinmania.indexmap.HashValue
import kotlin.native.HiddenFromObjC

/**
 * A view into an occupied entry in an IndexMap.
 */
@HiddenFromObjC
internal class OccupiedEntry<K, V> internal constructor(
    internal val map: Core<K, V>,
    internal var entryIndex: Int,
    internal var bucket: Int,
) {
    public companion object {
        public fun <K, V> fromHash(
            map: Core<K, V>,
            hash: HashValue,
            isMatch: (K) -> Boolean,
        ): OccupiedEntry<K, V>? {
            val i = map.getIndexOfRaw(hash, isMatch) ?: return null
            return OccupiedEntry(map, i, i)
        }
    }

    public fun intoCore(): Core<K, V> = map

    internal fun getBucket(): Bucket<K, V> = map.entries[entryIndex]

    internal fun getBucketMut(): Bucket<K, V> = map.entries[entryIndex]

    internal fun intoBucket(): Bucket<K, V> = map.entries[entryIndex]

    public fun index(): Int = entryIndex

    public fun key(): K = getBucket().key

    public fun get(): V = getBucket().value

    public fun getMut(): V = getBucket().value

    public fun intoMut(): V = getBucket().value

    public fun insert(value: V): V {
        val old = getBucket().value
        getBucketMut().value = value
        return old
    }

    public fun remove(): V = swapRemove()

    public fun swapRemove(): V = swapRemoveEntry().second

    public fun shiftRemove(): V = shiftRemoveEntry().second

    public fun removeEntry(): Pair<K, V> = swapRemoveEntry()

    public fun swapRemoveEntry(): Pair<K, V> {
        removeIndex()
        return map.swapRemoveFinish(entryIndex)
    }

    public fun shiftRemoveEntry(): Pair<K, V> {
        removeIndex()
        return map.shiftRemoveFinish(entryIndex)
    }

    internal fun removeIndex() {
        eraseIndex(map.indices, map.entries[entryIndex].hash, entryIndex)
    }

    public fun moveIndex(to: Int) {
        if (entryIndex != to) {
            require(to in map.entries.indices)
            map.moveIndexInner(entryIndex, to)
            updateIndex(to)
        }
    }

    public fun swapIndices(other: Int) {
        if (entryIndex != other) {
            require(other in map.entries.indices)
            val hashOther = map.entries[other].hash
            updateIndex(map.indices, hashOther, other, entryIndex)
            val temp = map.entries[entryIndex]
            map.entries[entryIndex] = map.entries[other]
            map.entries[other] = temp
            updateIndex(other)
        }
    }

    internal fun updateIndex(to: Int) {
        updateIndex(map.indices, map.entries[to].hash, entryIndex, to)
        entryIndex = to
        bucket = to
    }
}

/**
 * A view into a vacant entry in an IndexMap.
 */
@HiddenFromObjC
internal class VacantEntry<K, V> internal constructor(
    internal val map: Core<K, V>,
    internal val hash: HashValue,
    internal var key: K,
) {
    public fun index(): Int = map.len()

    public fun key(): K = key

    public fun keyMut(): K = key

    public fun intoKey(): K = key

    public fun insert(value: V): V {
        map.insertUnique(hash, key, value)
        return value
    }

    public fun insertEntry(value: V): OccupiedEntry<K, V> {
        val index = map.len()
        map.pushEntry(hash, key, value)
        map.indices.insertUnique(hash.get(), index)
        return OccupiedEntry(map, index, index)
    }

    public fun insertSorted(value: V): Pair<Int, V> =
        insertSortedBy(value) { k1, _, k2, _ ->
            if (k1 is Comparable<*> && k2 is Comparable<*>) {
                @Suppress("UNCHECKED_CAST")
                (k1 as Comparable<Any>).compareTo(k2 as Any)
            } else {
                0
            }
        }

    public fun insertSortedBy(value: V, cmp: (K, V, K, V) -> Int): Pair<Int, V> {
        var idx = 0
        while (idx < map.entries.size) {
            val entry = map.entries[idx]
            if (cmp(entry.key, entry.value, key, value) > 0) {
                break
            }
            idx += 1
        }
        shiftInsert(idx, value)
        return idx to value
    }

    public fun <B : Comparable<B>> insertSortedByKey(value: V, sortKey: (K, V) -> B): Pair<Int, V> {
        val target = sortKey(key, value)
        var idx = 0
        while (idx < map.entries.size) {
            val entry = map.entries[idx]
            if (sortKey(entry.key, entry.value) > target) {
                break
            }
            idx += 1
        }
        shiftInsert(idx, value)
        return idx to value
    }

    public fun shiftInsert(index: Int, value: V): V {
        map.shiftInsertUnique(index, hash, key, value)
        return value
    }

    public fun replaceIndex(index: Int): Pair<K, OccupiedEntry<K, V>> {
        require(index in map.entries.indices)
        val oldKey = map.replaceIndexUnique(index, hash, key)
        return oldKey to OccupiedEntry(map, index, index)
    }
}
