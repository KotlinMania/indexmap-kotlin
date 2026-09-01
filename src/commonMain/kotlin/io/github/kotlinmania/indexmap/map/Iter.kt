@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source map/iter.rs

package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.Bucket
import io.github.kotlinmania.indexmap.IndexMap
import kotlin.native.HiddenFromObjC

/**
 * An iterator over the entries of an IndexMap.
 */
@HiddenFromObjC
public class Iter<K, V> internal constructor(
    private val entries: List<Bucket<K, V>>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<Pair<K, V>> {
    public interface Item

    public interface Output

    public companion object {
        internal fun <K, V> new(entries: List<Bucket<K, V>>): Iter<K, V> = Iter(entries)

        public fun <K, V> default(): Iter<K, V> = Iter(emptyList())
    }

    /**
     * Returns a slice of the remaining entries in the iterator.
     */
    public fun asSlice(): Slice<K, V> =
        if (index < backIndex) {
            Slice.fromEntries(entries.subList(index, backIndex).toMutableList())
        } else {
            Slice.new()
        }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    public fun clone(): Iter<K, V> = Iter(entries, index, backIndex)

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): Pair<K, V> {
        if (!hasNext()) throw NoSuchElementException()
        val entry = entries[index]
        index += 1
        return entry.keyValue()
    }

    public fun nextBack(): Pair<K, V>? {
        if (index >= backIndex) return null
        backIndex -= 1
        return entries[backIndex].keyValue()
    }

    public fun <R> fold(initial: R, operation: (R, Pair<K, V>) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, Pair<K, V>) -> R): R {
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
 * A mutable iterator over the entries of an IndexMap.
 */
@HiddenFromObjC
public class IterMut<K, V> internal constructor(
    private val entries: MutableList<Bucket<K, V>>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<Pair<K, V>> {
    public companion object {
        internal fun <K, V> new(entries: MutableList<Bucket<K, V>>): IterMut<K, V> = IterMut(entries)

        public fun <K, V> default(): IterMut<K, V> = IterMut(mutableListOf())
    }

    /**
     * Returns a slice of the remaining entries in the iterator.
     */
    public fun asSlice(): Slice<K, V> =
        if (index < backIndex) {
            Slice.fromEntries(entries.subList(index, backIndex))
        } else {
            Slice.new()
        }

    public fun intoSlice(): Slice<K, V> = asSlice()

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): Pair<K, V> {
        if (!hasNext()) throw NoSuchElementException()
        val entry = entries[index]
        index += 1
        return entry.keyValue()
    }

    public fun nextBack(): Pair<K, V>? {
        if (index >= backIndex) return null
        backIndex -= 1
        return entries[backIndex].keyValue()
    }

    public fun <R> fold(initial: R, operation: (R, Pair<K, V>) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, Pair<K, V>) -> R): R {
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
 * A mutable iterator over the entries of an IndexMap with key mutations.
 */
@HiddenFromObjC
public class IterMut2<K, V> internal constructor(
    private val entries: MutableList<Bucket<K, V>>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<Pair<K, V>> {
    public companion object {
        internal fun <K, V> new(entries: MutableList<Bucket<K, V>>): IterMut2<K, V> = IterMut2(entries)

        public fun <K, V> default(): IterMut2<K, V> = IterMut2(mutableListOf())
    }

    /**
     * Returns a slice of the remaining entries in the iterator.
     */
    public fun asSlice(): Slice<K, V> =
        if (index < backIndex) {
            Slice.fromEntries(entries.subList(index, backIndex))
        } else {
            Slice.new()
        }

    public fun intoSlice(): Slice<K, V> = asSlice()

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): Pair<K, V> {
        if (!hasNext()) throw NoSuchElementException()
        val entry = entries[index]
        index += 1
        return entry.keyValue()
    }

    public fun nextBack(): Pair<K, V>? {
        if (index >= backIndex) return null
        backIndex -= 1
        return entries[backIndex].keyValue()
    }

    public fun <R> fold(initial: R, operation: (R, Pair<K, V>) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, Pair<K, V>) -> R): R {
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
 * An owning iterator over the entries of an IndexMap.
 */
@HiddenFromObjC
public class IntoIter<K, V> internal constructor(
    private val entries: List<Bucket<K, V>>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<Pair<K, V>> {
    public companion object {
        internal fun <K, V> new(entries: List<Bucket<K, V>>): IntoIter<K, V> = IntoIter(entries)

        public fun <K, V> default(): IntoIter<K, V> = IntoIter(emptyList())
    }

    /**
     * Returns a slice of the remaining entries in the iterator.
     */
    public fun asSlice(): Slice<K, V> =
        if (index < backIndex) {
            Slice.fromEntries(entries.subList(index, backIndex).toMutableList())
        } else {
            Slice.new()
        }

    public fun asMutSlice(): Slice<K, V> = asSlice()

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    public fun clone(): IntoIter<K, V> = IntoIter(entries, index, backIndex)

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): Pair<K, V> {
        if (!hasNext()) throw NoSuchElementException()
        val entry = entries[index]
        index += 1
        return entry.keyValue()
    }

    public fun nextBack(): Pair<K, V>? {
        if (index >= backIndex) return null
        backIndex -= 1
        return entries[backIndex].keyValue()
    }

    public fun <R> fold(initial: R, operation: (R, Pair<K, V>) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, Pair<K, V>) -> R): R {
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
 * A draining iterator over the entries of an IndexMap.
 */
@HiddenFromObjC
public class Drain<K, V> internal constructor(
    private val drained: List<Bucket<K, V>>,
    private var index: Int = 0,
    private var backIndex: Int = drained.size,
) : Iterator<Pair<K, V>> {
    /**
     * Returns a slice of the remaining entries in the iterator.
     */
    public fun asSlice(): Slice<K, V> =
        if (index < backIndex) {
            Slice.fromEntries(drained.subList(index, backIndex).toMutableList())
        } else {
            Slice.new()
        }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): Pair<K, V> {
        if (!hasNext()) throw NoSuchElementException()
        val entry = drained[index]
        index += 1
        return entry.keyValue()
    }

    public fun nextBack(): Pair<K, V>? {
        if (index >= backIndex) return null
        backIndex -= 1
        return drained[backIndex].keyValue()
    }

    public fun fmt(): String = toString()

    override fun toString(): String = asSlice().toList().toString()
}

/**
 * An iterator over the keys of an IndexMap.
 */
@HiddenFromObjC
public class Keys<K, V> internal constructor(
    private val entries: List<Bucket<K, V>>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<K> {
    public companion object {
        internal fun <K, V> new(entries: List<Bucket<K, V>>): Keys<K, V> = Keys(entries)

        public fun <K, V> default(): Keys<K, V> = Keys(emptyList())
    }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    public fun clone(): Keys<K, V> = Keys(entries, index, backIndex)

    public operator fun get(i: Int): K {
        val target = index + i
        require(target in index until backIndex) { "Index $i out of bounds for keys of length ${len()}" }
        return entries[target].key
    }

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): K {
        if (!hasNext()) throw NoSuchElementException()
        val entry = entries[index]
        index += 1
        return entry.key
    }

    public fun nextBack(): K? {
        if (index >= backIndex) return null
        backIndex -= 1
        return entries[backIndex].key
    }

    public fun <R> fold(initial: R, operation: (R, K) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, K) -> R): R {
        var accumulator = initial
        while (true) {
            val item = nextBack() ?: break
            accumulator = operation(accumulator, item)
        }
        return accumulator
    }

    public fun fmt(): String = toString()

    override fun toString(): String {
        val list = (index until backIndex).map { entries[it].key }
        return list.toString()
    }
}

/**
 * An owning iterator over the keys of an IndexMap.
 */
@HiddenFromObjC
public class IntoKeys<K, V> internal constructor(
    private val keys: List<K>,
    private var index: Int = 0,
    private var backIndex: Int = keys.size,
) : Iterator<K> {
    public companion object {
        public fun <K, V> new(keys: List<K>): IntoKeys<K, V> = IntoKeys(keys)

        public fun <K, V> default(): IntoKeys<K, V> = IntoKeys(emptyList())
    }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    public fun clone(): IntoKeys<K, V> = IntoKeys(keys, index, backIndex)

    public operator fun get(i: Int): K {
        val target = index + i
        require(target in index until backIndex) { "Index $i out of bounds for into_keys of length ${len()}" }
        return keys[target]
    }

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): K {
        if (!hasNext()) throw NoSuchElementException()
        val key = keys[index]
        index += 1
        return key
    }

    public fun nextBack(): K? {
        if (index >= backIndex) return null
        backIndex -= 1
        return keys[backIndex]
    }

    public fun <R> fold(initial: R, operation: (R, K) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, K) -> R): R {
        var accumulator = initial
        while (true) {
            val item = nextBack() ?: break
            accumulator = operation(accumulator, item)
        }
        return accumulator
    }

    public fun fmt(): String = toString()

    override fun toString(): String {
        val list = (index until backIndex).map { keys[it] }
        return list.toString()
    }
}

/**
 * An iterator over the values of an IndexMap.
 */
@HiddenFromObjC
public class Values<K, V> internal constructor(
    private val entries: List<Bucket<K, V>>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<V> {
    public companion object {
        internal fun <K, V> new(entries: List<Bucket<K, V>>): Values<K, V> = Values(entries)

        public fun <K, V> default(): Values<K, V> = Values(emptyList())
    }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    public fun clone(): Values<K, V> = Values(entries, index, backIndex)

    public operator fun get(i: Int): V {
        val target = index + i
        require(target in index until backIndex) { "Index $i out of bounds for values of length ${len()}" }
        return entries[target].value
    }

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): V {
        if (!hasNext()) throw NoSuchElementException()
        val entry = entries[index]
        index += 1
        return entry.value
    }

    public fun nextBack(): V? {
        if (index >= backIndex) return null
        backIndex -= 1
        return entries[backIndex].value
    }

    public fun <R> fold(initial: R, operation: (R, V) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, V) -> R): R {
        var accumulator = initial
        while (true) {
            val item = nextBack() ?: break
            accumulator = operation(accumulator, item)
        }
        return accumulator
    }

    public fun fmt(): String = toString()

    override fun toString(): String {
        val list = (index until backIndex).map { entries[it].value }
        return list.toString()
    }
}

/**
 * A mutable iterator over the values of an IndexMap.
 */
@HiddenFromObjC
public class ValuesMut<K, V> internal constructor(
    private val entries: MutableList<Bucket<K, V>>,
    private var index: Int = 0,
    private var backIndex: Int = entries.size,
) : Iterator<V> {
    public companion object {
        internal fun <K, V> new(entries: MutableList<Bucket<K, V>>): ValuesMut<K, V> = ValuesMut(entries)

        public fun <K, V> default(): ValuesMut<K, V> = ValuesMut(mutableListOf())
    }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    public operator fun get(i: Int): V {
        val target = index + i
        require(target in index until backIndex) { "Index $i out of bounds for values_mut of length ${len()}" }
        return entries[target].value
    }

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): V {
        if (!hasNext()) throw NoSuchElementException()
        val entry = entries[index]
        index += 1
        return entry.value
    }

    public fun nextBack(): V? {
        if (index >= backIndex) return null
        backIndex -= 1
        return entries[backIndex].value
    }

    public fun <R> fold(initial: R, operation: (R, V) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, V) -> R): R {
        var accumulator = initial
        while (true) {
            val item = nextBack() ?: break
            accumulator = operation(accumulator, item)
        }
        return accumulator
    }

    public fun fmt(): String = toString()

    override fun toString(): String {
        val list = (index until backIndex).map { entries[it].value }
        return list.toString()
    }
}

/**
 * An owning iterator over the values of an IndexMap.
 */
@HiddenFromObjC
public class IntoValues<K, V> internal constructor(
    private val values: List<V>,
    private var index: Int = 0,
    private var backIndex: Int = values.size,
) : Iterator<V> {
    public companion object {
        public fun <K, V> new(values: List<V>): IntoValues<K, V> = IntoValues(values)

        public fun <K, V> default(): IntoValues<K, V> = IntoValues(emptyList())
    }

    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to len()

    public fun clone(): IntoValues<K, V> = IntoValues(values, index, backIndex)

    public operator fun get(i: Int): V {
        val target = index + i
        require(target in index until backIndex) { "Index $i out of bounds for into_values of length ${len()}" }
        return values[target]
    }

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): V {
        if (!hasNext()) throw NoSuchElementException()
        val value = values[index]
        index += 1
        return value
    }

    public fun nextBack(): V? {
        if (index >= backIndex) return null
        backIndex -= 1
        return values[backIndex]
    }

    public fun <R> fold(initial: R, operation: (R, V) -> R): R {
        var accumulator = initial
        while (hasNext()) {
            accumulator = operation(accumulator, next())
        }
        return accumulator
    }

    public fun <R> rfold(initial: R, operation: (R, V) -> R): R {
        var accumulator = initial
        while (true) {
            val item = nextBack() ?: break
            accumulator = operation(accumulator, item)
        }
        return accumulator
    }

    public fun fmt(): String = toString()

    override fun toString(): String {
        val list = (index until backIndex).map { values[it] }
        return list.toString()
    }
}

/**
 * A splicing iterator for IndexMap.
 */
@HiddenFromObjC
public class Splice<K, V> internal constructor(
    private val map: IndexMap<K, V>,
    private val drained: List<Bucket<K, V>>,
    private val replacement: Iterator<Pair<K, V>>,
    private var index: Int = 0,
    private var backIndex: Int = drained.size,
) : Iterator<Pair<K, V>> {
    public fun len(): Int = (backIndex - index).coerceAtLeast(0)

    public fun isEmpty(): Boolean = len() == 0

    public fun sizeHint(): Pair<Int, Int?> = len() to null

    override fun hasNext(): Boolean = index < backIndex

    override fun next(): Pair<K, V> {
        if (!hasNext()) throw NoSuchElementException()
        val entry = drained[index]
        index += 1
        return entry.keyValue()
    }

    public fun nextBack(): Pair<K, V>? {
        if (index >= backIndex) return null
        backIndex -= 1
        return drained[backIndex].keyValue()
    }

    public fun close() {
        while (replacement.hasNext()) {
            val (key, value) = replacement.next()
            map.insert(key, value)
        }
    }

    public fun fmt(): String = toString()
}

/**
 * An extracting iterator for IndexMap.
 */
@HiddenFromObjC
public class ExtractIf<K, V> internal constructor(
    private val map: IndexMap<K, V>,
    private val predicate: (K, V) -> Boolean,
) : Iterator<Pair<K, V>> {
    private val extracted: Iterator<Pair<K, V>> by lazy {
        map.extractIf(predicate).iterator()
    }

    public fun sizeHint(): Pair<Int, Int?> = 0 to null

    public fun fmt(): String = toString()

    override fun hasNext(): Boolean = extracted.hasNext()

    override fun next(): Pair<K, V> {
        if (!hasNext()) throw NoSuchElementException()
        return extracted.next()
    }
}
