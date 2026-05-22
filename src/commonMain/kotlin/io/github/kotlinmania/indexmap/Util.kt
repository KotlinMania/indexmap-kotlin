// port-lint: source src/util.rs
package io.github.kotlinmania.indexmap

internal sealed interface Bound<out T> {
    data object Unbounded : Bound<Nothing>
    data class Included<T>(val value: T) : Bound<T>
    data class Excluded<T>(val value: T) : Bound<T>
}

internal interface RangeBounds<T> {
    fun startBound(): Bound<T>
    fun endBound(): Bound<T>
}

internal data class IndexRange(val start: Int, val end: Int)

internal fun <A, B, C> third(t: Triple<A, B, C>): C = t.third

internal fun simplifyRange(range: RangeBounds<Int>, len: Int): IndexRange {
    val start = when (val bound = range.startBound()) {
        Bound.Unbounded -> 0
        is Bound.Included -> if (bound.value <= len) {
            bound.value
        } else {
            throw IndexOutOfBoundsException("range start index ${bound.value} out of range for slice of length $len")
        }
        is Bound.Excluded -> if (bound.value < len) {
            bound.value + 1
        } else {
            throw IndexOutOfBoundsException("range start index ${bound.value} out of range for slice of length $len")
        }
    }
    val end = when (val bound = range.endBound()) {
        Bound.Unbounded -> len
        is Bound.Excluded -> if (bound.value <= len) {
            bound.value
        } else {
            throw IndexOutOfBoundsException("range end index ${bound.value} out of range for slice of length $len")
        }
        is Bound.Included -> if (bound.value < len) {
            bound.value + 1
        } else {
            throw IndexOutOfBoundsException("range end index ${bound.value} out of range for slice of length $len")
        }
    }
    if (start > end) {
        throw IndexOutOfBoundsException(
            "range start index ${range.startBound()} should be <= range end index ${range.endBound()}",
        )
    }
    return IndexRange(start, end)
}

internal fun trySimplifyRange(range: RangeBounds<Int>, len: Int): IndexRange? {
    val start = when (val bound = range.startBound()) {
        Bound.Unbounded -> 0
        is Bound.Included -> if (bound.value <= len) bound.value else return null
        is Bound.Excluded -> if (bound.value < len) bound.value + 1 else return null
    }
    val end = when (val bound = range.endBound()) {
        Bound.Unbounded -> len
        is Bound.Excluded -> if (bound.value <= len) bound.value else return null
        is Bound.Included -> if (bound.value < len) bound.value + 1 else return null
    }
    if (start > end) {
        return null
    }
    return IndexRange(start, end)
}

// Generic list equality -- copied from the standard library but adding a custom comparator,
// allowing for our `Bucket` wrapper on either or both sides.
internal fun <T, U> sliceEq(left: List<T>, right: List<U>, eq: (T, U) -> Boolean): Boolean {
    if (left.size != right.size) {
        return false
    }

    // Implemented as explicit indexing rather
    // than zipped iterators for performance reasons.
    // See PR https://github.com/rust-lang/rust/pull/116846
    for (i in left.indices) {
        // bound checks are optimized away
        if (!eq(left[i], right[i])) {
            return false
        }
    }

    return true
}
