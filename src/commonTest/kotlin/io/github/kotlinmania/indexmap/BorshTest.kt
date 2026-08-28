// port-lint: tests borsh.rs
package io.github.kotlinmania.indexmap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BorshTest {
    private class SimpleWriter : BorshWriter {
        val buffer = mutableListOf<Byte>()

        override fun write(bytes: ByteArray, offset: Int, length: Int) {
            for (i in offset until offset + length) {
                buffer.add(bytes[i])
            }
        }

        override fun writeU32(value: UInt) {
            buffer.add((value and 0xFFu).toByte())
            buffer.add(((value shr 8) and 0xFFu).toByte())
            buffer.add(((value shr 16) and 0xFFu).toByte())
            buffer.add(((value shr 24) and 0xFFu).toByte())
        }

        fun toByteArray(): ByteArray = buffer.toByteArray()
    }

    private class SimpleReader(private val bytes: ByteArray) : BorshReader {
        private var pos = 0

        override fun read(bytes: ByteArray, offset: Int, length: Int): Int {
            var readCount = 0
            for (i in offset until offset + length) {
                if (pos < this.bytes.size) {
                    bytes[i] = this.bytes[pos++]
                    readCount++
                } else break
            }
            return readCount
        }

        override fun readU32(): UInt {
            val b0 = bytes[pos++].toUByte().toUInt()
            val b1 = bytes[pos++].toUByte().toUInt()
            val b2 = bytes[pos++].toUByte().toUInt()
            val b3 = bytes[pos++].toUByte().toUInt()
            return b0 or (b1 shl 8) or (b2 shl 16) or (b3 shl 24)
        }
    }

    private val intSerializer = object : BorshSerializer<Int> {
        override fun serialize(value: Int, writer: BorshWriter) {
            writer.writeU32(value.toUInt())
        }
    }

    private val intDeserializer = object : BorshDeserializer<Int> {
        override fun deserialize(reader: BorshReader): Int {
            return reader.readU32().toInt()
        }
    }

    @Test
    fun mapBorshRoundtrip() {
        val originalMap = IndexMap<Int, Int>()
        originalMap.insert(1, 2)
        originalMap.insert(3, 4)
        originalMap.insert(5, 6)

        val writer = SimpleWriter()
        originalMap.borshSerialize(writer, intSerializer, intSerializer)

        val reader = SimpleReader(writer.toByteArray())
        val deserializedMap = IndexMap.borshDeserialize(reader, intDeserializer, intDeserializer)

        assertEquals(originalMap, deserializedMap)
    }

    @Test
    fun setBorshRoundtrip() {
        val originalSet = IndexSet<Int>()
        for (i in listOf(1, 2, 3, 4, 5, 6)) {
            originalSet.insert(i)
        }

        val writer = SimpleWriter()
        originalSet.borshSerialize(writer, intSerializer)

        val reader = SimpleReader(writer.toByteArray())
        val deserializedSet = IndexSet.borshDeserialize(reader, intDeserializer)

        assertEquals(originalSet, deserializedSet)
    }

    @Test
    fun zstCheckForbidden() {
        assertFailsWith<IllegalArgumentException> {
            checkZst(true)
        }
    }
}
