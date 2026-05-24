// port-lint: source map.rs
package io.github.kotlinmania.indexmap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapTest {
    @Test
    fun insertKeepsInsertionOrderAndUpdatesExistingValuesInPlace() {
        val map = IndexMap.withCapacity<Char, Int>(4)

        assertNull(map.insert('s', 1))
        assertNull(map.insert('t', 1))
        assertNull(map.insert('u', 1))
        assertEquals(1, map.insert('s', 2))

        assertEquals(3, map.len())
        assertEquals(listOf('s', 't', 'u'), map.keys())
        assertEquals(2, map['s'])
        assertEquals(1, map['t'])
        assertNull(map['y'])
    }

    @Test
    fun insertBeforeMovesExistingKeysAroundTheInsertionPoint() {
        val map = IndexMap.new<Char, Unit>()
        for (ch in 'a'..'z') {
            map.insert(ch, Unit)
        }

        assertNull(map.getIndexOf('*'))
        assertEquals(10 to null, map.insertBefore(10, '*', Unit))
        assertEquals(10, map.getIndexOf('*'))

        assertEquals(9 to Unit, map.insertBefore(10, 'a', Unit))
        assertEquals(9, map.getIndexOf('a'))
        assertEquals(10, map.getIndexOf('*'))

        assertEquals(10 to Unit, map.insertBefore(10, 'z', Unit))
        assertEquals(10, map.getIndexOf('z'))
        assertEquals(11, map.getIndexOf('*'))

        assertEquals(27, map.len())
        assertEquals(26 to Unit, map.insertBefore(map.len(), '*', Unit))
        assertEquals(26, map.getIndexOf('*'))
        assertEquals(27 to null, map.insertBefore(map.len(), '+', Unit))
        assertEquals(27, map.getIndexOf('+'))
        assertEquals(28, map.len())
    }

    @Test
    fun shiftInsertMovesExistingKeysToTheRequestedIndex() {
        val map = IndexMap.new<Char, Unit>()
        for (ch in 'a'..'z') {
            map.insert(ch, Unit)
        }

        assertNull(map.shiftInsert(10, '*', Unit))
        assertEquals(10, map.getIndexOf('*'))

        assertEquals(Unit, map.shiftInsert(10, 'a', Unit))
        assertEquals(10, map.getIndexOf('a'))
        assertEquals(9, map.getIndexOf('*'))

        assertEquals(Unit, map.shiftInsert(9, 'z', Unit))
        assertEquals(9, map.getIndexOf('z'))
        assertEquals(10, map.getIndexOf('*'))

        assertEquals(27, map.len())
        assertEquals(Unit, map.shiftInsert(map.len() - 1, '*', Unit))
        assertEquals(26, map.getIndexOf('*'))
        assertNull(map.shiftInsert(map.len(), '+', Unit))
        assertEquals(27, map.getIndexOf('+'))
        assertEquals(28, map.len())
    }

    @Test
    fun shiftAndSwapRemovalHaveDifferentOrderEffects() {
        val shifted = IndexMap.new<Int, String>()
        val swapped = IndexMap.new<Int, String>()
        for (entry in listOf(1 to "a", 2 to "b", 3 to "c", 4 to "d")) {
            shifted.insert(entry.first, entry.second)
            swapped.insert(entry.first, entry.second)
        }

        assertEquals("b", shifted.shiftRemove(2))
        assertEquals(listOf(1, 3, 4), shifted.keys())

        assertEquals("b", swapped.swapRemove(2))
        assertEquals(listOf(1, 4, 3), swapped.keys())
    }

    @Test
    fun clearTruncateAndIndexLookupTrackCompactIndices() {
        val map = IndexMap.new<String, Int>()
        for (entry in listOf("a" to 1, "b" to 2, "c" to 3, "d" to 4)) {
            map.insert(entry.first, entry.second)
        }

        assertFalse(map.isEmpty())
        assertEquals("c" to 3, map.getIndex(2))
        assertEquals(Triple(1, "b", 2), map.getFull("b"))

        map.truncate(2)
        assertEquals(listOf("a", "b"), map.keys())
        assertNull(map.getIndex(2))
        assertTrue(map.containsKey("b"))
        assertFalse(map.containsKey("c"))

        map.clear()
        assertTrue(map.isEmpty())
        assertNull(map.first())
        assertNull(map.last())
    }
}
