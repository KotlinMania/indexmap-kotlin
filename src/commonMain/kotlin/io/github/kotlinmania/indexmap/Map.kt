@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source map.rs
package io.github.kotlinmania.indexmap

import io.github.kotlinmania.indexmap.map.SearchResult
import io.github.kotlinmania.indexmap.map.Slice
import io.github.kotlinmania.indexmap.map.Entry
import io.github.kotlinmania.indexmap.map.IndexedEntry
import io.github.kotlinmania.indexmap.map.MutableKeys
import kotlin.native.HiddenFromObjC

// A hash table where the iteration order of the key-value pairs is independent
// of the hash values of the keys.
//
// The key-value pairs have a consistent order determined by insertion and
// removal calls on the map. The order does not depend on the keys or on their
// hash values.
@HiddenFromObjC
public class IndexMap<K, V> private constructor(
    private val entries: MutableList<Bucket<K, V>>,
) : Iterable<Pair<K, V>>, MutableKeys<K, V> {

    public constructor() : this(mutableListOf())

    public companion object {
        // Create a new map. Does not allocate storage for entries yet.
        public fun <K, V> new(): IndexMap<K, V> = IndexMap()

        public fun <K, V> default(): IndexMap<K, V> = new()

        // Create a new map with capacity for n key-value pairs.
        public fun <K, V> withCapacity(n: Int): IndexMap<K, V> =
            IndexMap(ArrayList(n.coerceAtLeast(0)))

        public fun <K, V, S> withHasher(hasher: S): IndexMap<K, V> {
            hasher.hashCode()
            return new()
        }

        public fun <K, V, S> withCapacityAndHasher(n: Int, hasher: S): IndexMap<K, V> {
            hasher.hashCode()
            return withCapacity(n)
        }

        public fun <K, V> from(entries: Iterable<Pair<K, V>>): IndexMap<K, V> =
            new<K, V>().also { it.extend(entries) }

        public fun <K, V> fromIter(entries: Iterable<Pair<K, V>>): IndexMap<K, V> =
            from(entries)
    }

    // Return the number of key-value pairs in the map.
    public fun len(): Int = entries.size

    // Returns true if the map contains no elements.
    public fun isEmpty(): Boolean = entries.isEmpty()

    // Return the number of elements the map can hold without reallocating.
    // Kotlin common code does not expose ArrayList capacity, so this returns
    // the lower bound guaranteed by the current entries.
    public fun capacity(): Int = entries.size

    public fun hasher(): String = "kotlin.hashCode"

    public fun clone(): IndexMap<K, V> = from(asEntries())

    public fun cloneFrom(other: IndexMap<K, V>) {
        clear()
        extend(other.asEntries())
    }

    public fun fmt(): String = toString()

    // Return an iterator over the key-value pairs of the map, in their order.
    override fun iterator(): Iterator<Pair<K, V>> =
        entries.map { it.key to it.value }.iterator()

    // Return an iterator over the key-value pairs of the map, in their order.
    public fun iter(): Iterator<Pair<K, V>> = iterator()

    // Return the keys of the map, in their order.
    public fun keys(): List<K> = entries.map { it.key }

    // Return the keys as an ordered list.
    public fun intoKeys(): List<K> = keys()

    // Return the values of the map, in their order.
    public fun values(): List<V> = entries.map { it.value }

    // Return the values as an ordered list.
    public fun intoValues(): List<V> = values()

    // Return a shared slice view of the map's entries.
    public fun asSlice(): Slice<K, V> = Slice.fromEntries(entries)

    // Return the map entries as an ordered list.
    public fun asEntries(): List<Pair<K, V>> = asSlice().toList()

    // Return the map entries as an ordered list.
    public fun intoEntries(): List<Pair<K, V>> = asEntries()

    // Get a contiguous slice by entry indices.
    public fun getRange(start: Int, endExclusive: Int): Slice<K, V>? =
        asSlice().getRange(start, endExclusive)

    // Binary search the ordered entries by key.
    public fun binarySearchKeys(key: K, comparator: Comparator<in K>): SearchResult =
        asSlice().binarySearchKeys(key, comparator)

    // Binary search the ordered entries with a comparator over key-value pairs.
    public fun binarySearchBy(compare: (K, V) -> Int): SearchResult =
        asSlice().binarySearchBy(compare)

    // Binary search the ordered entries by a derived key.
    public fun <T> binarySearchByKey(
        key: T,
        selector: (K, V) -> T,
        comparator: Comparator<in T>,
    ): SearchResult =
        asSlice().binarySearchByKey(key, selector, comparator)

    // Return true if the keys are sorted.
    public fun isSorted(comparator: Comparator<in K>): Boolean =
        asSlice().isSorted(comparator)

    // Return true if every adjacent entry pair satisfies the ordering predicate.
    public fun isSortedBy(inOrder: (K, V, K, V) -> Boolean): Boolean =
        asSlice().isSortedBy(inOrder)

    // Return true if the entries are sorted by a derived key.
    public fun <T> isSortedByKey(
        selector: (K, V) -> T,
        comparator: Comparator<in T>,
    ): Boolean =
        asSlice().isSortedByKey(selector, comparator)

    // Return the split point where the predicate stops matching.
    public fun partitionPoint(predicate: (K, V) -> Boolean): Int =
        asSlice().partitionPoint(predicate)

    // Remove all key-value pairs in the map, while preserving the map object.
    public fun clear() {
        entries.clear()
    }

    // Shorten the map, keeping the first len elements and dropping the rest.
    public fun truncate(len: Int) {
        if (len < entries.size) {
            entries.subList(len.coerceAtLeast(0), entries.size).clear()
        }
    }

    // Return true if an equivalent key exists in the map.
    public fun containsKey(key: K): Boolean = getIndexOf(key) != null

    // Return the stored value for key, if it is present, else null.
    public operator fun get(key: K): V? =
        getIndexOf(key)?.let { entries[it].value }

    // Get a value by entry index.
    public fun index(index: Int): V =
        entries[index].value

    // Return the stored key-value pair for key, if it is present, else null.
    public fun getKeyValue(key: K): Pair<K, V>? =
        getIndexOf(key)?.let { entries[it].key to entries[it].value }

    // Return the index and stored key-value pair for key, if it is present.
    public fun getFull(key: K): Triple<Int, K, V>? =
        getIndexOf(key)?.let { Triple(it, entries[it].key, entries[it].value) }

    // Return the item index for key, if it is present, else null.
    public fun getIndexOf(key: K): Int? {
        val index = entries.indexOfFirst { it.key == key }
        return if (index >= 0) index else null
    }

    // Get a key-value pair by index.
    public fun getIndex(index: Int): Pair<K, V>? =
        entries.getOrNull(index)?.keyValue()

    // Get an indexed entry by index.
    public fun getIndexEntry(index: Int): Pair<Int, Pair<K, V>>? =
        getIndex(index)?.let { index to it }

    // Get an indexed entry handle by index.
    public fun indexedEntry(index: Int): IndexedEntry<K, V>? =
        if (index in entries.indices) IndexedEntry(this, index) else null

    // Get the first key-value pair.
    public fun first(): Pair<K, V>? = entries.firstOrNull()?.keyValue()

    // Get the last key-value pair.
    public fun last(): Pair<K, V>? = entries.lastOrNull()?.keyValue()

    // Insert a key-value pair in the map.
    //
    // If an equivalent key already exists, the key remains in its existing
    // position, the value is updated, and the older value is returned.
    // If no equivalent key existed, the pair is inserted last in order and
    // null is returned.
    public fun insert(key: K, value: V): V? = insertFull(key, value).second

    // Insert a key-value pair in the map, and get its index.
    public fun insertFull(key: K, value: V): Pair<Int, V?> {
        val existing = getIndexOf(key)
        if (existing != null) {
            val old = entries[existing].value
            entries[existing].value = value
            return existing to old
        }

        entries += Bucket(hashValueFor(key), key, value)
        return entries.lastIndex to null
    }

    // Get an entry handle for in-place ordered-map insertion or update.
    public fun entry(key: K): Entry<K, V> {
        val existing = getIndexOf(key)
        return if (existing != null) {
            Entry.Occupied(io.github.kotlinmania.indexmap.map.OccupiedEntry(this, existing))
        } else {
            Entry.Vacant(io.github.kotlinmania.indexmap.map.VacantEntry(this, key, entries.size))
        }
    }

    // Insert a key-value pair at its ordered position among sorted keys.
    public fun insertSorted(key: K, value: V, comparator: Comparator<in K>): Pair<Int, V?> {
        val index = binarySearchKeys(key, comparator).index
        return insertBefore(index, key, value)
    }

    // Insert a key-value pair at its ordered position under a comparator.
    public fun insertSortedBy(key: K, value: V, compare: (K, V, K, V) -> Int): Pair<Int, V?> {
        val index = binarySearchBy { entryKey, entryValue -> compare(entryKey, entryValue, key, value) }.index
        return insertBefore(index, key, value)
    }

    // Insert a key-value pair at its ordered position by a derived key.
    public fun <T> insertSortedByKey(
        key: K,
        value: V,
        selector: (K, V) -> T,
        comparator: Comparator<in T>,
    ): Pair<Int, V?> {
        val searchKey = selector(key, value)
        val index = binarySearchByKey(searchKey, selector, comparator).index
        return insertBefore(index, key, value)
    }

    // Replace the key at an index while preserving the stored value.
    public fun replaceIndex(index: Int, key: K): K {
        require(index in entries.indices) {
            "index out of bounds: the len is ${entries.size} but the index is $index"
        }
        val existing = getIndexOf(key)
        require(existing == null || existing == index) {
            "an equivalent key already exists at index $existing"
        }
        val old = entries[index].key
        entries[index].key = key
        entries[index].hash = hashValueFor(key)
        return old
    }

    // Reserve capacity for additional entries when the platform exposes it.
    public fun reserve(additional: Int) {
        require(additional >= 0) {
            "additional capacity must be non-negative"
        }
    }

    // Reserve exact capacity for additional entries when the platform exposes it.
    public fun reserveExact(additional: Int) {
        reserve(additional)
    }

    public fun tryReserve(additional: Int): TryReserveError? {
        reserve(additional)
        return null
    }

    public fun tryReserveExact(additional: Int): TryReserveError? =
        tryReserve(additional)

    public fun shrinkToFit() {
        // Kotlin common collections do not expose backing-capacity control.
    }

    public fun shrinkTo(minCapacity: Int) {
        require(minCapacity >= 0) {
            "minimum capacity must be non-negative"
        }
    }

    // Insert a key-value pair before the entry at index, or at the end.
    public fun insertBefore(index: Int, key: K, value: V): Pair<Int, V?> {
        require(index in 0..entries.size) {
            "index out of bounds: the len is ${entries.size} but the index is $index. Expected index <= len"
        }

        val existing = getIndexOf(key)
        if (existing != null) {
            val old = entries[existing].value
            val bucket = entries.removeAt(existing)
            bucket.value = value
            val destination = if (index > existing) index - 1 else index
            entries.add(destination, bucket)
            return destination to old
        }

        entries.add(index, Bucket(hashValueFor(key), key, value))
        return index to null
    }

    // Insert a key-value pair at the given index.
    public fun shiftInsert(index: Int, key: K, value: V): V? {
        val existing = getIndexOf(key)
        if (existing != null) {
            require(index in 0 until entries.size) {
                "index out of bounds: the len is ${entries.size} but the index is $index"
            }
            val old = entries[existing].value
            val bucket = entries.removeAt(existing)
            bucket.value = value
            entries.add(index, bucket)
            return old
        }

        require(index in 0..entries.size) {
            "index out of bounds: the len is ${entries.size} but the index is $index. Expected index <= len"
        }
        entries.add(index, Bucket(hashValueFor(key), key, value))
        return null
    }

    // Remove the key-value pair by index by shifting following entries down.
    public fun shiftRemoveIndex(index: Int): Pair<K, V>? =
        if (index in entries.indices) entries.removeAt(index).keyValue() else null

    // Remove the key-value pair by key by shifting following entries down.
    public fun shiftRemove(key: K): V? =
        getIndexOf(key)?.let { entries.removeAt(it).value }

    // Remove the key-value pair by key by shifting following entries down.
    public fun remove(key: K): V? = shiftRemove(key)

    // Remove the key-value pair by key by shifting following entries down.
    public fun removeEntry(key: K): Pair<K, V>? =
        getIndexOf(key)?.let { entries.removeAt(it).keyValue() }

    // Remove the key-value pair by key and return its former index.
    public fun shiftRemoveFull(key: K): Triple<Int, K, V>? =
        getIndexOf(key)?.let { index ->
            val removed = entries.removeAt(index)
            Triple(index, removed.key, removed.value)
        }

    public fun shiftRemoveEntry(key: K): Pair<K, V>? = removeEntry(key)

    // Remove the key-value pair by index by swapping it with the last entry.
    public fun swapRemoveIndex(index: Int): Pair<K, V>? {
        if (index !in entries.indices) {
            return null
        }
        entries[index] = entries.last().also { entries[entries.lastIndex] = entries[index] }
        return entries.removeAt(entries.lastIndex).keyValue()
    }

    // Remove the key-value pair by key by swapping it with the last entry.
    public fun swapRemove(key: K): V? =
        getIndexOf(key)?.let { swapRemoveIndex(it)?.second }

    // Remove the key-value pair by key by swapping it with the last entry.
    public fun swapRemoveEntry(key: K): Pair<K, V>? =
        getIndexOf(key)?.let { swapRemoveIndex(it) }

    // Remove the key-value pair by key and return its former index.
    public fun swapRemoveFull(key: K): Triple<Int, K, V>? =
        getIndexOf(key)?.let { index ->
            val removed = swapRemoveIndex(index) ?: return null
            Triple(index, removed.first, removed.second)
        }

    // Remove and return the last key-value pair.
    public fun pop(): Pair<K, V>? =
        if (entries.isEmpty()) null else entries.removeAt(entries.lastIndex).keyValue()

    // Remove and return the last key-value pair if the predicate accepts it.
    public fun popIf(predicate: (K, V) -> Boolean): Pair<K, V>? {
        val last = entries.lastOrNull() ?: return null
        return if (predicate(last.key, last.value)) pop() else null
    }

    // Keep only the entries accepted by the predicate.
    public fun retain(keep: (K, V) -> Boolean) {
        var index = 0
        while (index < entries.size) {
            val entry = entries[index]
            if (keep(entry.key, entry.value)) {
                index += 1
            } else {
                entries.removeAt(index)
            }
        }
    }

    override fun getFullMut2(key: K): Triple<Int, K, V>? = getFull(key)

    override fun getIndexMut2(index: Int): Pair<K, V>? = getIndex(index)

    override fun iterMut2(): List<Pair<K, V>> = asEntries()

    override fun retain2(keep: (K, V) -> Boolean) {
        retain(keep)
    }

    // Drain a range of entries and return them in removal order.
    public fun drain(start: Int = 0, endExclusive: Int = entries.size): List<Pair<K, V>> {
        require(start in 0..entries.size) {
            "range start index $start out of range for map of length ${entries.size}"
        }
        require(endExclusive in start..entries.size) {
            "range end index $endExclusive out of range for map of length ${entries.size}"
        }
        val removed = entries.subList(start, endExclusive).map { it.keyValue() }
        entries.subList(start, endExclusive).clear()
        return removed
    }

    // Remove entries accepted by a predicate and return them in original order.
    public fun extractIf(predicate: (K, V) -> Boolean): List<Pair<K, V>> {
        val removed = mutableListOf<Pair<K, V>>()
        var index = 0
        while (index < entries.size) {
            val entry = entries[index]
            if (predicate(entry.key, entry.value)) {
                removed += entries.removeAt(index).keyValue()
            } else {
                index += 1
            }
        }
        return removed
    }

    // Split off entries starting at index.
    public fun splitOff(index: Int): IndexMap<K, V> {
        require(index in 0..entries.size) {
            "index out of bounds: the len is ${entries.size} but the index is $index"
        }
        val tail = entries.subList(index, entries.size).map { it.keyValue() }
        entries.subList(index, entries.size).clear()
        return from(tail)
    }

    // Replace a range of entries and return the removed entries.
    public fun splice(
        start: Int,
        endExclusive: Int,
        replacement: Iterable<Pair<K, V>>,
    ): List<Pair<K, V>> {
        val removed = drain(start, endExclusive)
        var index = start
        for ((key, value) in replacement) {
            insertBefore(index, key, value)
            index += 1
        }
        return removed
    }

    // Move the position of a key-value pair from one index to another.
    public fun moveIndex(from: Int, to: Int) {
        require(from in entries.indices) {
            "index out of bounds: the len is ${entries.size} but the index is $from"
        }
        require(to in entries.indices) {
            "index out of bounds: the len is ${entries.size} but the index is $to"
        }
        if (from != to) {
            entries.add(to, entries.removeAt(from))
        }
    }

    // Swap the position of two key-value pairs in the map.
    public fun swapIndices(a: Int, b: Int) {
        require(a in entries.indices) {
            "index out of bounds: the len is ${entries.size} but the index is $a"
        }
        require(b in entries.indices) {
            "index out of bounds: the len is ${entries.size} but the index is $b"
        }
        val left = entries[a]
        entries[a] = entries[b]
        entries[b] = left
    }

    // Reverse the order of the key-value pairs.
    public fun reverse() {
        entries.reverse()
    }

    // Sort the key-value pairs by key.
    public fun sortKeys(comparator: Comparator<in K>) {
        entries.sortWith { left, right -> comparator.compare(left.key, right.key) }
    }

    // Sort the key-value pairs with a comparator over entries.
    public fun sortBy(compare: (K, V, K, V) -> Int) {
        entries.sortWith { left, right -> compare(left.key, left.value, right.key, right.value) }
    }

    // Return the sorted key-value pairs without modifying the map.
    public fun sortedBy(compare: (K, V, K, V) -> Int): List<Pair<K, V>> =
        entries.map { it.clone() }
            .sortedWith { left, right -> compare(left.key, left.value, right.key, right.value) }
            .map { it.keyValue() }

    // Sort the key-value pairs by a derived key.
    public fun <T> sortByKey(selector: (K, V) -> T, comparator: Comparator<in T>) {
        entries.sortWith { left, right -> comparator.compare(selector(left.key, left.value), selector(right.key, right.value)) }
    }

    public fun sortUnstableKeys(comparator: Comparator<in K>) {
        sortKeys(comparator)
    }

    public fun sortUnstableBy(compare: (K, V, K, V) -> Int) {
        sortBy(compare)
    }

    public fun sortedUnstableBy(compare: (K, V, K, V) -> Int): List<Pair<K, V>> =
        sortedBy(compare)

    public fun <T> sortUnstableByKey(selector: (K, V) -> T, comparator: Comparator<in T>) {
        sortByKey(selector, comparator)
    }

    public fun <T> sortByCachedKey(selector: (K, V) -> T, comparator: Comparator<in T>) {
        sortByKey(selector, comparator)
    }

    // Return the first key-value pair with its index.
    public fun firstEntry(): Pair<Int, Pair<K, V>>? =
        first()?.let { 0 to it }

    // Return the last key-value pair with its index.
    public fun lastEntry(): Pair<Int, Pair<K, V>>? =
        last()?.let { entries.lastIndex to it }

    // Extend the map with ordered key-value pairs.
    public fun extend(entries: Iterable<Pair<K, V>>) {
        for ((key, value) in entries) {
            insert(key, value)
        }
    }

    // Append all entries from another map and clear the other map.
    public fun append(other: IndexMap<K, V>) {
        extend(other.asEntries())
        other.clear()
    }

    public fun eq(other: IndexMap<K, V>): Boolean = asEntries() == other.asEntries()

    internal fun hash(key: K): HashValue = hashValueFor(key)

    private fun hashValueFor(key: K): HashValue {
        val hash = key?.hashCode() ?: 0
        return HashValue(hash.toLong().toULong())
    }
}
