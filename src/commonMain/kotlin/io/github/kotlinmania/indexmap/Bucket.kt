package io.github.kotlinmania.indexmap

// Hash value newtype. Not larger than ULong, since anything larger isn't used
// for selecting position anyway.
internal class HashValue(
    internal val raw: ULong,
) {
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

    internal fun key(): K = key

    internal fun value(): V = value

    internal fun keyValue(): Pair<K, V> = key to value

    internal fun refs(): Pair<K, V> = key to value

    internal fun refMut(): Pair<K, V> = key to value

    internal fun muts(): Pair<K, V> = key to value

    override fun toString(): String = "Bucket(hash=$hash, key=$key, value=$value)"
}
