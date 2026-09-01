// port-lint: source map/serde_seq.rs
package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.IndexMap
import io.github.kotlinmania.indexmap.cautiousCapacity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Functions and serializer to serialize and deserialize an [IndexMap] as an ordered sequence of pairs.
 */
public class IndexMapSeqSerializer<K, V>(
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
) : KSerializer<IndexMap<K, V>> {
    private val delegate = ListSerializer(PairSerializer(keySerializer, valueSerializer))

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: IndexMap<K, V>) {
        val list = value.map { it.first to it.second }
        delegate.serialize(encoder, list)
    }

    override fun deserialize(decoder: Decoder): IndexMap<K, V> {
        val list = delegate.deserialize(decoder)
        val result = IndexMap.withCapacity<K, V>(cautiousCapacity(list.size))
        for ((k, v) in list) {
            result.insert(k, v)
        }
        return result
    }
}

public fun <K, V> serialize(
    map: IndexMap<K, V>,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
    encoder: Encoder,
) {
    IndexMapSeqSerializer(keySerializer, valueSerializer).serialize(encoder, map)
}

public fun <K, V> deserialize(
    decoder: Decoder,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
): IndexMap<K, V> = IndexMapSeqSerializer(keySerializer, valueSerializer).deserialize(decoder)
