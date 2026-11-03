// port-lint: source rayon/map.rs
package io.github.kotlinmania.indexmap.rayon

import io.github.kotlinmania.indexmap.IndexMap
import io.github.kotlinmania.indexmap.map.Slice
import io.github.kotlinmania.indexmap.sortKeys

/**
 * Parallel owning iterator over the entries of an [IndexMap].
 */
public class IntoParIter<K, V>(
    public val entries: List<Pair<K, V>>,
) : Sequence<Pair<K, V>> {
    override fun iterator(): Iterator<Pair<K, V>> = entries.iterator()
}

/**
 * Parallel iterator over reference entries of an [IndexMap].
 */
public class ParIter<K, V>(
    public val entries: List<Pair<K, V>>,
) : Sequence<Pair<K, V>> {
    override fun iterator(): Iterator<Pair<K, V>> = entries.iterator()
}

/**
 * Parallel iterator over keys of an [IndexMap].
 */
public class ParKeys<K>(
    public val keys: List<K>,
) : Sequence<K> {
    override fun iterator(): Iterator<K> = keys.iterator()
}

/**
 * Parallel iterator over values of an [IndexMap].
 */
public class ParValues<V>(
    public val values: List<V>,
) : Sequence<V> {
    override fun iterator(): Iterator<V> = values.iterator()
}

/**
 * Creates a parallel owning iterator for [IndexMap].
 */
public fun <K, V> IndexMap<K, V>.intoParIter(): IntoParIter<K, V> =
    IntoParIter(toList())

/**
 * Creates a parallel iterator for [IndexMap].
 */
public fun <K, V> IndexMap<K, V>.parIter(): ParIter<K, V> =
    ParIter(toList())

/**
 * Creates a parallel keys iterator for [IndexMap].
 */
public fun <K, V> IndexMap<K, V>.parKeys(): ParKeys<K> =
    ParKeys(keys())

/**
 * Creates a parallel values iterator for [IndexMap].
 */
public fun <K, V> IndexMap<K, V>.parValues(): ParValues<V> =
    ParValues(values())

/**
 * Creates a parallel iterator for [Slice].
 */
public fun <K, V> Slice<K, V>.parIter(): ParIter<K, V> =
    ParIter(iterator().asSequence().toList())

/**
 * Sort the map's key-value pairs in parallel with a comparator.
 */
public fun <K, V> IndexMap<K, V>.parSortBy(comparator: (K, V, K, V) -> Int) {
    sortBy(comparator)
}

/**
 * Sort the map's key-value pairs in parallel by key.
 */
public fun <K : Comparable<K>, V> IndexMap<K, V>.parSortKeys() {
    sortKeys()
}
