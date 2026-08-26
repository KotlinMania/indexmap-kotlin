// port-lint: source serde.rs
package io.github.kotlinmania.indexmap

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.math.min

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
 * Serializer for [IndexMap] using standard map serialization semantics.
 */
public class IndexMapSerializer<K, V>(
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
) : KSerializer<IndexMap<K, V>> {
    private val delegate = MapSerializer(keySerializer, valueSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: IndexMap<K, V>) {
        val map = LinkedHashMap<K, V>(value.len())
        for ((k, v) in value) {
            map[k] = v
        }
        delegate.serialize(encoder, map)
    }

    override fun deserialize(decoder: Decoder): IndexMap<K, V> {
        val map = delegate.deserialize(decoder)
        val result = IndexMap.withCapacity<K, V>(cautiousCapacity(map.size))
        for ((k, v) in map) {
            result.insert(k, v)
        }
        return result
    }
}

/**
 * Serializer for [IndexSet] using standard set serialization semantics.
 */
public class IndexSetSerializer<T>(
    elementSerializer: KSerializer<T>,
) : KSerializer<IndexSet<T>> {
    private val delegate = SetSerializer(elementSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: IndexSet<T>) {
        val set = LinkedHashSet<T>(value.len())
        for (item in value) {
            set.add(item)
        }
        delegate.serialize(encoder, set)
    }

    override fun deserialize(decoder: Decoder): IndexSet<T> {
        val set = delegate.deserialize(decoder)
        val result = IndexSet.withCapacity<T>(cautiousCapacity(set.size))
        for (item in set) {
            result.insert(item)
        }
        return result
    }
}
