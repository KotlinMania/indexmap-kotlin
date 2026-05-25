@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source map/slice.rs
package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.Bucket
import io.github.kotlinmania.indexmap.IndexRange
import io.github.kotlinmania.indexmap.RangeBounds
import io.github.kotlinmania.indexmap.trySimplifyRange
import kotlin.native.HiddenFromObjC

// The result of searching an ordered entry slice.
public data class SearchResult(val index: Int, val found: Boolean) {
    public companion object {
        public fun found(index: Int): SearchResult = SearchResult(index, true)
        public fun insertion(index: Int): SearchResult = SearchResult(index, false)
    }
}

// A view over the contiguous key-value entry slice of an IndexMap.
@HiddenFromObjC
public class Slice<K, V> internal constructor(
    private val entries: MutableList<Bucket<K, V>>,
    private val start: Int,
    private val endExclusive: Int,
) : Iterable<Pair<K, V>> {

    init {
        require(start in 0..entries.size) {
            "slice start index $start out of range for entries of length ${entries.size}"
        }
        require(endExclusive in start..entries.size) {
            "slice end index $endExclusive out of range for entries of length ${entries.size}"
        }
    }

    public companion object {
        public fun <K, V> new(): Slice<K, V> = Slice(mutableListOf(), 0, 0)

        public fun <K, V> default(): Slice<K, V> = new()

        public fun <K, V> from(slice: Slice<K, V>): Slice<K, V> = slice.clone()

        internal fun <K, V> fromEntries(entries: MutableList<Bucket<K, V>>): Slice<K, V> =
            Slice(entries.map { it.clone() }.toMutableList(), 0, entries.size)
    }

    // Return the number of key-value pairs in the slice.
    public fun len(): Int = endExclusive - start

    // Return true if the slice contains no key-value pairs.
    public fun isEmpty(): Boolean = len() == 0

    // Get a key-value pair by its slice index.
    public fun getIndex(index: Int): Pair<K, V>? =
        if (index in 0 until len()) entries[absoluteIndex(index)].keyValue() else null

    // Get the value at a slice index.
    public operator fun get(index: Int): V =
        entries[absoluteIndex(index)].value

    // Get the value at a slice index.
    public fun index(index: Int): V = this[index]

    // Get a contiguous subslice by entry indices.
    public fun getRange(start: Int, endExclusive: Int): Slice<K, V>? {
        if (start < 0 || endExclusive < start || endExclusive > len()) {
            return null
        }
        return Slice(entries, absoluteIndex(start), absoluteIndex(endExclusive))
    }

    internal fun getRange(range: RangeBounds<Int>): Slice<K, V>? {
        val simplified = trySimplifyRange(range, len()) ?: return null
        return getRange(simplified)
    }

    internal fun getRange(range: IndexRange): Slice<K, V> =
        Slice(entries, absoluteIndex(range.start), absoluteIndex(range.end))

    // Get the first key-value pair.
    public fun first(): Pair<K, V>? = getIndex(0)

    // Get the last key-value pair.
    public fun last(): Pair<K, V>? = getIndex(len() - 1)

    // Split the slice at index.
    public fun splitAt(index: Int): Pair<Slice<K, V>, Slice<K, V>> {
        require(index in 0..len()) {
            "mid > len"
        }
        return uncheckedSplitAt(index)
    }

    // Split the slice at index, returning null when the index is out of bounds.
    public fun splitAtChecked(index: Int): Pair<Slice<K, V>, Slice<K, V>>? =
        if (index in 0..len()) uncheckedSplitAt(index) else null

    // Split off the first key-value pair and the remaining slice.
    public fun splitFirst(): Pair<Pair<K, V>, Slice<K, V>>? {
        val first = getIndex(0) ?: return null
        return first to Slice(entries, start + 1, endExclusive)
    }

    // Split off the last key-value pair and the preceding slice.
    public fun splitLast(): Pair<Pair<K, V>, Slice<K, V>>? {
        val last = getIndex(len() - 1) ?: return null
        return last to Slice(entries, start, endExclusive - 1)
    }

    // Return the keys in slice order.
    public fun keys(): List<K> = visibleEntries().map { it.key }

    // Return the keys as an owned list.
    public fun intoKeys(): List<K> = keys()

    // Return the values in slice order.
    public fun values(): List<V> = visibleEntries().map { it.value }

    // Return the values as an owned list.
    public fun intoValues(): List<V> = values()

    // Return the key-value pairs in slice order.
    public fun toList(): List<Pair<K, V>> = visibleEntries().map { it.keyValue() }

    // Return the key-value pairs as an owned list.
    public fun intoEntries(): List<Pair<K, V>> = toList()

    override fun iterator(): Iterator<Pair<K, V>> = toList().iterator()

    // Return an iterator over the key-value pairs of the slice.
    public fun iter(): Iterator<Pair<K, V>> = iterator()

    // Binary search the ordered entries by key.
    public fun binarySearchKeys(key: K, comparator: Comparator<in K>): SearchResult =
        binarySearchBy { entryKey, _ -> comparator.compare(entryKey, key) }

    // Binary search the ordered entries with a comparator over key-value pairs.
    public fun binarySearchBy(compare: (K, V) -> Int): SearchResult {
        var low = 0
        var high = len()
        while (low < high) {
            val mid = low + (high - low) / 2
            val bucket = entries[absoluteIndex(mid)]
            val order = compare(bucket.key, bucket.value)
            when {
                order < 0 -> low = mid + 1
                order > 0 -> high = mid
                else -> return SearchResult.found(mid)
            }
        }
        return SearchResult.insertion(low)
    }

    // Binary search by a derived key.
    public fun <T> binarySearchByKey(
        key: T,
        selector: (K, V) -> T,
        comparator: Comparator<in T>,
    ): SearchResult =
        binarySearchBy { entryKey, entryValue -> comparator.compare(selector(entryKey, entryValue), key) }

    // Return true if the slice is sorted under the adjacent-pair predicate.
    public fun isSortedBy(inOrder: (K, V, K, V) -> Boolean): Boolean {
        for (index in 1 until len()) {
            val previous = entries[absoluteIndex(index - 1)]
            val current = entries[absoluteIndex(index)]
            if (!inOrder(previous.key, previous.value, current.key, current.value)) {
                return false
            }
        }
        return true
    }

    // Return true if the slice is sorted by key.
    public fun isSorted(comparator: Comparator<in K>): Boolean =
        isSortedBy { leftKey, _, rightKey, _ -> comparator.compare(leftKey, rightKey) <= 0 }

    // Return true if the slice is sorted by a derived key.
    public fun <T> isSortedByKey(
        selector: (K, V) -> T,
        comparator: Comparator<in T>,
    ): Boolean =
        isSortedBy { leftKey, leftValue, rightKey, rightValue ->
            comparator.compare(selector(leftKey, leftValue), selector(rightKey, rightValue)) <= 0
        }

    // Return the split point where the predicate stops matching.
    public fun partitionPoint(predicate: (K, V) -> Boolean): Int {
        var low = 0
        var high = len()
        while (low < high) {
            val mid = low + (high - low) / 2
            val bucket = entries[absoluteIndex(mid)]
            if (predicate(bucket.key, bucket.value)) {
                low = mid + 1
            } else {
                high = mid
            }
        }
        return low
    }

    public fun clone(): Slice<K, V> =
        Slice(visibleEntries().map { it.clone() }.toMutableList(), 0, len())

    public fun fmt(): String = toString()

    public fun eq(other: Slice<K, V>): Boolean = this == other

    public fun partialCmp(other: Slice<K, V>, comparator: Comparator<in Pair<K, V>>): Int =
        cmp(other, comparator)

    public fun cmp(other: Slice<K, V>, comparator: Comparator<in Pair<K, V>>): Int {
        val left = toList()
        val right = other.toList()
        val commonLength = minOf(left.size, right.size)
        for (index in 0 until commonLength) {
            val order = comparator.compare(left[index], right[index])
            if (order != 0) {
                return order
            }
        }
        return left.size.compareTo(right.size)
    }

    public fun hash(): Int = hashCode()

    override fun equals(other: Any?): Boolean =
        other is Slice<*, *> && toList() == other.toList()

    override fun hashCode(): Int = toList().hashCode()

    override fun toString(): String = toList().toString()

    private fun uncheckedSplitAt(index: Int): Pair<Slice<K, V>, Slice<K, V>> =
        Slice(entries, start, absoluteIndex(index)) to Slice(entries, absoluteIndex(index), endExclusive)

    private fun visibleEntries(): List<Bucket<K, V>> =
        entries.subList(start, endExclusive)

    private fun absoluteIndex(index: Int): Int = start + index
}
