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
    override abstract fun key(): K

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

    public class Occupied<K, V> internal constructor(
        public val entry: OccupiedEntry<K, V>,
    ) : Entry<K, V>() {
        override fun index(): Int = entry.index()
        override fun key(): K = entry.key()
        override fun replaceKey(newKey: K): K = entry.replaceKey(newKey)
        override fun toString(): String = "Entry($entry)"
    }

    public class Vacant<K, V> internal constructor(
        public val entry: VacantEntry<K, V>,
    ) : Entry<K, V>() {
        override fun index(): Int = entry.index()
        override fun key(): K = entry.key()
        override fun replaceKey(newKey: K): K = entry.replaceKey(newKey)
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

    private fun pair(): Pair<K, V> =
        map.getIndex(index()) ?: error("indexed entry no longer exists")

    override fun toString(): String = "IndexedEntry(index=$entryIndex, key=${key()}, value=${get()})"
}
