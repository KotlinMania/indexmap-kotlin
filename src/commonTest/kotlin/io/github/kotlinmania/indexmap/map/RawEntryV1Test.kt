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
}
