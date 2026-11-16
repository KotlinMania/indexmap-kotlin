// port-lint: source rayon/set.rs
package io.github.kotlinmania.indexmap.rayon

import io.github.kotlinmania.indexmap.IndexSet
import io.github.kotlinmania.indexmap.set.Slice
import io.github.kotlinmania.indexmap.sort

/**
 * Parallel owning iterator over the items of an [IndexSet].
 */
public class IntoParSetIter<T>(
    public val entries: List<T>,
) : Sequence<T> {
    override fun iterator(): Iterator<T> = entries.iterator()
}

/**
 * Parallel iterator over reference items of an [IndexSet].
 */
public class ParSetIter<T>(
    public val entries: List<T>,
) : Sequence<T> {
    override fun iterator(): Iterator<T> = entries.iterator()
}

/**
 * Creates a parallel owning iterator for [IndexSet].
 */
public fun <T> IndexSet<T>.intoParIter(): IntoParSetIter<T> =
    IntoParSetIter(toList())

/**
 * Creates a parallel iterator for [IndexSet].
 */
public fun <T> IndexSet<T>.parIter(): ParSetIter<T> =
    ParSetIter(toList())

/**
 * Creates a parallel iterator for [Slice].
 */
public fun <T> Slice<T>.parIter(): ParSetIter<T> =
    ParSetIter(toList())

/**
 * Sort the set's elements in parallel.
 */
public fun <T : Comparable<T>> IndexSet<T>.parSort() {
    sort()
}

/**
 * Sort the set's elements in parallel with a comparator.
 */
public fun <T> IndexSet<T>.parSortBy(comparator: (T, T) -> Int) {
    sortBy(comparator)
}
