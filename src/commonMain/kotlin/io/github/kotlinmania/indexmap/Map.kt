// port-lint: source map.rs
package io.github.kotlinmania.indexmap

// A hash table where the iteration order of the key-value pairs is independent
// of the hash values of the keys.
//
// The key-value pairs have a consistent order determined by insertion and
// removal calls on the map. The order does not depend on the keys or on their
// hash values.
public class IndexMap<K, V> private constructor(
    private val entries: MutableList<Bucket<K, V>>,
) : Iterable<Pair<K, V>> {

    public constructor() : this(mutableListOf())

    public companion object {
        // Create a new map. Does not allocate storage for entries yet.
        public fun <K, V> new(): IndexMap<K, V> = IndexMap()

        // Create a new map with capacity for n key-value pairs.
        public fun <K, V> withCapacity(n: Int): IndexMap<K, V> =
            IndexMap(ArrayList(n.coerceAtLeast(0)))
    }

    // Return the number of key-value pairs in the map.
    public fun len(): Int = entries.size

    // Returns true if the map contains no elements.
    public fun isEmpty(): Boolean = entries.isEmpty()

    // Return the number of elements the map can hold without reallocating.
    // Kotlin common code does not expose ArrayList capacity, so this returns
    // the lower bound guaranteed by the current entries.
    public fun capacity(): Int = entries.size

    // Return an iterator over the key-value pairs of the map, in their order.
    override fun iterator(): Iterator<Pair<K, V>> =
        entries.map { it.key to it.value }.iterator()

    // Return the keys of the map, in their order.
    public fun keys(): List<K> = entries.map { it.key }

    // Return the values of the map, in their order.
    public fun values(): List<V> = entries.map { it.value }

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

    private fun hashValueFor(key: K): HashValue {
        val hash = key?.hashCode() ?: 0
        return HashValue(hash.toLong().toULong())
    }
}
