// port-lint: tests map.rs
package io.github.kotlinmania.indexmap

import io.github.kotlinmania.indexmap.map.SearchResult
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

    @Test
    fun orderedEntryHelpersSearchSortAndPartition() {
        val map = IndexMap.from(listOf(3 to "c", 1 to "a", 2 to "b"))

        assertEquals(listOf(3, 1, 2), map.intoKeys())
        assertEquals(listOf("c", "a", "b"), map.intoValues())
        assertEquals(listOf(3 to "c", 1 to "a", 2 to "b"), map.intoEntries())

        map.sortKeys(naturalOrder())
        assertEquals(listOf(1, 2, 3), map.keys())
        assertTrue(map.isSorted(naturalOrder()))
        assertTrue(map.isSortedByKey({ key, _ -> key }, naturalOrder()))
        assertEquals(SearchResult.found(1), map.binarySearchKeys(2, naturalOrder()))
        assertEquals(SearchResult.insertion(3), map.binarySearchByKey(4, { key, _ -> key }, naturalOrder()))
        assertEquals(2, map.partitionPoint { key, _ -> key < 3 })

        assertEquals(3 to null, map.insertSorted(4, "d", naturalOrder()))
        assertEquals(listOf(1, 2, 3, 4), map.keys())
        assertEquals(2 to "c", map.insertSortedBy(3, "C") { leftKey, _, rightKey, _ -> leftKey.compareTo(rightKey) })
        assertEquals("C", map[3])
    }

    @Test
    fun removalRetainReverseAndEntriesPreserveOrder() {
        val map = IndexMap.from(listOf(1 to "a", 2 to "b", 3 to "c", 4 to "d"))

        assertEquals(Triple(1, 2, "b"), map.shiftRemoveFull(2))
        assertEquals(listOf(1, 3, 4), map.keys())
        assertEquals(Triple(1, 3, "c"), map.swapRemoveFull(3))
        assertEquals(listOf(1, 4), map.keys())
        assertEquals(4 to "d", map.popIf { key, _ -> key == 4 })
        assertNull(map.popIf { key, _ -> key == 4 })

        map.extend(listOf(5 to "e", 6 to "f", 7 to "g"))
        map.retain { key, _ -> key % 2 == 1 }
        assertEquals(listOf(1, 5, 7), map.keys())
        assertEquals(0 to (1 to "a"), map.firstEntry())
        assertEquals(2 to (7 to "g"), map.lastEntry())

        map.reverse()
        assertEquals(listOf(7, 5, 1), map.keys())
        assertTrue(map.eq(IndexMap.from(listOf(7 to "g", 5 to "e", 1 to "a"))))
    }

    @Test
    fun cloneDrainSplitSpliceAndAppendMirrorOrderedEntries() {
        val map = IndexMap.withCapacityAndHasher<Int, String, String>(4, "hash")
        map.extend(listOf(1 to "a", 2 to "b", 3 to "c", 4 to "d"))
        map.reserve(2)
        map.reserveExact(1)
        map.shrinkTo(2)
        map.shrinkToFit()

        val clone = map.clone()
        assertTrue(map !== clone)
        assertTrue(map.eq(clone))
        assertEquals("kotlin.hashCode", map.hasher())
        assertEquals(map.toString(), map.fmt())
        assertNull(map.tryReserve(1))
        assertNull(map.tryReserveExact(1))
        assertEquals("a", map.index(0))
        assertEquals(1 to (2 to "b"), map.getIndexEntry(1))

        assertEquals(1, map.replaceIndex(0, 10))
        assertEquals(listOf(10, 2, 3, 4), map.keys())
        assertEquals(listOf(2 to "b", 3 to "c"), map.drain(1, 3))
        assertEquals(listOf(10, 4), map.keys())

        val tail = map.splitOff(1)
        assertEquals(listOf(10), map.keys())
        assertEquals(listOf(4), tail.keys())

        assertEquals(listOf(10 to "a"), map.splice(0, 1, listOf(5 to "e", 6 to "f")))
        assertEquals(listOf(5, 6), map.keys())
        assertEquals(listOf(6 to "f"), map.extractIf { key, _ -> key == 6 })

        map.append(tail)
        assertTrue(tail.isEmpty())
        assertEquals(listOf(5, 4), map.keys())
        assertEquals(listOf(5, 4), IndexMap.fromIter(map.asEntries()).keys())
        assertEquals(listOf(5, 4), IndexMap.withHasher<Int, String, String>("hash").also { it.extend(map.asEntries()) }.keys())
    }
}
