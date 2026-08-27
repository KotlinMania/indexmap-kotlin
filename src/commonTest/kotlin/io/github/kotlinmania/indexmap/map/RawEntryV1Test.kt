// port-lint: tests map/raw_entry_v1.rs
package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.IndexMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class RawEntryV1Test {
    @Test
    fun rawEntryV1ReadsEntriesByHashOrKey() {
        val map = IndexMap.from(listOf("a" to 100, "b" to 200, "c" to 300))
        val raw = map.rawEntryV1()

        assertEquals("a" to 100, raw.fromKey("a"))
        assertEquals("b" to 200, raw.fromHash(0uL) { it == "b" })
        assertEquals(Triple(2, "c", 300), raw.fromHashFull(0uL) { it == "c" })
        assertEquals(1, raw.indexFromHash(0uL) { it == "b" })
        assertNull(raw.fromKey("d"))
    }

    @Test
    fun rawEntryMutV1UpdatesOccupiedAndInsertsVacant() {
        val map = IndexMap.from(listOf("a" to 100, "b" to 200, "c" to 300))
        val rawMut = map.rawEntryMutV1()

        val occupied = rawMut.fromKey("a")
        assertIs<RawEntryMut.Occupied<String, Int>>(occupied)
        assertEquals(0, occupied.index())
        assertEquals("a" to 100, occupied.orInsert("a", 999))

        val vacant = rawMut.fromKey("d")
        assertIs<RawEntryMut.Vacant<String, Int>>(vacant)
        assertEquals(3, vacant.index())
        assertEquals("d" to 400, vacant.orInsert("d", 400))
        assertEquals(4, map.len())
        assertEquals(400, map["d"])
    }

    @Test
    fun rawOccupiedEntryMutModifiesAndRemoves() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3))
        val rawMut = map.rawEntryMutV1()

        when (val entry = rawMut.fromKey("b")) {
            is RawEntryMut.Vacant -> error("expected occupied")
            is RawEntryMut.Occupied -> {
                assertEquals(1, entry.entry.index())
                assertEquals("b", entry.entry.key())
                assertEquals(2, entry.entry.get())
                assertEquals(2, entry.entry.insert(20))
                assertEquals("b" to 20, entry.entry.shiftRemoveEntry())
            }
        }
        assertEquals(listOf("a" to 1, "c" to 3), map.asEntries())
    }

    @Test
    fun rawEntryV1UpstreamDocTestParity() {
        val map = IndexMap.new<String, Int>()
        map.extend(listOf("a" to 100, "b" to 200, "c" to 300))

        when (val entry = map.rawEntryMutV1().fromKey("a")) {
            is RawEntryMut.Vacant -> error("unreachable")
            is RawEntryMut.Occupied -> {
                assertEquals(0, entry.entry.index())
                assertEquals(100, entry.entry.get())
                assertEquals(100, entry.entry.insert(1111))
            }
        }
        assertEquals(1111, map["a"])
        assertEquals(3, map.len())

        when (val entry = map.rawEntryMutV1().fromKeyHashedNocheck(0uL, "c")) {
            is RawEntryMut.Vacant -> error("unreachable")
            is RawEntryMut.Occupied -> {
                assertEquals(2, entry.entry.index())
                assertEquals("c" to 300, entry.entry.shiftRemoveEntry())
            }
        }
        assertNull(map.rawEntryV1().fromKey("c"))
        assertEquals(2, map.len())

        when (val entry = map.rawEntryMutV1().fromHash(0uL) { it == "d" }) {
            is RawEntryMut.Occupied -> error("unreachable")
            is RawEntryMut.Vacant -> {
                assertEquals(2, entry.entry.index())
                val (k, value) = entry.entry.insert("d", 40000)
                assertEquals("d", k)
                assertEquals(40000, value)
            }
        }
        assertEquals(40000, map["d"])
        assertEquals(3, map.len())

        when (val entry = map.rawEntryMutV1().fromHash(0uL) { it == "d" }) {
            is RawEntryMut.Vacant -> error("unreachable")
            is RawEntryMut.Occupied -> {
                assertEquals(2, entry.entry.index())
                assertEquals("d" to 40000, entry.entry.swapRemoveEntry())
            }
        }
        assertNull(map.get("d"))
        assertEquals(2, map.len())
    }

    @Test
    fun rawOccupiedEntryMutInsertKeyAndDetails() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2))
        val rawMut = map.rawEntryMutV1()
        when (val entry = rawMut.fromKey("a")) {
            is RawEntryMut.Vacant -> error("expected occupied")
            is RawEntryMut.Occupied -> {
                assertEquals("a", entry.entry.keyMut())
                assertEquals("a", entry.entry.intoKey())
                assertEquals(1, entry.entry.intoMut())
                assertEquals("a" to 1, entry.entry.getKeyValue())
                assertEquals("a" to 1, entry.entry.getKeyValueMut())
                assertEquals("a" to 1, entry.entry.intoKeyValueMut())
                assertEquals("a", entry.entry.insertKey("a_new"))
                assertEquals("a_new", entry.entry.key())
            }
        }
        assertEquals(listOf("a_new" to 1, "b" to 2), map.asEntries())
    }

    @Test
    fun rawVacantEntryMutShiftInsert() {
        val map = IndexMap.from(listOf("a" to 1, "c" to 3))
        val rawMut = map.rawEntryMutV1()
        when (val entry = rawMut.fromKey("b")) {
            is RawEntryMut.Occupied -> error("expected vacant")
            is RawEntryMut.Vacant -> {
                val inserted = entry.entry.shiftInsert(1, "b", 2)
                assertEquals("b" to 2, inserted)
            }
        }
        assertEquals(listOf("a" to 1, "b" to 2, "c" to 3), map.asEntries())
    }

    @Test
    fun assertSendSync() {
        RawEntryBuilder.assertSendSync()
        RawEntryBuilderMut.assertSendSync()
        RawEntryMut.assertSendSync()
        RawOccupiedEntryMut.assertSendSync()
        RawVacantEntryMut.assertSendSync()
    }
}
