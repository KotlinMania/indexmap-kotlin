// port-lint: source sval.rs
package io.github.kotlinmania.indexmap

/**
 * Interface for streaming value representations according to sval.
 */
public interface SvalStream {
    public fun mapBegin(len: Int?)

    public fun mapKeyBegin()

    public fun mapKeyEnd()

    public fun mapValueBegin()

    public fun mapValueEnd()

    public fun mapEnd()

    public fun seqBegin(len: Int?)

    public fun seqValueBegin()

    public fun seqValueEnd()

    public fun seqEnd()

    public fun value(v: Any?)
}

/**
 * Streams the map structure and its key-value pairs to an [SvalStream].
 */
public fun <K, V> IndexMap<K, V>.svalStream(
    stream: SvalStream,
    streamKey: (SvalStream, K) -> Unit = { s, k -> s.value(k) },
    streamValue: (SvalStream, V) -> Unit = { s, v -> s.value(v) },
) {
    stream.mapBegin(len())
    for ((k, v) in this) {
        stream.mapKeyBegin()
        streamKey(stream, k)
        stream.mapKeyEnd()

        stream.mapValueBegin()
        streamValue(stream, v)
        stream.mapValueEnd()
    }
    stream.mapEnd()
}

/**
 * Streams the set structure and its elements to an [SvalStream].
 */
public fun <T> IndexSet<T>.svalStream(
    stream: SvalStream,
    streamValue: (SvalStream, T) -> Unit = { s, v -> s.value(v) },
) {
    stream.seqBegin(len())
    for (value in this) {
        stream.seqValueBegin()
        streamValue(stream, value)
        stream.seqValueEnd()
    }
    stream.seqEnd()
}
