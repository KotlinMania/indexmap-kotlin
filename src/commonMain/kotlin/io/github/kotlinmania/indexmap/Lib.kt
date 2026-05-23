// port-lint: source src/lib.rs
package io.github.kotlinmania.indexmap

// IndexMap is a hash table where the iteration order of the key-value pairs
// is independent of the hash values of the keys.
//
// IndexSet is a corresponding hash set using the same implementation and with
// similar properties.
//
// ### Highlights
//
// IndexMap and IndexSet are drop-in compatible with the standard library's
// MutableMap and MutableSet, but they also have some features of note:
//
// - The ordering semantics (see their documentation for details)
// - Sorting methods and the pop() methods.
// - The Equivalent interface, which offers more flexible equality definitions
//   between borrowed and owned versions of keys.
// - The MutableKeys interface, which gives opt-in mutable access to map keys,
//   and MutableValues for sets.
//
// ### Feature flags
//
// To reduce the amount of compiled code in the crate by default, certain
// features are gated behind feature flags. These allow you to opt in to (or
// out of) functionality. Below is a list of the features available in this
// crate.
//
// * `std`: Enables features which require the host standard library. For more
//   information see the section on no-std targets.
// * `rayon`: Enables parallel iteration and other parallel methods.
// * `serde`: Adds implementations for Serialize and Deserialize to IndexMap
//   and IndexSet. Alternative implementations for (de)serializing IndexMap as
//   an ordered sequence are available in the map.serdeSeq module.
// * `arbitrary`: Adds implementations for the Arbitrary interface to IndexMap
//   and IndexSet.
// * `quickcheck`: Adds implementations for the quickcheck Arbitrary interface
//   to IndexMap and IndexSet.
// * `borsh` (deprecated): Adds implementations for BorshSerialize and
//   BorshDeserialize to IndexMap and IndexSet. Due to a cyclic dependency that
//   arose between borsh and indexmap, borsh v1.5.6 added an indexmap feature
//   that should be used instead of enabling the feature here.
//
// _Note: only the `std` feature is enabled by default._
//
// ### Alternate hashers
//
// IndexMap and IndexSet have a default hasher type, just like the standard
// library's MutableMap and MutableSet, which is resistant to HashDoS attacks
// but not the most performant. Type aliases can make it easier to use
// alternate hashers:
//
//     typealias FnvIndexMap<K, V> = IndexMap<K, V, FnvBuildHasher>
//     typealias FnvIndexSet<T> = IndexSet<T, FnvBuildHasher>
//
//     val std: IndexSet<Int> = (0 until 100).toIndexSet()
//     val fnv: FnvIndexSet<Int> = (0 until 100).toIndexSet()
//     check(std == fnv)
//
// ### No standard library targets
//
// This crate supports being built without `std`, requiring `alloc` instead.
// This is chosen by disabling the default "std" cargo feature, by adding
// `default-features = false` to your dependency specification.
//
// - Creating maps and sets using IndexMap() and withCapacity() is unavailable
//   without `std`. Use IndexMap.default(), withHasher(), and
//   withCapacityAndHasher() instead. A no-std compatible hasher will be needed
//   as well.
// - The convenience builders indexmapOf() and indexsetOf() are unavailable
//   without `std`. Use indexmapWithDefault() and indexsetWithDefault() instead.

// Upstream module declarations from `src/lib.rs`:
//   mod arbitrary
//   mod inner
//   mod macros
//   mod borsh        (feature = "borsh")
//   mod serde        (feature = "serde")
//   mod sval         (feature = "sval")
//   mod util
//   pub mod map
//   pub mod set
//   mod rayon        (feature = "rayon")
//
// Upstream public re-exports from `src/lib.rs`:
//   pub use crate::map::IndexMap;
//   pub use crate::set::IndexSet;
//   pub use equivalent::Equivalent;
// Per workspace mod.rs / lib.rs re-export rules, these names are not minted as
// central typealiases here. Callers import the original symbols directly from
// their owning packages.

// shared private items

// Hash value newtype. Not larger than ULong, since anything larger isn't used
// for selecting position anyway.
internal class HashValue(internal val raw: ULong) {
    internal fun get(): ULong = raw

    override fun equals(other: Any?): Boolean =
        other is HashValue && other.raw == raw

    override fun hashCode(): Int = raw.hashCode()

    override fun toString(): String = "HashValue($raw)"
}

internal class Bucket<K, V>(
    internal var hash: HashValue,
    internal var key: K,
    internal var value: V,
) {
    internal fun clone(): Bucket<K, V> = Bucket(hash, key, value)

    internal fun cloneFrom(other: Bucket<K, V>) {
        this.hash = other.hash
        this.key = other.key
        this.value = other.value
    }

    // field accessors -- used for `f` instead of closures in `.map(f)`
    internal fun keyRef(): K = key
    internal fun valueRef(): V = value
    internal fun valueMut(): V = value
    internal fun consumeKey(): K = key
    internal fun consumeValue(): V = value
    internal fun keyValue(): Pair<K, V> = key to value
    internal fun refs(): Pair<K, V> = key to value
    internal fun refMut(): Pair<K, V> = key to value
    internal fun muts(): Pair<K, V> = key to value

    override fun toString(): String = "Bucket(hash=$hash, key=$key, value=$value)"
}

// Memory layout description carried by `TryReserveErrorKind.AllocError`.
// In upstream Rust this is `alloc::alloc::Layout`. The Kotlin port preserves
// the (size, align) carrier so the error kind stays faithful to upstream;
// no allocator semantics are implied beyond the data carrier.
public data class Layout(val size: ULong, val align: ULong)

// The error type for try-reserve methods on IndexMap and IndexSet.
public class TryReserveError internal constructor(internal val kind: TryReserveErrorKind) : Throwable() {

    override val message: String
        get() {
            val reason = when (kind) {
                is TryReserveErrorKind.Std -> return kind.display
                TryReserveErrorKind.CapacityOverflow ->
                    " because the computed capacity exceeded the collection's maximum"
                is TryReserveErrorKind.AllocError ->
                    " because the memory allocator returned an error"
            }
            return "memory allocation failed$reason"
        }

    override fun toString(): String = message

    override fun equals(other: Any?): Boolean =
        other is TryReserveError && other.kind == kind

    override fun hashCode(): Int = kind.hashCode()

    internal companion object {
        // These are not exposed as constructors so they don't appear in the
        // public API.
        internal fun fromAlloc(display: String): TryReserveError =
            TryReserveError(TryReserveErrorKind.Std(display))

        internal fun fromHashbrown(error: HashbrownTryReserveError): TryReserveError =
            TryReserveError(
                when (error) {
                    HashbrownTryReserveError.CapacityOverflow ->
                        TryReserveErrorKind.CapacityOverflow
                    is HashbrownTryReserveError.AllocError ->
                        TryReserveErrorKind.AllocError(error.layout)
                },
            )
    }
}

internal sealed class TryReserveErrorKind {
    // The host standard library's kind is currently opaque to us, otherwise we
    // could unify this. The Kotlin port keeps the underlying error's display
    // form so the resulting message remains identical.
    internal data class Std(val display: String) : TryReserveErrorKind()
    internal data object CapacityOverflow : TryReserveErrorKind()
    internal data class AllocError(val layout: Layout) : TryReserveErrorKind()
}

// Mirror of the hashbrown try-reserve error shape used by `fromHashbrown`.
// Mirrors the upstream `hashbrown::TryReserveError` variants that the lib.rs
// converter inspects.
internal sealed class HashbrownTryReserveError {
    internal data object CapacityOverflow : HashbrownTryReserveError()
    internal data class AllocError(val layout: Layout) : HashbrownTryReserveError()
}

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
    OverlappingIndices;

    override fun toString(): String = when (this) {
        IndexOutOfBounds -> "an index is out of bounds"
        OverlappingIndices -> "there were overlapping indices"
    }
}
