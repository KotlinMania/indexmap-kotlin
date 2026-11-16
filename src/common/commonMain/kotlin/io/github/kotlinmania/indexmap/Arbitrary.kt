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
 * Shrinks an [IndexMap] into smaller candidate maps for property-based testing.
 */
public fun <K, V> IndexMap<K, V>.shrink(): Sequence<IndexMap<K, V>> = emptySequence()

/**
 * Shrinks an [IndexSet] into smaller candidate sets for property-based testing.
 */
public fun <T> IndexSet<T>.shrink(): Sequence<IndexSet<T>> = emptySequence()
