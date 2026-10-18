package io.github.kotlinmania.indexmap

// Memory layout description carried by `TryReserveErrorKind.AllocError`.
// In upstream Rust this is `alloc::alloc::Layout`. The Kotlin port preserves
// the (size, align) carrier so the error kind stays faithful to upstream;
// no allocator semantics are implied beyond the data carrier.
public data class Layout(
    val size: ULong,
    val align: ULong,
)

// The error type for try-reserve methods on IndexMap and IndexSet.
//
// The upstream reserve helper reports this value on allocation failure; the
// error is a returned value, never thrown. The Kotlin port mirrors that:
// `TryReserveError` is a plain value class, not a `Throwable` subclass.
// Extending Throwable would expose `kotlin.Throwable` (with its
// `Array<StackTraceElement>` reach) across the Swift Export boundary,
// dragging in the plugin-generated `KotlinStdlib.kt` bridge whose
// `Any?` → `Array<Any?>` unchecked casts trip `allWarningsAsErrors`.
public class TryReserveError internal constructor(
    internal val kind: TryReserveErrorKind,
) {
    public val message: String
        get() {
            val reason =
                when (kind) {
                    is TryReserveErrorKind.Std -> return kind.display
                    TryReserveErrorKind.CapacityOverflow ->
                        " because the computed capacity exceeded the collection's maximum"
                    is TryReserveErrorKind.AllocError ->
                        " because the memory allocator returned an error"
                }
            return "memory allocation failed$reason"
        }

    public fun fmt(): String = message

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
    internal data class Std(
        val display: String,
    ) : TryReserveErrorKind()

    internal data object CapacityOverflow : TryReserveErrorKind()

    internal data class AllocError(
        val layout: Layout,
    ) : TryReserveErrorKind()
}

// Mirror of the hashbrown try-reserve error shape used by `fromHashbrown`.
// Mirrors the upstream `hashbrown::TryReserveError` variants that the lib.rs
// converter inspects.
internal sealed class HashbrownTryReserveError {
    internal data object CapacityOverflow : HashbrownTryReserveError()

    internal data class AllocError(
        val layout: Layout,
    ) : HashbrownTryReserveError()
}
