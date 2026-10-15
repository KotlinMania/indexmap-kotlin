// port-lint: source map/serde_seq.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.IndexMap
import io.github.kotlinmania.indexmap.SeedFromDeserialize
import io.github.kotlinmania.indexmap.cautiousCapacity
import io.github.kotlinmania.indexmap.wrapSerialize
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serde.serdeCatching
import io.github.kotlinmania.serdecore.de.Deserialize
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.SeqAccess
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.de.pairDeserialize
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.native.HiddenFromObjC

/**
 * Functions and serializer to serialize and deserialize an [IndexMap] as an ordered sequence of pairs.
 */
@HiddenFromObjC
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

@HiddenFromObjC
public fun <K, V> serialize(
    map: IndexMap<K, V>,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
    encoder: Encoder,
) {
    IndexMapSeqSerializer(keySerializer, valueSerializer).serialize(encoder, map)
}

@HiddenFromObjC
public fun <K, V> deserialize(
    decoder: Decoder,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
): IndexMap<K, V> = IndexMapSeqSerializer(keySerializer, valueSerializer).deserialize(decoder)

@HiddenFromObjC
public fun <Ok> serialize(
    map: IndexMap<*, *>,
    serializer: Serializer<Ok>,
): SerdeResult<Ok> {
    val mapped =
        map.map { (k, v) ->
            object : Serialize {
                override fun <OkTuple> serialize(serializer: Serializer<OkTuple>): SerdeResult<OkTuple> =
                    serdeCatching {
                        val tuple = serializer.serializeTuple(2).getOrThrow()
                        tuple.serializeElement(wrapSerialize(k)).getOrThrow()
                        tuple.serializeElement(wrapSerialize(v)).getOrThrow()
                        tuple.end().getOrThrow()
                    }
            }
        }
    return serializer.collectSeq(mapped)
}

@HiddenFromObjC
public class SeqVisitor<K, V> : Visitor<IndexMap<K, V>> {
    private val keyDeserialize: Deserialize<K>?
    private val valueDeserialize: Deserialize<V>?

    public constructor() {
        this.keyDeserialize = null
        this.valueDeserialize = null
    }

    public constructor(keyDeserialize: Deserialize<K>, valueDeserialize: Deserialize<V>) {
        this.keyDeserialize = keyDeserialize
        this.valueDeserialize = valueDeserialize
    }

    override fun expecting(): String = "a sequenced map"

    public fun expecting(formatter: StringBuilder) {
        formatter.append(expecting())
    }

    public fun visitSeq(entries: Iterable<Pair<K, V>>): IndexMap<K, V> {
        val map = IndexMap<K, V>()
        for ((k, v) in entries) {
            map.insert(k, v)
        }
        return map
    }

    override fun <A : SeqAccess> visitSeq(access: A): SerdeResult<IndexMap<K, V>> =
        serdeCatching {
            val kd = checkNotNull(keyDeserialize) { "keyDeserialize is required for visitSeq(SeqAccess)" }
            val vd = checkNotNull(valueDeserialize) { "valueDeserialize is required for visitSeq(SeqAccess)" }
            val capacity = cautiousCapacity(access.sizeHint())
            val map = IndexMap.withCapacity<K, V>(capacity)
            val pairSeed = SeedFromDeserialize(pairDeserialize(kd, vd))
            while (true) {
                val next = access.nextElementSeed(pairSeed).getOrThrow() ?: break
                map.insert(next.first, next.second)
            }
            map
        }
}

@HiddenFromObjC
public fun <K, V> deserialize(
    keyDeserialize: Deserialize<K>,
    valueDeserialize: Deserialize<V>,
): Deserialize<IndexMap<K, V>> =
    object : Deserialize<IndexMap<K, V>> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<IndexMap<K, V>> =
            deserializer.deserializeSeq(SeqVisitor(keyDeserialize, valueDeserialize))
    }

@HiddenFromObjC
public class SequencedIndexMap<K, V>(
    public val map: IndexMap<K, V>,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
        serialize(map, serializer)

    override fun equals(other: Any?): Boolean =
        other is SequencedIndexMap<*, *> && map == other.map

    override fun hashCode(): Int = map.hashCode()
}

@HiddenFromObjC
public fun <K, V> IndexMap<K, V>.asSequenced(): SequencedIndexMap<K, V> =
    SequencedIndexMap(this)
