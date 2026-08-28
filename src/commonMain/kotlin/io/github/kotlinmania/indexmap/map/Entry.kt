@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source map/entry.rs

package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.IndexMap
import kotlin.native.HiddenFromObjC

// Entry for an existing key-value pair in an IndexMap, or for a vacant
// insertion position.
@HiddenFromObjC
public sealed class Entry<K, V> : MutableEntryKey<K> {
    public abstract fun index(): Int

    abstract override fun key(): K

    override fun keyMut(): K = key()

    public fun insertEntry(value: V): OccupiedEntry<K, V> =
        when (this) {
            is Occupied -> {
                entry.insert(value)
                entry
            }
            is Vacant -> entry.insertEntry(value)
        }

    public fun orInsert(default: V): V =
        when (this) {
            is Occupied -> entry.get()
            is Vacant -> entry.insert(default)
        }

    public fun orDefault(default: V): V = orInsert(default)

    public fun orInsertWith(create: () -> V): V =
        when (this) {
            is Occupied -> entry.get()
            is Vacant -> entry.insert(create())
        }

    public fun orInsertWithKey(create: (K) -> V): V =
        when (this) {
            is Occupied -> entry.get()
            is Vacant -> entry.insert(create(entry.key()))
        }

    public fun andModify(modify: (V) -> V): Entry<K, V> {
        if (this is Occupied) {
            entry.insert(modify(entry.get()))
        }
        return this
    }

    public open fun fmt(): String = toString()

    public class Occupied<K, V> internal constructor(
        public val entry: OccupiedEntry<K, V>,
    ) : Entry<K, V>() {
        override fun index(): Int = entry.index()

        override fun key(): K = entry.key()

        override fun replaceKey(newKey: K): K = entry.replaceKey(newKey)

        override fun fmt(): String = toString()

        override fun toString(): String = "Entry($entry)"
    }

    public class Vacant<K, V> internal constructor(
        public val entry: VacantEntry<K, V>,
    ) : Entry<K, V>() {
        override fun index(): Int = entry.index()

        override fun key(): K = entry.key()

        override fun replaceKey(newKey: K): K = entry.replaceKey(newKey)

        override fun fmt(): String = toString()

        override fun toString(): String = "Entry($entry)"
    }
}

// A view into an occupied entry in an IndexMap.
@HiddenFromObjC
public class OccupiedEntry<K, V> internal constructor(
    private val map: IndexMap<K, V>,
    private var entryIndex: Int,
) : MutableEntryKey<K> {
    public fun index(): Int = entryIndex

    override fun key(): K = pair().first
 
    override fun keyMut(): K = key()

    public fun get(): V = pair().second

    public fun getMut(): V = get()

    public fun insert(value: V): V {
        val old = get()
        map.insert(key(), value)
        return old
    }

    public fun intoMut(): V = get()

    public fun swapRemoveEntry(): Pair<K, V> =
        map.swapRemoveIndex(index()) ?: error("occupied entry no longer exists")

    public fun shiftRemoveEntry(): Pair<K, V> =
        map.shiftRemoveIndex(index()) ?: error("occupied entry no longer exists")

    public fun swapRemove(): V = swapRemoveEntry().second

    public fun shiftRemove(): V = shiftRemoveEntry().second

    public fun moveIndex(to: Int) {
        map.moveIndex(index(), to)
        entryIndex = to
    }

    public fun swapIndices(other: Int) {
        map.swapIndices(index(), other)
        entryIndex = other
    }

    override fun replaceKey(newKey: K): K =
        map.replaceIndex(index(), newKey)

    public fun intoCore(): IndexMap<K, V> = map

    public fun fmt(): String = toString()

    private fun pair(): Pair<K, V> =
        map.getIndex(index()) ?: error("occupied entry no longer exists")

    override fun toString(): String = "OccupiedEntry(key=${key()}, value=${get()})"
}

// A view into a vacant insertion position in an IndexMap.
@HiddenFromObjC
public class VacantEntry<K, V> internal constructor(
    private val map: IndexMap<K, V>,
    private var entryKey: K,
    private val insertionIndex: Int,
) : MutableEntryKey<K> {
    public fun index(): Int = insertionIndex

    override fun key(): K = entryKey
 
    override fun keyMut(): K = key()

    public fun insert(value: V): V {
        insertEntry(value)
        return value
    }

    public fun insertEntry(value: V): OccupiedEntry<K, V> {
        val (index, _) = map.insertBefore(insertionIndex.coerceAtMost(map.len()), entryKey, value)
        return OccupiedEntry(map, index)
    }

    override fun replaceKey(newKey: K): K {
        val old = entryKey
        entryKey = newKey
        return old
    }

    public fun intoCore(): IndexMap<K, V> = map

    public fun insertSortedBy(value: V, cmp: (K, V, K, V) -> Int): Pair<Int, V> {
        val (index, _) = map.insertSortedBy(entryKey, value, cmp)
        return index to value
    }

    public fun <B : Comparable<B>> insertSortedByKey(value: V, sortKey: (K, V) -> B): Pair<Int, V> {
        val (index, _) = map.insertSortedByKey(entryKey, value, sortKey)
        return index to value
    }

    public fun fmt(): String = toString()

    override fun toString(): String = "VacantEntry($entryKey)"
}

// A view into an occupied entry obtained by index.
@HiddenFromObjC
public class IndexedEntry<K, V> internal constructor(
    private val map: IndexMap<K, V>,
    private var entryIndex: Int,
) : MutableEntryKey<K> {
    public fun index(): Int = entryIndex

    override fun key(): K = pair().first
 
    override fun keyMut(): K = key()

    public fun get(): V = pair().second

    public fun getMut(): V = get()

    public fun insert(value: V): V {
        val old = get()
        map.insert(key(), value)
        return old
    }

    public fun intoMut(): V = get()

    public fun swapRemoveEntry(): Pair<K, V> =
        map.swapRemoveIndex(index()) ?: error("indexed entry no longer exists")

    public fun shiftRemoveEntry(): Pair<K, V> =
        map.shiftRemoveIndex(index()) ?: error("indexed entry no longer exists")

    public fun swapRemove(): V = swapRemoveEntry().second

    public fun shiftRemove(): V = shiftRemoveEntry().second

    public fun moveIndex(to: Int) {
        map.moveIndex(index(), to)
        entryIndex = to
    }

    public fun swapIndices(other: Int) {
        map.swapIndices(index(), other)
        entryIndex = other
    }

    override fun replaceKey(newKey: K): K =
        map.replaceIndex(index(), newKey)

    public fun intoCore(): IndexMap<K, V> = map

    public fun getBucket(): Pair<K, V> = pair()

    public fun getBucketMut(): Pair<K, V> = pair()

    public fun intoBucket(): Pair<K, V> = pair()

    public fun fmt(): String = toString()

    private fun pair(): Pair<K, V> =
        map.getIndex(index()) ?: error("indexed entry no longer exists")

    override fun toString(): String = "IndexedEntry(index=$entryIndex, key=${key()}, value=${get()})"

    public companion object {
        public fun <K, V> new(map: IndexMap<K, V>, index: Int): IndexedEntry<K, V>? =
            if (index in 0 until map.len()) IndexedEntry(map, index) else null

        public fun <K, V> from(other: OccupiedEntry<K, V>): IndexedEntry<K, V> =
            IndexedEntry(other.intoCore(), other.index())
    }
}

public fun <K : Comparable<K>, V> VacantEntry<K, V>.insertSorted(value: V): Pair<Int, V> =
    insertSortedBy(value) { k1, _, k2, _ -> k1.compareTo(k2) }

