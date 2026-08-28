@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source map/raw_entry_v1.rs

package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.IndexMap
import kotlin.native.HiddenFromObjC

// Opt-in access to the experimental raw entry API.
@HiddenFromObjC
public interface RawEntryApiV1<K, V> {
    public interface Sealed

    public fun rawEntryV1(): RawEntryBuilder<K, V>

    public fun rawEntryMutV1(): RawEntryBuilderMut<K, V>
}

// A builder for computing where in an IndexMap a key-value pair would be stored.
@HiddenFromObjC
public class RawEntryBuilder<K, V> internal constructor(
    private val map: IndexMap<K, V>,
) {
    public companion object {
        public fun assertSendSync() {}
    }

    public fun fromKey(key: K): Pair<K, V>? = map.getKeyValue(key)

    public fun fromKeyHashedNocheck(hash: ULong, key: K): Pair<K, V>? = map.getKeyValue(key)

    public fun fromHash(hash: ULong, isMatch: (K) -> Boolean): Pair<K, V>? {
        val i = indexFromHash(hash, isMatch) ?: return null
        return map.getIndex(i)
    }

    public fun fromHashFull(hash: ULong, isMatch: (K) -> Boolean): Triple<Int, K, V>? {
        val i = indexFromHash(hash, isMatch) ?: return null
        val (k, v) = map.getIndex(i) ?: return null
        return Triple(i, k, v)
    }

    public fun indexFromHash(hash: ULong, isMatch: (K) -> Boolean): Int? {
        val entries = map.asEntries()
        for (i in entries.indices) {
            if (isMatch(entries[i].first)) {
                return i
            }
        }
        return null
    }

    public fun fmt(): String = toString()

    override fun toString(): String = "RawEntryBuilder"
}

// A builder for computing where in an IndexMap a key-value pair would be stored.
@HiddenFromObjC
public class RawEntryBuilderMut<K, V> internal constructor(
    private val map: IndexMap<K, V>,
) {
    public companion object {
        public fun assertSendSync() {}
    }

    public fun fromKey(key: K): RawEntryMut<K, V> =
        fromHash(0uL) { it == key }

    public fun fromKeyHashedNocheck(hash: ULong, key: K): RawEntryMut<K, V> =
        fromHash(hash) { it == key }

    public fun fromHash(hash: ULong, isMatch: (K) -> Boolean): RawEntryMut<K, V> {
        val i = indexFromHash(hash, isMatch)
        return if (i != null) {
            RawEntryMut.Occupied(RawOccupiedEntryMut(map, i))
        } else {
            RawEntryMut.Vacant(RawVacantEntryMut(map))
        }
    }

    public fun indexFromHash(hash: ULong, isMatch: (K) -> Boolean): Int? {
        val entries = map.asEntries()
        for (i in entries.indices) {
            if (isMatch(entries[i].first)) {
                return i
            }
        }
        return null
    }

    public fun fmt(): String = toString()

    override fun toString(): String = "RawEntryBuilderMut"
}

// A view into a single raw entry in an IndexMap, which may either be vacant or occupied.
@HiddenFromObjC
public sealed class RawEntryMut<K, V> {
    public companion object {
        public fun assertSendSync() {}
    }

    public class Occupied<K, V>(
        public val entry: RawOccupiedEntryMut<K, V>,
    ) : RawEntryMut<K, V>()

    public class Vacant<K, V>(
        public val entry: RawVacantEntryMut<K, V>,
    ) : RawEntryMut<K, V>()

    public fun orInsert(defaultKey: K, defaultValue: V): Pair<K, V> =
        when (this) {
            is Occupied -> entry.key() to entry.get()
            is Vacant -> entry.insert(defaultKey, defaultValue)
        }

    public fun orInsertWith(default: () -> Pair<K, V>): Pair<K, V> =
        when (this) {
            is Occupied -> entry.key() to entry.get()
            is Vacant -> {
                val (key, value) = default()
                entry.insert(key, value)
            }
        }

    public fun intoKeyValueMut(): Pair<K, V> =
        when (this) {
            is Occupied -> entry.intoKeyValueMut()
            is Vacant -> error("cannot convert vacant raw entry to key value")
        }

    public fun andModify(modify: (Pair<K, V>) -> Unit): RawEntryMut<K, V> {
        if (this is Occupied) {
            val key = entry.key()
            val value = entry.get()
            modify(key to value)
        }
        return this
    }

    public fun index(): Int =
        when (this) {
            is Occupied -> entry.index()
            is Vacant -> entry.index()
        }

    public fun fmt(): String = toString()

    override fun toString(): String =
        when (this) {
            is Occupied -> "RawEntryMut::Occupied($entry)"
            is Vacant -> "RawEntryMut::Vacant($entry)"
        }
}

// A view into an occupied raw entry in an IndexMap.
@HiddenFromObjC
public class RawOccupiedEntryMut<K, V> internal constructor(
    private val map: IndexMap<K, V>,
    private var entryIndex: Int,
) {
    public companion object {
        public fun assertSendSync() {}
    }

    public fun index(): Int = entryIndex

    public fun key(): K =
        map.getIndex(entryIndex)?.first ?: error("raw occupied entry no longer exists")

    public fun keyMut(): K = key()

    public fun intoKey(): K = key()

    public fun get(): V =
        map.getIndex(entryIndex)?.second ?: error("raw occupied entry no longer exists")

    public fun getMut(): V = get()

    public fun intoMut(): V = get()

    public fun getKeyValue(): Pair<K, V> = key() to get()

    public fun getKeyValueMut(): Pair<K, V> = key() to get()

    public fun intoKeyValueMut(): Pair<K, V> = key() to get()

    public fun insert(value: V): V {
        val old = get()
        val key = key()
        map.insert(key, value)
        return old
    }

    public fun insertKey(key: K): K {
        val old = key()
        map.entries[entryIndex].key = key
        return old
    }

    public fun remove(): V = shiftRemove()

    public fun swapRemove(): V = swapRemoveEntry().second

    public fun shiftRemove(): V = shiftRemoveEntry().second

    public fun removeEntry(): Pair<K, V> = shiftRemoveEntry()

    public fun swapRemoveEntry(): Pair<K, V> =
        map.swapRemoveIndex(entryIndex) ?: error("raw occupied entry no longer exists")

    public fun shiftRemoveEntry(): Pair<K, V> =
        map.shiftRemoveIndex(entryIndex) ?: error("raw occupied entry no longer exists")

    public fun moveIndex(to: Int) {
        map.moveIndex(entryIndex, to)
        entryIndex = to
    }

    public fun swapIndices(other: Int) {
        map.swapIndices(entryIndex, other)
        entryIndex = other
    }

    public fun fmt(): String = toString()

    override fun toString(): String = "RawOccupiedEntryMut(key=${key()}, value=${get()})"
}

// A view into a vacant raw entry in an IndexMap.
@HiddenFromObjC
public class RawVacantEntryMut<K, V> internal constructor(
    private val map: IndexMap<K, V>,
) {
    public companion object {
        public fun assertSendSync() {}
    }

    public fun index(): Int = map.len()

    public fun insert(key: K, value: V): Pair<K, V> {
        map.insert(key, value)
        return key to value
    }

    public fun insertHashedNocheck(hash: ULong, key: K, value: V): Pair<K, V> =
        insert(key, value)

    public fun shiftInsert(index: Int, key: K, value: V): Pair<K, V> {
        map.shiftInsert(index, key, value)
        return key to value
    }

    public fun shiftInsertHashedNocheck(index: Int, hash: ULong, key: K, value: V): Pair<K, V> =
        shiftInsert(index, key, value)

    public fun fmt(): String = toString()

    override fun toString(): String = "RawVacantEntryMut"
}
