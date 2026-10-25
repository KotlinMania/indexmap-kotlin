// port-lint: source serde.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.indexmap

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serde.serdeCatching
import io.github.kotlinmania.serdecore.de.Deserialize
import io.github.kotlinmania.serdecore.de.DeserializeSeed
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.IntoDeserializer
import io.github.kotlinmania.serdecore.de.MapAccess
import io.github.kotlinmania.serdecore.de.SeqAccess
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.de.value.MapDeserializer
import io.github.kotlinmania.serdecore.de.value.MapEntry
import io.github.kotlinmania.serdecore.de.value.SeqDeserializer
import io.github.kotlinmania.serdecore.de.value.mapDeserializer
import io.github.kotlinmania.serdecore.de.value.seqDeserializer
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer
import kotlin.math.min
import kotlin.native.HiddenFromObjC

/**
 * Limit our preallocated capacity from a deserializer size hint.
 *
 * We account for the element overhead, capping the preallocation to 1MB.
 */
internal fun cautiousCapacity(hint: Int?): Int {
    val maxPreallocBytes = 1024 * 1024
    val estimatedBucketSize = 64
    val maxCapacity = maxPreallocBytes / estimatedBucketSize
    return min(hint ?: 0, maxCapacity)
}

/**
 * Visitor to deserialize an [IndexMap].
 */
@HiddenFromObjC
public class IndexMapVisitor<K, V> : Visitor<IndexMap<K, V>> {
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

    override fun expecting(): String = "a map"

    public fun expecting(formatter: StringBuilder) {
        formatter.append(expecting())
    }

    public fun visitMap(entries: Iterable<Pair<K, V>>): IndexMap<K, V> {
        val map = IndexMap<K, V>()
        for ((k, v) in entries) {
            map.insert(k, v)
        }
        return map
    }

    override fun <A : MapAccess> visitMap(access: A): SerdeResult<IndexMap<K, V>> =
        serdeCatching {
            val kd = checkNotNull(keyDeserialize) { "keyDeserialize is required for visitMap(MapAccess)" }
            val vd = checkNotNull(valueDeserialize) { "valueDeserialize is required for visitMap(MapAccess)" }
            val capacity = cautiousCapacity(access.sizeHint())
            val values = IndexMap.withCapacity<K, V>(capacity)
            val keySeed = SeedFromDeserialize(kd)
            val valueSeed = SeedFromDeserialize(vd)
            while (true) {
                val entry = access.nextEntrySeed(keySeed, valueSeed).getOrThrow() ?: break
                values.insert(entry.first, entry.second)
            }
            values
        }
}

public fun <K, V> indexMapDeserialize(
    keyDeserialize: Deserialize<K>,
    valueDeserialize: Deserialize<V>,
): Deserialize<IndexMap<K, V>> =
    object : Deserialize<IndexMap<K, V>> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<IndexMap<K, V>> =
            deserializer.deserializeMap(IndexMapVisitor(keyDeserialize, valueDeserialize))
    }

/**
 * Visitor to deserialize an [IndexSet].
 */
@HiddenFromObjC
public class IndexSetVisitor<T> : Visitor<IndexSet<T>> {
    private val elementDeserialize: Deserialize<T>?

    public constructor() {
        this.elementDeserialize = null
    }

    public constructor(elementDeserialize: Deserialize<T>) {
        this.elementDeserialize = elementDeserialize
    }

    override fun expecting(): String = "a set"

    public fun expecting(formatter: StringBuilder) {
        formatter.append(expecting())
    }

    public fun visitSeq(elements: Iterable<T>): IndexSet<T> {
        val set = IndexSet<T>()
        for (item in elements) {
            set.insert(item)
        }
        return set
    }

    override fun <A : SeqAccess> visitSeq(access: A): SerdeResult<IndexSet<T>> =
        serdeCatching {
            val ed = checkNotNull(elementDeserialize) { "elementDeserialize is required for visitSeq(SeqAccess)" }
            val capacity = cautiousCapacity(access.sizeHint())
            val values = IndexSet.withCapacity<T>(capacity)
            val seed = SeedFromDeserialize(ed)
            while (true) {
                val next = access.nextElementSeed(seed).getOrThrow() ?: break
                values.insert(next)
            }
            values
        }
}

public fun <T> indexSetDeserialize(
    elementDeserialize: Deserialize<T>,
): Deserialize<IndexSet<T>> =
    object : Deserialize<IndexSet<T>> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<IndexSet<T>> =
            deserializer.deserializeSeq(IndexSetVisitor(elementDeserialize))
    }

internal class SeedFromDeserialize<T>(
    private val deserialize: Deserialize<T>,
) : DeserializeSeed<T> {
    override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<T> =
        deserialize.deserialize(deserializer)
}

public fun wrapSerialize(value: Any?): Serialize =
    when (value) {
        null -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeNone()
            }
        }

        is Serialize -> {
            value
        }

        is Boolean -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeBool(value)
            }
        }

        is Int -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeI32(value)
            }
        }

        is Long -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeI64(value)
            }
        }

        is Byte -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeI8(value)
            }
        }

        is Short -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeI16(value)
            }
        }

        is UByte -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeU8(value)
            }
        }

        is UShort -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeU16(value)
            }
        }

        is UInt -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeU32(value)
            }
        }

        is ULong -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeU64(value)
            }
        }

        is Float -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeF32(value)
            }
        }

        is Double -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeF64(value)
            }
        }

        is Char -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeChar(value)
            }
        }

        is String -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeStr(value)
            }
        }

        is ByteArray -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeBytes(value)
            }
        }

        is Pair<*, *> -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serdeCatching {
                        val tuple = serializer.serializeTuple(2).getOrThrow()
                        tuple.serializeElement(wrapSerialize(value.first)).getOrThrow()
                        tuple.serializeElement(wrapSerialize(value.second)).getOrThrow()
                        tuple.end().getOrThrow()
                    }
            }
        }

        else -> {
            object : Serialize {
                override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
                    serializer.serializeStr(value.toString())
            }
        }
    }

/**
 * Converts this [IndexMap] into an iterator deserializer representation.
 */
public fun <K, V> IndexMap<K, V>.intoDeserializer(): Iterator<Pair<K, V>> = iterator()

/**
 * Converts this [IndexSet] into an iterator deserializer representation.
 */
public fun <T> IndexSet<T>.intoDeserializer(): Iterator<T> = iterator()

public fun <K : IntoDeserializer, V : IntoDeserializer> IndexMap<K, V>.intoMapDeserializer(): MapDeserializer<K, V> =
    mapDeserializer(map { MapEntry(it.first, it.second) }.iterator())

public fun <T : IntoDeserializer> IndexSet<T>.intoSeqDeserializer(): SeqDeserializer<T> =
    seqDeserializer(iterator())
