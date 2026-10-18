package io.github.kotlinmania.indexmap

// NOTE: This is copied from the slice module in the host standard library.
// The error type returned by IndexMap.getDisjointIndicesMut.
//
// It indicates one of two possible errors:
// - An index is out-of-bounds.
// - The same index appeared multiple times in the array.
//   (or different but overlapping indices when ranges are provided)
public enum class GetDisjointMutError {
    // An index provided was out-of-bounds for the slice.
    IndexOutOfBounds,

    // Two indices provided were overlapping.
    OverlappingIndices,

    ;

    public fun fmt(): String = toString()

    override fun toString(): String =
        when (this) {
            IndexOutOfBounds -> "an index is out of bounds"
            OverlappingIndices -> "there were overlapping indices"
        }
}
