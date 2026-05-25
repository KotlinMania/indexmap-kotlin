@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source set.rs
package io.github.kotlinmania.indexmap

import io.github.kotlinmania.indexmap.map.SearchResult
import kotlin.native.HiddenFromObjC

// A hash set where the iteration order of the values is independent of their
// hash values.
//
// The values have a consistent order determined by insertion and removal
// calls on the set. The order does not depend on the values or on their hash
// values. Internally this mirrors upstream by using IndexMap<T, Unit>.
@HiddenFromObjC
public class IndexSet<T> private constructor(
    private val map: IndexMap<T, Unit>,
) : Iterable<T> {

    public constructor() : this(IndexMap.new())

    public companion object {
        // Create a new set. Does not allocate storage for entries yet.
        public fun <T> new(): IndexSet<T> = IndexSet()

        public fun <T> default(): IndexSet<T> = new()

        // Create a new set with capacity for n values.
        public fun <T> withCapacity(n: Int): IndexSet<T> =
            IndexSet(IndexMap.withCapacity(n))

        public fun <T, S> withHasher(hasher: S): IndexSet<T> =
            IndexSet(IndexMap.withHasher<T, Unit, S>(hasher))

        public fun <T, S> withCapacityAndHasher(n: Int, hasher: S): IndexSet<T> =
            IndexSet(IndexMap.withCapacityAndHasher<T, Unit, S>(n, hasher))

        public fun <T> from(values: Iterable<T>): IndexSet<T> =
            new<T>().also { it.extend(values) }

        public fun <T> fromIter(values: Iterable<T>): IndexSet<T> =
            from(values)
    }

    // Return the number of values in the set.
    public fun len(): Int = map.len()

    // Returns true if the set contains no elements.
    public fun isEmpty(): Boolean = map.isEmpty()

    // Return the number of elements the set can hold without reallocating.
    public fun capacity(): Int = map.capacity()

    public fun hasher(): String = map.hasher()

    public fun clone(): IndexSet<T> = from(asList())

    public fun cloneFrom(other: IndexSet<T>) {
        clear()
        extend(other.asList())
    }

    public fun fmt(): String = toString()

    // Return an iterator over the values of the set, in their order.
    override fun iterator(): Iterator<T> = map.keys().iterator()

    // Return an iterator over the values of the set, in their order.
    public fun iter(): Iterator<T> = iterator()

    // Return the set values as an ordered list.
    public fun asList(): List<T> = map.keys()

    // Return the set values as an ordered list.
    public fun intoList(): List<T> = asList()

    // Return a value by index.
    public operator fun get(index: Int): T = map.getIndex(index)!!.first

    // Return a value by index, if it exists.
    public fun getIndex(index: Int): T? = map.getIndex(index)?.first

    // Return true if an equivalent value exists in the set.
    public fun contains(value: T): Boolean = map.containsKey(value)

    // Return the stored value if it is present.
    public fun get(value: T): T? = map.getKeyValue(value)?.first

    // Return item index and stored value.
    public fun getFull(value: T): Pair<Int, T>? =
        map.getFull(value)?.let { (index, stored, _) -> index to stored }

    // Return item index, if it exists in the set.
    public fun getIndexOf(value: T): Int? = map.getIndexOf(value)

    // Get the first value.
    public fun first(): T? = map.first()?.first

    // Get the last value.
    public fun last(): T? = map.last()?.first

    // Remove all elements in the set, while preserving the set object.
    public fun clear() {
        map.clear()
    }

    // Shorten the set, keeping the first len elements and dropping the rest.
    public fun truncate(len: Int) {
        map.truncate(len)
    }

    // Insert the value into the set.
    public fun insert(value: T): Boolean = map.insert(value, Unit) == null

    // Insert the value into the set, and get its index.
    public fun insertFull(value: T): Pair<Int, Boolean> {
        val (index, existing) = map.insertFull(value, Unit)
        return index to (existing == null)
    }

    // Insert the value into the set at its ordered position among sorted values.
    public fun insertSorted(value: T, comparator: Comparator<in T>): Pair<Int, Boolean> {
        val (index, existing) = map.insertSorted(value, Unit, comparator)
        return index to (existing == null)
    }

    // Insert the value into the set at its ordered position under a comparator.
    public fun insertSortedBy(value: T, compare: (T, T) -> Int): Pair<Int, Boolean> {
        val (index, existing) = map.insertSortedBy(value, Unit) { left, _, right, _ -> compare(left, right) }
        return index to (existing == null)
    }

    // Insert the value into the set at its ordered position by a derived key.
    public fun <K> insertSortedByKey(
        value: T,
        selector: (T) -> K,
        comparator: Comparator<in K>,
    ): Pair<Int, Boolean> {
        val (index, existing) = map.insertSortedByKey(value, Unit, { key, _ -> selector(key) }, comparator)
        return index to (existing == null)
    }

    // Insert the value before the value at index, or at the end.
    public fun insertBefore(index: Int, value: T): Pair<Int, Boolean> {
        val (storedIndex, existing) = map.insertBefore(index, value, Unit)
        return storedIndex to (existing == null)
    }

    // Insert the value at the given index.
    public fun shiftInsert(index: Int, value: T): Boolean =
        map.shiftInsert(index, value, Unit) == null

    // Add a value, replacing the existing value if any without changing order.
    public fun replace(value: T): T? = replaceFull(value).second

    // Add a value, returning its index and replaced value if any.
    public fun replaceFull(value: T): Pair<Int, T?> {
        val existing = getIndexOf(value)
        if (existing != null) {
            val old = map.replaceIndex(existing, value)
            return existing to old
        }
        val (index, _) = map.insertFull(value, Unit)
        return index to null
    }

    // Replace the value at the given index with a unique value.
    public fun replaceIndex(index: Int, value: T): T = map.replaceIndex(index, value)

    // Reserve capacity for additional values when the platform exposes it.
    public fun reserve(additional: Int) {
        map.reserve(additional)
    }

    // Reserve exact capacity for additional values when the platform exposes it.
    public fun reserveExact(additional: Int) {
        map.reserveExact(additional)
    }

    public fun tryReserve(additional: Int): TryReserveError? = map.tryReserve(additional)

    public fun tryReserveExact(additional: Int): TryReserveError? = map.tryReserveExact(additional)

    public fun shrinkToFit() {
        map.shrinkToFit()
    }

    public fun shrinkTo(minCapacity: Int) {
        map.shrinkTo(minCapacity)
    }

    // Remove the value and return true if it was present.
    public fun remove(value: T): Boolean = shiftRemove(value)

    // Remove the value by swapping in the last value.
    public fun swapRemove(value: T): Boolean = map.swapRemove(value) != null

    // Remove the value by shifting following values down.
    public fun shiftRemove(value: T): Boolean = map.shiftRemove(value) != null

    // Remove and return the value by swapping in the last value.
    public fun swapTake(value: T): T? = map.swapRemoveEntry(value)?.first

    // Remove and return the value by shifting following values down.
    public fun shiftTake(value: T): T? = map.shiftRemoveEntry(value)?.first

    // Remove the value and return it with the index it had.
    public fun swapRemoveFull(value: T): Pair<Int, T>? =
        map.swapRemoveFull(value)?.let { (index, stored, _) -> index to stored }

    // Remove the value and return it with the index it had.
    public fun shiftRemoveFull(value: T): Pair<Int, T>? =
        map.shiftRemoveFull(value)?.let { (index, stored, _) -> index to stored }

    // Remove the last value.
    public fun pop(): T? = map.pop()?.first

    // Remove the last value if the predicate accepts it.
    public fun popIf(predicate: (T) -> Boolean): T? {
        val last = last() ?: return null
        return if (predicate(last)) pop() else null
    }

    // Keep only the values accepted by the predicate.
    public fun retain(keep: (T) -> Boolean) {
        map.retain { key, _ -> keep(key) }
    }

    // Sort the set's values by their default ordering.
    public fun sort(comparator: Comparator<in T>) {
        map.sortKeys(comparator)
    }

    // Sort the set's values in place using the comparison function.
    public fun sortBy(compare: (T, T) -> Int) {
        map.sortBy { left, _, right, _ -> compare(left, right) }
    }

    // Sort the set's values and return an ordered list of the result.
    public fun sortedBy(compare: (T, T) -> Int): List<T> =
        map.sortedBy { left, _, right, _ -> compare(left, right) }.map { it.first }

    // Sort the set's values in place using a key extraction function.
    public fun <K> sortByKey(selector: (T) -> K, comparator: Comparator<in K>) {
        map.sortByKey({ key, _ -> selector(key) }, comparator)
    }

    public fun sortUnstable(comparator: Comparator<in T>) {
        sort(comparator)
    }

    public fun sortUnstableBy(compare: (T, T) -> Int) {
        sortBy(compare)
    }

    public fun sortedUnstableBy(compare: (T, T) -> Int): List<T> =
        sortedBy(compare)

    public fun <K> sortUnstableByKey(selector: (T) -> K, comparator: Comparator<in K>) {
        sortByKey(selector, comparator)
    }

    public fun <K> sortByCachedKey(selector: (T) -> K, comparator: Comparator<in K>) {
        sortByKey(selector, comparator)
    }

    // Search over a sorted set for a value.
    public fun binarySearch(value: T, comparator: Comparator<in T>): SearchResult =
        map.binarySearchKeys(value, comparator)

    // Search over a sorted set with a comparator function.
    public fun binarySearchBy(compare: (T) -> Int): SearchResult =
        map.binarySearchBy { key, _ -> compare(key) }

    // Search over a sorted set with a key extraction function.
    public fun <K> binarySearchByKey(
        key: K,
        selector: (T) -> K,
        comparator: Comparator<in K>,
    ): SearchResult =
        map.binarySearchByKey(key, { value, _ -> selector(value) }, comparator)

    // Return true if the values of this set are sorted.
    public fun isSorted(comparator: Comparator<in T>): Boolean = map.isSorted(comparator)

    // Return true if this set is sorted by the given adjacent-value predicate.
    public fun isSortedBy(inOrder: (T, T) -> Boolean): Boolean =
        map.isSortedBy { left, _, right, _ -> inOrder(left, right) }

    // Return true if this set is sorted using the given sort-key function.
    public fun <K> isSortedByKey(selector: (T) -> K, comparator: Comparator<in K>): Boolean =
        map.isSortedByKey({ key, _ -> selector(key) }, comparator)

    // Return the partition point according to the predicate.
    public fun partitionPoint(predicate: (T) -> Boolean): Int =
        map.partitionPoint { key, _ -> predicate(key) }

    // Reverse the order of the set's values in place.
    public fun reverse() {
        map.reverse()
    }

    // Return a list of values in the given index range.
    public fun getRange(start: Int, endExclusive: Int): List<T>? =
        map.getRange(start, endExclusive)?.keys()

    // Remove the value by index by swapping in the last value.
    public fun swapRemoveIndex(index: Int): T? = map.swapRemoveIndex(index)?.first

    // Remove the value by index by shifting following values down.
    public fun shiftRemoveIndex(index: Int): T? = map.shiftRemoveIndex(index)?.first

    // Move the position of a value from one index to another.
    public fun moveIndex(from: Int, to: Int) {
        map.moveIndex(from, to)
    }

    // Swap the positions of two values in the set.
    public fun swapIndices(a: Int, b: Int) {
        map.swapIndices(a, b)
    }

    // Drain a range of values and return them in removal order.
    public fun drain(start: Int = 0, endExclusive: Int = len()): List<T> =
        map.drain(start, endExclusive).map { it.first }

    // Remove values accepted by a predicate and return them in original order.
    public fun extractIf(predicate: (T) -> Boolean): List<T> =
        map.extractIf { key, _ -> predicate(key) }.map { it.first }

    // Split off values starting at index.
    public fun splitOff(index: Int): IndexSet<T> =
        IndexSet(map.splitOff(index))

    // Replace a range of values and return the removed values.
    public fun splice(start: Int, endExclusive: Int, replacement: Iterable<T>): List<T> =
        map.splice(start, endExclusive, replacement.map { it to Unit }).map { it.first }

    // Move all values from another set into this one, leaving the other empty.
    public fun append(other: IndexSet<T>) {
        extend(other.asList())
        other.clear()
    }

    // Extend the set with ordered values.
    public fun extend(values: Iterable<T>) {
        for (value in values) {
            insert(value)
        }
    }

    // Return true if self has no values in common with other.
    public fun isDisjoint(other: IndexSet<T>): Boolean =
        if (len() <= other.len()) asList().all { !other.contains(it) } else other.asList().all { !contains(it) }

    // Return true if all values of self are contained in other.
    public fun isSubset(other: IndexSet<T>): Boolean =
        len() <= other.len() && asList().all { other.contains(it) }

    // Return true if all values of other are contained in self.
    public fun isSuperset(other: IndexSet<T>): Boolean = other.isSubset(this)

    // Return values in self but not in other, preserving self order.
    public fun difference(other: IndexSet<T>): List<T> =
        asList().filter { !other.contains(it) }

    // Return values in both sets, preserving self order.
    public fun intersection(other: IndexSet<T>): List<T> =
        asList().filter { other.contains(it) }

    // Return values in either set but not both, preserving set order.
    public fun symmetricDifference(other: IndexSet<T>): List<T> =
        difference(other) + other.difference(this)

    // Return all values in self, followed by values unique to other.
    public fun union(other: IndexSet<T>): List<T> =
        asList() + other.asList().filter { !contains(it) }

    public fun eq(other: IndexSet<T>): Boolean = this == other

    override fun equals(other: Any?): Boolean =
        other is IndexSet<*> && len() == other.len() && asList().all { other.containsAny(it) }

    override fun hashCode(): Int = asList().fold(0) { hash, value -> hash + value.hashCode() }

    override fun toString(): String = asList().toString()

    private fun containsAny(value: Any?): Boolean =
        map.keys().any { it == value }
}
