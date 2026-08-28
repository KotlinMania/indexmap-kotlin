@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source set/iter.rs

package io.github.kotlinmania.indexmap.set

import io.github.kotlinmania.indexmap.Bucket
import io.github.kotlinmania.indexmap.IndexSet
import kotlin.native.HiddenFromObjC

/**
 * An iterator over the items of an IndexSet.
 */
@HiddenFromObjC
public class Iter<T> internal constructor(
    internal val entries: List<Bucket<T, Unit>>,
    internal var index: Int = 0,
    internal var backIndex: Int = entries.size,
) : Iterator<T> {
    public interface Item

    public companion object {
        internal fun <T> new(entries: List<Bucket<T, Unit>>): Iter<T> = Iter(entries)

        public fun <T> default(): Iter<T> = Iter(emptyList())
    }

    /**
     * Returns a slice of the remaining entries in the iterator.
     */
    public fun asSlice(): Slice<T> =
        if (index < backIndex) {
            Slice.fromEntries(entries.subList(index, backIndex).toMutableList())
        } else {
            Slice.new()
        }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    public fun clone(): Iter<T> = Iter(entries, index, backIndex)

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        val entry = entries[index]
        index += 1
        return entry.key
    }

    public fun nextBack(): T? {
        if (index >= backIndex) return null
        backIndex -= 1
        return entries[backIndex].key
    }

    public fun <R> fold(initial: R, operation: (R, T) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, T) -> R): R {
        var accumulator = initial
        while (true) {
            val item = nextBack() ?: break
            accumulator = operation(accumulator, item)
        }
        return accumulator
    }

    public fun fmt(): String = toString()

    override fun toString(): String = asSlice().toList().toString()
}

/**
 * An owning iterator over the items of an IndexSet.
 */
@HiddenFromObjC
public class IntoIter<T> internal constructor(
    private val entries: List<Bucket<T, Unit>>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<T> {
    public companion object {
        internal fun <T> new(entries: List<Bucket<T, Unit>>): IntoIter<T> = IntoIter(entries)

        public fun <T> default(): IntoIter<T> = IntoIter(emptyList())
    }

    /**
     * Returns a slice of the remaining entries in the iterator.
     */
    public fun asSlice(): Slice<T> =
        if (index < backIndex) {
            Slice.fromEntries(entries.subList(index, backIndex).toMutableList())
        } else {
            Slice.new()
        }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    public fun clone(): IntoIter<T> = IntoIter(entries, index, backIndex)

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        val entry = entries[index]
        index += 1
        return entry.key
    }

    public fun nextBack(): T? {
        if (index >= backIndex) return null
        backIndex -= 1
        return entries[backIndex].key
    }

    public fun <R> fold(initial: R, operation: (R, T) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, T) -> R): R {
        var accumulator = initial
        while (true) {
            val item = nextBack() ?: break
            accumulator = operation(accumulator, item)
        }
        return accumulator
    }

    public fun fmt(): String = toString()

    override fun toString(): String = asSlice().toList().toString()
}

/**
 * A draining iterator over the items of an IndexSet.
 */
@HiddenFromObjC
public class Drain<T> internal constructor(
    private val drained: List<Bucket<T, Unit>>,
    private var index: Int = 0,
    private var backIndex: Int = drained.size,
) : Iterator<T> {
    /**
     * Returns a slice of the remaining entries in the iterator.
     */
    public fun asSlice(): Slice<T> =
        if (index < backIndex) {
            Slice.fromEntries(drained.subList(index, backIndex).toMutableList())
        } else {
            Slice.new()
        }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        val entry = drained[index]
        index += 1
        return entry.key
    }

    public fun nextBack(): T? {
        if (index >= backIndex) return null
        backIndex -= 1
        return drained[backIndex].key
    }

    public fun fmt(): String = toString()

    override fun toString(): String = asSlice().toList().toString()
}

/**
 * A lazy iterator producing elements in the difference of IndexSets.
 */
@HiddenFromObjC
public class Difference<T> internal constructor(
    private val entries: List<Bucket<T, Unit>>,
    private val other: IndexSet<T>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<T> {
    public constructor(iter: Iter<T>, other: IndexSet<T>) : this(
        iter.entries,
        other,
        iter.index,
        iter.backIndex,
    )

    public fun clone(): Difference<T> = Difference(entries, other, index, backIndex)

    override fun hasNext(): Boolean {
        var i = index
        while (i < backIndex) {
            if (!other.contains(entries[i].key)) {
                return true
            }
            i += 1
        }
        return false
    }

    override fun next(): T {
        while (index < backIndex) {
            val key = entries[index].key
            index += 1
            if (!other.contains(key)) {
                return key
            }
        }
        throw NoSuchElementException()
    }

    public fun nextBack(): T? {
        while (index < backIndex) {
            backIndex -= 1
            val key = entries[backIndex].key
            if (!other.contains(key)) {
                return key
            }
        }
        return null
    }

    public fun fmt(): String = toString()
}

/**
 * A lazy iterator producing elements in the intersection of IndexSets.
 */
@HiddenFromObjC
public class Intersection<T> internal constructor(
    private val entries: List<Bucket<T, Unit>>,
    private val other: IndexSet<T>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<T> {
    public constructor(iter: Iter<T>, other: IndexSet<T>) : this(
        iter.entries,
        other,
        iter.index,
        iter.backIndex,
    )

    public fun clone(): Intersection<T> = Intersection(entries, other, index, backIndex)

    override fun hasNext(): Boolean {
        var i = index
        while (i < backIndex) {
            if (other.contains(entries[i].key)) {
                return true
            }
            i += 1
        }
        return false
    }

    override fun next(): T {
        while (index < backIndex) {
            val key = entries[index].key
            index += 1
            if (other.contains(key)) {
                return key
            }
        }
        throw NoSuchElementException()
    }

    public fun nextBack(): T? {
        while (index < backIndex) {
            backIndex -= 1
            val key = entries[backIndex].key
            if (other.contains(key)) {
                return key
            }
        }
        return null
    }

    public fun fmt(): String = toString()
}

/**
 * A lazy iterator producing elements in the symmetric difference of IndexSets.
 */
@HiddenFromObjC
public class SymmetricDifference<T> internal constructor(
    private val diff1: Difference<T>,
    private val diff2: Difference<T>,
) : Iterator<T> {
    public fun clone(): SymmetricDifference<T> =
        SymmetricDifference(diff1.clone(), diff2.clone())

    override fun hasNext(): Boolean = diff1.hasNext() || diff2.hasNext()

    override fun next(): T {
        if (diff1.hasNext()) return diff1.next()
        if (diff2.hasNext()) return diff2.next()
        throw NoSuchElementException()
    }

    public fun nextBack(): T? {
        val back2 = diff2.nextBack()
        if (back2 != null) return back2
        return diff1.nextBack()
    }

    public fun fmt(): String = toString()
}

/**
 * A lazy iterator producing elements in the union of IndexSets.
 */
@HiddenFromObjC
public class Union<T> internal constructor(
    private val iter1: Iter<T>,
    private val diff2: Difference<T>,
) : Iterator<T> {
    public fun clone(): Union<T> = Union(iter1.clone(), diff2.clone())

    override fun hasNext(): Boolean = iter1.hasNext() || diff2.hasNext()

    override fun next(): T {
        if (iter1.hasNext()) return iter1.next()
        if (diff2.hasNext()) return diff2.next()
        throw NoSuchElementException()
    }

    public fun nextBack(): T? {
        val back2 = diff2.nextBack()
        if (back2 != null) return back2
        return iter1.nextBack()
    }

    public fun fmt(): String = toString()
}

/**
 * A splicing iterator for IndexSet.
 */
@HiddenFromObjC
public class Splice<T> internal constructor(
    private val set: IndexSet<T>,
    private val drained: List<Bucket<T, Unit>>,
    private val replacement: Iterator<T>,
    private var index: Int = 0,
    private var backIndex: Int = drained.size,
) : Iterator<T> {
    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to null

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        val entry = drained[index]
        index += 1
        return entry.key
    }

    public fun nextBack(): T? {
        if (index >= backIndex) return null
        backIndex -= 1
        return drained[backIndex].key
    }

    public fun close() {
        while (replacement.hasNext()) {
            val key = replacement.next()
            set.insert(key)
        }
    }

    public fun fmt(): String = toString()
}

/**
 * An extracting iterator for IndexSet.
 */
@HiddenFromObjC
public class ExtractIf<T> internal constructor(
    private val set: IndexSet<T>,
    private val predicate: (T) -> Boolean,
) : Iterator<T> {
    private val extracted: Iterator<T> by lazy {
        set.extractIf(predicate).iterator()
    }

    public fun sizeHint(): Pair<Int, Int?> = 0 to null

    public fun fmt(): String = toString()

    override fun hasNext(): Boolean = extracted.hasNext()

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        return extracted.next()
    }
}

internal class UnitValue<I : Iterator<*>>(
    private val iter: I,
) : Iterator<Pair<Any?, Unit>> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): Pair<Any?, Unit> = iter.next() to Unit
}

