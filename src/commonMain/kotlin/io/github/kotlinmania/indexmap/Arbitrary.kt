// port-lint: source arbitrary.rs
package io.github.kotlinmania.indexmap

/**
 * An unstructured data source for generating arbitrary data instances.
 */
public interface Unstructured {
    public fun <T> arbitraryIter(generator: (Unstructured) -> T): Sequence<T>
    public fun <T> arbitraryTakeRestIter(generator: (Unstructured) -> T): Sequence<T>
}

/**
 * A trait for types that can be generated from unstructured data.
 */
public interface Arbitrary<T> {
    public fun arbitrary(u: Unstructured): T
    public fun arbitraryTakeRest(u: Unstructured): T
}

/**
 * Generator context for property-based testing.
 */
public interface Gen {
    public val size: Int
}

/**
 * Quickcheck arbitrary generator and shrinking interface.
 */
public interface QuickcheckArbitrary<T> {
    public fun arbitrary(g: Gen): T
    public fun shrink(value: T): Sequence<T>
}

/**
 * Constructs an [IndexMap] from an arbitrary unstructured source.
 */
public fun <K, V> IndexMap.Companion.arbitrary(
    u: Unstructured,
    keyGen: (Unstructured) -> K,
    valueGen: (Unstructured) -> V,
): IndexMap<K, V> {
    val map = IndexMap<K, V>()
    for (entry in u.arbitraryIter { keyGen(it) to valueGen(it) }) {
        map.insert(entry.first, entry.second)
    }
    return map
}

/**
 * Constructs an [IndexMap] by consuming the rest of an arbitrary unstructured source.
 */
public fun <K, V> IndexMap.Companion.arbitraryTakeRest(
    u: Unstructured,
    keyGen: (Unstructured) -> K,
    valueGen: (Unstructured) -> V,
): IndexMap<K, V> {
    val map = IndexMap<K, V>()
    for (entry in u.arbitraryTakeRestIter { keyGen(it) to valueGen(it) }) {
        map.insert(entry.first, entry.second)
    }
    return map
}

/**
 * Constructs an [IndexSet] from an arbitrary unstructured source.
 */
public fun <T> IndexSet.Companion.arbitrary(
    u: Unstructured,
    itemGen: (Unstructured) -> T,
): IndexSet<T> {
    val set = IndexSet<T>()
    for (item in u.arbitraryIter(itemGen)) {
        set.insert(item)
    }
    return set
}

/**
 * Constructs an [IndexSet] by consuming the rest of an arbitrary unstructured source.
 */
public fun <T> IndexSet.Companion.arbitraryTakeRest(
    u: Unstructured,
    itemGen: (Unstructured) -> T,
): IndexSet<T> {
    val set = IndexSet<T>()
    for (item in u.arbitraryTakeRestIter(itemGen)) {
        set.insert(item)
    }
    return set
}
