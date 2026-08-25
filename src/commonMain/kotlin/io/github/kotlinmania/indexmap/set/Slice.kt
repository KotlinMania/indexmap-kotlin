@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source set/slice.rs

package io.github.kotlinmania.indexmap.set

import io.github.kotlinmania.indexmap.Bucket
import io.github.kotlinmania.indexmap.IndexRange
import io.github.kotlinmania.indexmap.RangeBounds
import io.github.kotlinmania.indexmap.map.SearchResult
import io.github.kotlinmania.indexmap.trySimplifyRange
import kotlin.native.HiddenFromObjC

/**
 * A dynamically-sized slice of values in an IndexSet.
 *
 * This supports indexed operations much like a list slice,
 * but not any hashed operations on the values.
 */
@HiddenFromObjC
public class Slice<T> internal constructor(
    private val entries: MutableList<Bucket<T, Unit>>,
    private val start: Int,
    private val endExclusive: Int,
) : Iterable<T> {
    init {
        require(start in 0..entries.size) {
            "slice start index $start out of range for entries of length ${entries.size}"
        }
        require(endExclusive in start..entries.size) {
            "slice end index $endExclusive out of range for entries of length ${entries.size}"
        }
    }

    public companion object {
        /** Returns an empty slice. */
        public fun <T> new(): Slice<T> = Slice(mutableListOf(), 0, 0)

        /** Returns an empty slice. */
        public fun <T> default(): Slice<T> = new()

        public fun <T> from(slice: Slice<T>): Slice<T> = slice.clone()

        public fun <T> fromSlice(slice: Slice<T>): Slice<T> = slice.clone()

        public fun <T> fromBoxed(slice: Slice<T>): Slice<T> = slice.clone()

        internal fun <T> fromEntries(entries: MutableList<Bucket<T, Unit>>): Slice<T> =
            Slice(entries.map { it.clone() }.toMutableList(), 0, entries.size)
    }

    /** Return the number of elements in the set slice. */
    public fun len(): Int = endExclusive - start

    /** Returns true if the set slice contains no elements. */
    public fun isEmpty(): Boolean = len() == 0

    /** Return a boxed slice view. */
    public fun intoBoxed(): Slice<T> = this

    /**
     * Get a value by index.
     *
     * Valid indices are `0 <= index < len()`.
     */
    public fun getIndex(index: Int): T? =
        if (index in 0 until len()) entries[absoluteIndex(index)].key else null

    /** Get the value at a slice index. */
    public operator fun get(index: Int): T =
        entries[absoluteIndex(index)].key

    /** Get the value at a slice index. */
    public fun index(index: Int): T = this[index]

    /** Returns the first value. */
    public fun first(): T? = if (isEmpty()) null else this[0]

    /** Returns the last value. */
    public fun last(): T? = if (isEmpty()) null else this[len() - 1]

    /**
     * Divides one slice into two at an index.
     *
     * Throws IllegalArgumentException if index > len.
     */
    public fun splitAt(index: Int): Pair<Slice<T>, Slice<T>> {
        require(index in 0..len()) {
            "split index $index out of bounds for slice of length ${len()}"
        }
        val mid = absoluteIndex(index)
        return Slice(entries, start, mid) to Slice(entries, mid, endExclusive)
    }

    /**
     * Divides one slice into two at an index, or null if out of bounds.
     */
    public fun splitAtChecked(index: Int): Pair<Slice<T>, Slice<T>>? {
        if (index !in 0..len()) return null
        val mid = absoluteIndex(index)
        return Slice(entries, start, mid) to Slice(entries, mid, endExclusive)
    }

    /**
     * Returns the first value and the rest of the slice, or null if empty.
     */
    public fun splitFirst(): Pair<T, Slice<T>>? {
        if (isEmpty()) return null
        return get(0) to Slice(entries, start + 1, endExclusive)
    }

    /**
     * Returns the last value and the rest of the slice, or null if empty.
     */
    public fun splitLast(): Pair<T, Slice<T>>? {
        if (isEmpty()) return null
        return get(len() - 1) to Slice(entries, start, endExclusive - 1)
    }


    /**
     * Returns a slice of values in the given range of indices.
     *
     * Valid indices are `0 <= index < len()`.
     */
    public fun getRange(start: Int, endExclusive: Int): Slice<T>? {
        if (start < 0 || endExclusive < start || endExclusive > len()) {
            return null
        }
        return Slice(entries, absoluteIndex(start), absoluteIndex(endExclusive))
    }

    /**
     * Returns a slice of values in the given range of indices.
     */
    public fun getRange(range: IntRange): Slice<T>? =
        if (range.isEmpty()) {
            if (range.first in 0..len()) Slice(entries, absoluteIndex(range.first), absoluteIndex(range.first)) else null
        } else {
            getRange(range.first, range.last + 1)
        }

    internal fun getRange(range: RangeBounds<Int>): Slice<T>? {
        val simplified = trySimplifyRange(range, len()) ?: return null
        return getRange(simplified)
    }

    internal fun getRange(range: IndexRange): Slice<T>? =
        getRange(range.start, range.end)

    /**
     * Search over a sorted set slice for a value.
     *
     * Computes in O(log(n)) time.
     */
    public fun binarySearch(target: T, comparator: Comparator<T>): SearchResult {
        var low = 0
        var high = len() - 1
        while (low <= high) {
            val mid = (low + high) ushr 1
            val midVal = get(mid)
            val cmp = comparator.compare(midVal, target)
            when {
                cmp < 0 -> low = mid + 1
                cmp > 0 -> high = mid - 1
                else -> return SearchResult.found(mid)
            }
        }
        return SearchResult.insertion(low)
    }

    /**
     * Search over a sorted set slice with a custom comparator lambda.
     *
     * Computes in O(log(n)) time.
     */
    public fun binarySearchBy(comparator: (T) -> Int): SearchResult {
        var low = 0
        var high = len() - 1
        while (low <= high) {
            val mid = (low + high) ushr 1
            val midVal = get(mid)
            val cmp = comparator(midVal)
            when {
                cmp < 0 -> low = mid + 1
                cmp > 0 -> high = mid - 1
                else -> return SearchResult.found(mid)
            }
        }
        return SearchResult.insertion(low)
    }

    /**
     * Search over a sorted set slice by a key extraction function.
     *
     * Computes in O(log(n)) time.
     */
    public fun <K> binarySearchByKey(
        target: K,
        keySelector: (T) -> K,
        comparator: Comparator<K>,
    ): SearchResult =
        binarySearchBy { item ->
            comparator.compare(keySelector(item), target)
        }

    /**
     * Search over a sorted set slice by a key extraction function using natural ordering.
     */
    public fun <K : Comparable<K>> binarySearchByKey(
        target: K,
        keySelector: (T) -> K,
    ): SearchResult =
        binarySearchByKey(target, keySelector, naturalOrder())

    /**
     * Checks if the values of this slice are sorted.
     */
    public fun isSorted(comparator: Comparator<T>): Boolean {
        for (i in 0 until len() - 1) {
            if (comparator.compare(get(i), get(i + 1)) > 0) {
                return false
            }
        }
        return true
    }

    /**
     * Checks if this slice is sorted using the given comparator function.
     */
    public fun isSortedBy(comparator: (T, T) -> Boolean): Boolean {
        for (i in 0 until len() - 1) {
            if (!comparator(get(i), get(i + 1))) {
                return false
            }
        }
        return true
    }

    /**
     * Checks if this slice is sorted using the given sort-key function.
     */
    public fun <K> isSortedByKey(
        keySelector: (T) -> K,
        comparator: Comparator<K>,
    ): Boolean {
        for (i in 0 until len() - 1) {
            val a = keySelector(get(i))
            val b = keySelector(get(i + 1))
            if (comparator.compare(a, b) > 0) {
                return false
            }
        }
        return true
    }

    /**
     * Checks if this slice is sorted using the given sort-key function and natural ordering.
     */
    public fun <K : Comparable<K>> isSortedByKey(keySelector: (T) -> K): Boolean =
        isSortedByKey(keySelector, naturalOrder())

    /**
     * Returns the partition point according to the given predicate.
     *
     * Computes in O(log(n)) time.
     */
    public fun partitionPoint(predicate: (T) -> Boolean): Int {
        var left = 0
        var right = len()
        while (left < right) {
            val mid = (left + right) ushr 1
            if (predicate(get(mid))) {
                left = mid + 1
            } else {
                right = mid
            }
        }
        return left
    }

    /** Return the slice values as a List. */
    public fun toList(): List<T> =
        (0 until len()).map { get(it) }

    /** Return the slice values as an owned list. */
    public fun intoEntries(): List<T> = toList()

    /** Return an iterator over the values of the set slice. */
    override fun iterator(): Iterator<T> =
        object : Iterator<T> {
            private var index = 0

            override fun hasNext(): Boolean = index < len()

            override fun next(): T {
                if (!hasNext()) throw NoSuchElementException()
                return get(index++)
            }
        }

    /** Return an iterator over the values of the set slice. */
    public fun iter(): Iterator<T> = iterator()

    /** Return an owning iterator over the values of the set slice. */
    public fun intoIter(): Iterator<T> = iterator()

    /** Clone this slice view. */
    public fun clone(): Slice<T> =
        Slice(entries.map { it.clone() }.toMutableList(), start, endExclusive)

    public fun fmt(): String = toString()

    public fun eq(other: Slice<T>): Boolean = this == other

    public fun partialCmp(other: Slice<T>, comparator: Comparator<in T>): Int =
        cmp(other, comparator)

    public fun cmp(other: Slice<T>, comparator: Comparator<in T>): Int {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Slice<*>) return false
        if (len() != other.len()) return false
        for (i in 0 until len()) {
            if (get(i) != other[i]) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = len().hashCode()
        for (item in this) {
            result = 31 * result + (item?.hashCode() ?: 0)
        }
        return result
    }

    override fun toString(): String =
        toList().joinToString(prefix = "[", postfix = "]")

    private fun absoluteIndex(relativeIndex: Int): Int = start + relativeIndex
}

/**
 * Checks if the values of this slice are sorted by natural ordering.
 */
public fun <T : Comparable<T>> Slice<T>.isSorted(): Boolean =
    isSorted(naturalOrder())

/**
 * Search over a sorted set slice for a value using natural ordering.
 */
public fun <T : Comparable<T>> Slice<T>.binarySearch(target: T): SearchResult =
    binarySearch(target, naturalOrder())

