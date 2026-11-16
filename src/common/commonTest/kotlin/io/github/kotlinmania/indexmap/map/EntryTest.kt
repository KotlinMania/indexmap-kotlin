// port-lint: tests map/tests.rs
package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.IndexMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

class EntryTest {
    @Test
    fun entryUpdatesOccupiedAndInsertsVacant() {
        val map = IndexMap.new<String, Int>()
        map.insert("a", 1)

        val occupied = map.entry("a")
        assertIs<Entry.Occupied<String, Int>>(occupied)
        assertEquals(0, occupied.index())
        assertEquals(1, occupied.orInsert(10))
        occupied.andModify { it + 1 }
        assertEquals(2, map["a"])

        val vacant = map.entry("b")
        assertIs<Entry.Vacant<String, Int>>(vacant)
        assertEquals(1, vacant.index())
        assertEquals(20, vacant.orInsertWith { 20 })
        assertEquals(listOf("a" to 2, "b" to 20), map.asEntries())
    }

    @Test
    fun mutableKeysRetainsAndExposesEntriesInOrder() {
        val map: MutableKeys<String, Int> = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3))

        assertEquals(Triple(1, "b", 2), map.getFullMut2("b"))
        assertEquals("c" to 3, map.getIndexMut2(2))
        assertEquals(listOf("a" to 1, "b" to 2, "c" to 3), map.iterMut2())

        map.retain2 { _, value -> value % 2 == 1 }
        assertEquals(listOf("a" to 1, "c" to 3), map.iterMut2())
    }

    @Test
    fun mutableEntryKeyReplacesKeysWithoutChangingValues() {
        val map = IndexMap.from(listOf("old" to 1))
        val entry = map.entry("old")

        assertEquals("old", entry.replaceKey("new"))
        assertFalse(map.containsKey("old"))
        assertEquals(1, map["new"])

        val vacant = map.entry("later")
        assertEquals("later", vacant.replaceKey("inserted"))
        vacant.insertEntry(2)
        assertEquals(listOf("new" to 1, "inserted" to 2), map.asEntries())
    }

    @Test
    fun indexedEntryRemovesAndMovesByIndex() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3))

        val entry = map.indexedEntry(1) ?: error("missing indexed entry")
        assertEquals("b", entry.key())
        assertEquals(2, entry.insert(20))
        entry.moveIndex(2)
        assertEquals(listOf("a" to 1, "c" to 3, "b" to 20), map.asEntries())

        assertEquals("b" to 20, entry.shiftRemoveEntry())
        assertEquals(listOf("a" to 1, "c" to 3), map.asEntries())
    }
}
