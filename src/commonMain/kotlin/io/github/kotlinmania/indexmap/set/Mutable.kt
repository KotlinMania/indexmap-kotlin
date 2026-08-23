@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source set/mutable.rs

package io.github.kotlinmania.indexmap.set

import kotlin.native.HiddenFromObjC

// Opt-in mutable access to IndexSet values.
@HiddenFromObjC
public interface MutableValues<T> {
    public fun getFullMut2(value: T): Pair<Int, T>?

    public fun getIndexMut2(index: Int): T?

    public fun retain2(keep: (T) -> Boolean)
}
