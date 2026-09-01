// port-lint: source borsh.rs
package io.github.kotlinmania.indexmap

public const val ERROR_ZST_FORBIDDEN: String = "Zero-sized types are forbidden"

/**
 * Interface for writing binary data according to Borsh specification.
 */
public interface BorshWriter {
    public fun write(bytes: ByteArray, offset: Int = 0, length: Int = bytes.size)

    public fun writeU32(value: UInt)
}

/**
 * Interface for reading binary data according to Borsh specification.
 */
public interface BorshReader {
    public fun read(bytes: ByteArray, offset: Int = 0, length: Int = bytes.size): Int

    public fun readU32(): UInt
}

/**
 * Interface for Borsh serialization.
 */
public interface BorshSerializer<T> {
    public fun serialize(value: T, writer: BorshWriter)
}

/**
 * Interface for Borsh deserialization.
 */
public interface BorshDeserializer<T> {
    public fun deserialize(reader: BorshReader): T
}

/**
 * Helper to check zero-sized types restriction in Borsh.
 */
public fun checkZst(isZst: Boolean) {
    if (isZst) {
        throw IllegalArgumentException(ERROR_ZST_FORBIDDEN)
    }
}

/**
 * Serializes an [IndexMap] into a [BorshWriter].
 */
public fun <K, V> IndexMap<K, V>.borshSerialize(
    writer: BorshWriter,
    keySerializer: BorshSerializer<K>,
    valueSerializer: BorshSerializer<V>,
    isKeyZst: Boolean = false,
) {
    checkZst(isKeyZst)
    writer.writeU32(len().toUInt())
    for ((key, value) in this) {
        keySerializer.serialize(key, writer)
        valueSerializer.serialize(value, writer)
    }
}

/**
 * Deserializes an [IndexMap] from a [BorshReader].
 */
public fun <K, V> IndexMap.Companion.borshDeserialize(
    reader: BorshReader,
    keyDeserializer: BorshDeserializer<K>,
    valueDeserializer: BorshDeserializer<V>,
    isKeyZst: Boolean = false,
): IndexMap<K, V> {
    checkZst(isKeyZst)
    val len = reader.readU32().toInt()
    val map = IndexMap.withCapacity<K, V>(len)
    for (i in 0 until len) {
        val key = keyDeserializer.deserialize(reader)
        val value = valueDeserializer.deserialize(reader)
        map.insert(key, value)
    }
    return map
}

/**
 * Serializes an [IndexSet] into a [BorshWriter].
 */
public fun <T> IndexSet<T>.borshSerialize(
    writer: BorshWriter,
    itemSerializer: BorshSerializer<T>,
    isItemZst: Boolean = false,
) {
    checkZst(isItemZst)
    writer.writeU32(len().toUInt())
    for (item in this) {
        itemSerializer.serialize(item, writer)
    }
}

/**
 * Deserializes an [IndexSet] from a [BorshReader].
 */
public fun <T> IndexSet.Companion.borshDeserialize(
    reader: BorshReader,
    itemDeserializer: BorshDeserializer<T>,
    isItemZst: Boolean = false,
): IndexSet<T> {
    checkZst(isItemZst)
    val len = reader.readU32().toInt()
    val set = IndexSet.withCapacity<T>(len)
    for (i in 0 until len) {
        val item = itemDeserializer.deserialize(reader)
        set.insert(item)
    }
    return set
}
