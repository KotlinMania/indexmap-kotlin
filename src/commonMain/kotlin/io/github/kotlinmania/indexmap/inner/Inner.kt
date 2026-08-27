@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source inner.rs

package io.github.kotlinmania.indexmap.inner

import io.github.kotlinmania.indexmap.Bucket
import io.github.kotlinmania.indexmap.HashValue
import io.github.kotlinmania.indexmap.TryReserveError
import kotlin.native.HiddenFromObjC

internal class Indices(
    internal val items: MutableList<Int> = mutableListOf(),
) {
    public fun len(): Int = items.size

    public fun isEmpty(): Boolean = items.isEmpty()

    public fun capacity(): Int = items.size

    public fun clear() {
        items.clear()
    }

    public fun clone(): Indices = Indices(items.toMutableList())

    public fun cloneFrom(other: Indices) {
        items.clear()
        items.addAll(other.items)
    }

    public fun find(hash: ULong, isMatch: (Int) -> Boolean): Int? {
        for (idx in items) {
            if (isMatch(idx)) {
                return idx
            }
        }
        return null
    }

    public fun findEntry(hash: ULong, isMatch: (Int) -> Boolean): Int? =
        find(hash, isMatch)

    public fun findMut(hash: ULong, isMatch: (Int) -> Boolean): Int? =
        find(hash, isMatch)

    public fun findBucketIndex(hash: ULong, isMatch: (Int) -> Boolean): Int? {
        for (i in items.indices) {
            if (isMatch(items[i])) {
                return i
            }
        }
        return null
    }

    public fun insertUnique(hash: ULong, index: Int) {
        items.add(index)
    }

    public fun removeIndex(index: Int) {
        val i = items.indexOf(index)
        if (i >= 0) {
            items.removeAt(i)
        }
    }
}

internal fun <K, V> getHash(entries: List<Bucket<K, V>>): (Int) -> ULong =
    { i -> entries[i].hash.get() }

internal fun <K, V> equivalent(key: K, entries: List<Bucket<K, V>>): (Int) -> Boolean =
    { i -> entries[i].key == key }

internal fun eraseIndex(indices: Indices, hash: HashValue, index: Int) {
    indices.removeIndex(index)
}

internal fun updateIndex(indices: Indices, hash: HashValue, old: Int, new: Int) {
    val i = indices.items.indexOf(old)
    if (i >= 0) {
        indices.items[i] = new
    }
}

internal fun <K, V> insertBulkNoGrow(indices: Indices, entries: List<Bucket<K, V>>) {
    val base = indices.len()
    for (i in entries.indices) {
        indices.insertUnique(entries[i].hash.get(), base + i)
    }
}

/**
 * Core of the map that does not depend on S.
 */
@HiddenFromObjC
internal class Core<K, V> internal constructor(
    internal val indices: Indices,
    internal val entries: MutableList<Bucket<K, V>>,
) {
    public constructor() : this(Indices(), mutableListOf())

    public companion object {
        public fun <K, V> new(): Core<K, V> = Core()

        public fun <K, V> withCapacity(n: Int): Core<K, V> =
            Core(Indices(ArrayList(n)), ArrayList(n))

        public fun assertSendSync() {}
    }

    public fun intoEntries(): List<Bucket<K, V>> = entries

    public fun asEntries(): List<Bucket<K, V>> = entries

    public fun asEntriesMut(): MutableList<Bucket<K, V>> = entries

    public fun withEntries(f: (MutableList<Bucket<K, V>>) -> Unit) {
        f(entries)
        rebuildHashTable()
    }

    public fun len(): Int = entries.size

    public fun capacity(): Int = entries.size

    public fun clear() {
        indices.clear()
        entries.clear()
    }

    public fun truncate(len: Int) {
        if (len < len()) {
            eraseIndices(len, entries.size)
            while (entries.size > len) {
                entries.removeAt(entries.size - 1)
            }
        }
    }

    public fun drain(range: IntRange): List<Bucket<K, V>> {
        val start = range.first.coerceAtLeast(0)
        val end = (range.last + 1).coerceAtMost(entries.size)
        if (start >= end) return emptyList()
        eraseIndices(start, end)
        val drained = mutableListOf<Bucket<K, V>>()
        for (i in (end - 1) downTo start) {
            drained.add(0, entries.removeAt(i))
        }
        return drained
    }

    public fun splitOff(at: Int): Core<K, V> {
        val len = entries.size
        require(at in 0..len) { "index out of bounds: the len is $len but the index is $at." }
        eraseIndices(at, entries.size)
        val otherEntries = mutableListOf<Bucket<K, V>>()
        while (entries.size > at) {
            otherEntries.add(entries.removeAt(at))
        }
        val otherIndices = Indices()
        insertBulkNoGrow(otherIndices, otherEntries)
        return Core(otherIndices, otherEntries)
    }

    public fun splitSplice(range: IntRange): Pair<Core<K, V>, List<Bucket<K, V>>> {
        val start = range.first.coerceAtLeast(0)
        val end = (range.last + 1).coerceAtMost(entries.size)
        eraseIndices(start, entries.size)
        val tailEntries = mutableListOf<Bucket<K, V>>()
        while (entries.size > end) {
            tailEntries.add(entries.removeAt(end))
        }
        val drained = mutableListOf<Bucket<K, V>>()
        while (entries.size > start) {
            drained.add(entries.removeAt(start))
        }
        val tailIndices = Indices()
        insertBulkNoGrow(tailIndices, tailEntries)
        return Core(tailIndices, tailEntries) to drained
    }

    public fun appendUnchecked(other: Core<K, V>) {
        insertBulkNoGrow(indices, other.entries)
        entries.addAll(other.entries)
        other.indices.clear()
        other.entries.clear()
    }

    public fun reserve(additional: Int) {}

    public fun reserveExact(additional: Int) {}

    public fun tryReserve(additional: Int): Result<Unit> = Result.success(Unit)

    public fun tryReserveEntries(additional: Int): Result<Unit> = Result.success(Unit)

    public fun tryReserveExact(additional: Int): Result<Unit> = Result.success(Unit)

    public fun shrinkTo(minCapacity: Int) {}

    public fun pop(): Pair<K, V>? {
        if (entries.isEmpty()) return null
        val lastIndex = entries.size - 1
        val entry = entries.removeAt(lastIndex)
        eraseIndex(indices, entry.hash, lastIndex)
        return entry.key to entry.value
    }

    public fun getIndexOf(hash: HashValue, key: K): Int? {
        for (i in entries.indices) {
            val entry = entries[i]
            if (entry.hash == hash && entry.key == key) {
                return i
            }
        }
        return null
    }

    public fun getIndexOfRaw(hash: HashValue, isMatch: (K) -> Boolean): Int? {
        for (i in entries.indices) {
            val entry = entries[i]
            if (entry.hash == hash && isMatch(entry.key)) {
                return i
            }
        }
        return null
    }

    internal fun pushEntry(hash: HashValue, key: K, value: V) {
        entries.add(Bucket(hash, key, value))
    }

    public fun insertFull(hash: HashValue, key: K, value: V): Pair<Int, V?> {
        val existingIndex = getIndexOf(hash, key)
        if (existingIndex != null) {
            val old = entries[existingIndex].value
            entries[existingIndex].value = value
            return existingIndex to old
        }
        val i = entries.size
        indices.insertUnique(hash.get(), i)
        pushEntry(hash, key, value)
        return i to null
    }

    public fun replaceFull(hash: HashValue, key: K, value: V): Pair<Int, Pair<K, V>?> {
        val existingIndex = getIndexOf(hash, key)
        if (existingIndex != null) {
            val oldEntry = entries[existingIndex]
            val oldPair = oldEntry.key to oldEntry.value
            oldEntry.key = key
            oldEntry.value = value
            return existingIndex to oldPair
        }
        val i = entries.size
        indices.insertUnique(hash.get(), i)
        pushEntry(hash, key, value)
        return i to null
    }

    public fun shiftRemoveFull(hash: HashValue, key: K): Triple<Int, K, V>? {
        val index = getIndexOf(hash, key) ?: return null
        eraseIndex(indices, hash, index)
        val (k, v) = shiftRemoveFinish(index)
        return Triple(index, k, v)
    }

    public fun swapRemoveFull(hash: HashValue, key: K): Triple<Int, K, V>? {
        val index = getIndexOf(hash, key) ?: return null
        eraseIndex(indices, hash, index)
        val (k, v) = swapRemoveFinish(index)
        return Triple(index, k, v)
    }

    internal fun eraseIndices(start: Int, end: Int) {
        if (start >= end) return
        for (i in start until end) {
            if (i < entries.size) {
                eraseIndex(indices, entries[i].hash, i)
            }
        }
        decrementIndices(end, entries.size)
    }

    public fun retainInOrder(keep: (K, V) -> Boolean) {
        val oldLen = entries.size
        var i = 0
        while (i < entries.size) {
            val entry = entries[i]
            if (!keep(entry.key, entry.value)) {
                entries.removeAt(i)
            } else {
                i += 1
            }
        }
        if (entries.size < oldLen) {
            rebuildHashTable()
        }
    }

    public fun rebuildHashTable() {
        indices.clear()
        insertBulkNoGrow(indices, entries)
    }

    public fun reverse() {
        entries.reverse()
        val len = entries.size
        for (i in indices.items.indices) {
            indices.items[i] = len - indices.items[i] - 1
        }
    }

    internal fun reserveEntries(additional: Int) {}

    public fun insertUnique(hash: HashValue, key: K, value: V): Bucket<K, V> {
        val i = entries.size
        indices.insertUnique(hash.get(), i)
        val bucket = Bucket(hash, key, value)
        entries.add(bucket)
        return bucket
    }

    public fun replaceIndexUnique(index: Int, hash: HashValue, key: K): K {
        eraseIndex(indices, entries[index].hash, index)
        indices.insertUnique(hash.get(), index)
        val entry = entries[index]
        entry.hash = hash
        val oldKey = entry.key
        entry.key = key
        return oldKey
    }

    public fun shiftInsertUnique(
        index: Int,
        hash: HashValue,
        key: K,
        value: V,
    ): Bucket<K, V> {
        val end = entries.size
        require(index in 0..end)
        incrementIndices(index, end)
        indices.insertUnique(hash.get(), index)
        val bucket = Bucket(hash, key, value)
        entries.add(index, bucket)
        return bucket
    }

    public fun shiftRemoveIndex(index: Int): Pair<K, V>? {
        if (index !in entries.indices) return null
        val entry = entries[index]
        eraseIndex(indices, entry.hash, index)
        return shiftRemoveFinish(index)
    }

    internal fun shiftRemoveFinish(index: Int): Pair<K, V> {
        decrementIndices(index + 1, entries.size)
        val entry = entries.removeAt(index)
        return entry.key to entry.value
    }

    public fun swapRemoveIndex(index: Int): Pair<K, V>? {
        if (index !in entries.indices) return null
        val entry = entries[index]
        eraseIndex(indices, entry.hash, index)
        return swapRemoveFinish(index)
    }

    internal fun swapRemoveFinish(index: Int): Pair<K, V> {
        val lastIndex = entries.size - 1
        if (index == lastIndex) {
            val entry = entries.removeAt(lastIndex)
            return entry.key to entry.value
        }
        val entry = entries[index]
        val lastEntry = entries.removeAt(lastIndex)
        entries[index] = lastEntry
        updateIndex(indices, lastEntry.hash, lastIndex, index)
        return entry.key to entry.value
    }

    internal fun decrementIndices(start: Int, end: Int) {
        for (i in indices.items.indices) {
            val idx = indices.items[i]
            if (idx in start until end) {
                indices.items[i] = idx - 1
            }
        }
    }

    internal fun incrementIndices(start: Int, end: Int) {
        for (i in indices.items.indices) {
            val idx = indices.items[i]
            if (idx in start until end) {
                indices.items[i] = idx + 1
            }
        }
    }

    public fun moveIndex(from: Int, to: Int) {
        if (from != to) {
            require(from in entries.indices && to in entries.indices)
            moveIndexInner(from, to)
            val i = indices.items.indexOf(from)
            if (i >= 0) {
                indices.items[i] = to
            }
        }
    }

    internal fun moveIndexInner(from: Int, to: Int) {
        if (from < to) {
            decrementIndices(from + 1, to + 1)
            val item = entries.removeAt(from)
            entries.add(to, item)
        } else if (to < from) {
            incrementIndices(to, from)
            val item = entries.removeAt(from)
            entries.add(to, item)
        }
    }

    public fun swapIndices(a: Int, b: Int) {
        if (a == b && a in entries.indices) return
        require(a in entries.indices && b in entries.indices)
        val hashA = entries[a].hash
        val hashB = entries[b].hash
        updateIndex(indices, hashA, a, b)
        updateIndex(indices, hashB, b, a)
        val temp = entries[a]
        entries[a] = entries[b]
        entries[b] = temp
    }

    public fun clone(): Core<K, V> =
        Core(indices.clone(), entries.map { it.clone() }.toMutableList())

    public fun cloneFrom(other: Core<K, V>) {
        indices.cloneFrom(other.indices)
        entries.clear()
        entries.addAll(other.entries.map { it.clone() })
    }
}
