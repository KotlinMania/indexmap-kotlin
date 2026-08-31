// port-lint: source rayon/mod.rs
package io.github.kotlinmania.indexmap.rayon

// This form of intermediate collection is also how Rayon collects `HashMap`.
// Note that the order will also be preserved!
internal fun <I, T> collect(iter: I): MutableList<MutableList<T>> where I : Iterable<T> {
    val result = mutableListOf<MutableList<T>>()
    val current = mutableListOf<T>()
    for (item in iter) {
        current.add(item)
    }
    result.add(current)
    return result
}
