// port-lint: tests set.rs
package io.github.kotlinmania.indexmap

import io.github.kotlinmania.indexmap.map.SearchResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SetTest {
    @Test
    fun insertKeepsInsertionOrderAndRejectsDuplicates() {
        val set = IndexSet.withCapacity<Char>(4)

        assertTrue(set.insert('s'))
        assertTrue(set.insert('t'))
        assertTrue(set.insert('u'))
        assertFalse(set.insert('s'))

        assertEquals(3, set.len())
        assertEquals(listOf('s', 't', 'u'), set.asList())
        assertTrue(set.contains('s'))
        assertFalse(set.contains('y'))
        assertEquals(0 to 's', set.getFull('s'))
    }

    @Test
    fun insertBeforeMovesExistingValuesAroundTheInsertionPoint() {
        val set = IndexSet.new<Char>()
        set.extend('a'..'z')

        assertNull(set.getIndexOf('*'))
        assertEquals(10 to true, set.insertBefore(10, '*'))
        assertEquals(10, set.getIndexOf('*'))

        assertEquals(9 to false, set.insertBefore(10, 'a'))
        assertEquals(9, set.getIndexOf('a'))
        assertEquals(10, set.getIndexOf('*'))

        assertEquals(10 to false, set.insertBefore(10, 'z'))
        assertEquals(10, set.getIndexOf('z'))
        assertEquals(11, set.getIndexOf('*'))

        assertEquals(27, set.len())
        assertEquals(26 to false, set.insertBefore(set.len(), '*'))
        assertEquals(26, set.getIndexOf('*'))
        assertEquals(27 to true, set.insertBefore(set.len(), '+'))
        assertEquals(27, set.getIndexOf('+'))
        assertEquals(28, set.len())
    }

    @Test
    fun shiftInsertMovesExistingValuesToTheRequestedIndex() {
        val set = IndexSet.new<Char>()
        set.extend('a'..'z')

        assertTrue(set.shiftInsert(10, '*'))
        assertEquals(10, set.getIndexOf('*'))

        assertFalse(set.shiftInsert(10, 'a'))
        assertEquals(10, set.getIndexOf('a'))
        assertEquals(9, set.getIndexOf('*'))

        assertFalse(set.shiftInsert(9, 'z'))
        assertEquals(9, set.getIndexOf('z'))
        assertEquals(10, set.getIndexOf('*'))

        assertEquals(27, set.len())
        assertFalse(set.shiftInsert(set.len() - 1, '*'))
        assertEquals(26, set.getIndexOf('*'))
        assertTrue(set.shiftInsert(set.len(), '+'))
        assertEquals(27, set.getIndexOf('+'))
        assertEquals(28, set.len())
    }

    @Test
    fun shiftAndSwapRemovalHaveDifferentOrderEffects() {
        val shifted = IndexSet.from(listOf(1, 2, 3, 4))
        val swapped = IndexSet.from(listOf(1, 2, 3, 4))

        assertTrue(shifted.shiftRemove(2))
        assertEquals(listOf(1, 3, 4), shifted.asList())

        assertTrue(swapped.swapRemove(2))
        assertEquals(listOf(1, 4, 3), swapped.asList())
    }

    @Test
    fun orderedHelpersSearchSortAndPartition() {
        val set = IndexSet.from(listOf(3, 1, 2))

        assertEquals(listOf(3, 1, 2), set.intoList())
        set.sort(naturalOrder())

        assertEquals(listOf(1, 2, 3), set.asList())
        assertTrue(set.isSorted(naturalOrder()))
        assertTrue(set.isSortedByKey({ it }, naturalOrder()))
        assertEquals(SearchResult.found(1), set.binarySearch(2, naturalOrder()))
        assertEquals(SearchResult.insertion(3), set.binarySearchByKey(4, { it }, naturalOrder()))
        assertEquals(2, set.partitionPoint { it < 3 })

        assertEquals(3 to true, set.insertSorted(4, naturalOrder()))
        assertEquals(listOf(1, 2, 3, 4), set.asList())
        assertEquals(2 to false, set.insertSortedBy(3) { left, right -> left.compareTo(right) })
    }

    @Test
    fun retainRangeSpliceAndSetOperationsPreserveExpectedOrder() {
        val set = IndexSet.from(listOf(1, 2, 3, 4))

        assertEquals(1 to 2, set.shiftRemoveFull(2))
        assertEquals(listOf(1, 3, 4), set.asList())
        assertEquals(1 to 3, set.swapRemoveFull(3))
        assertEquals(listOf(1, 4), set.asList())
        assertEquals(4, set.popIf { it == 4 })
        assertNull(set.popIf { it == 4 })

        set.extend(listOf(5, 6, 7))
        set.retain { it % 2 == 1 }
        assertEquals(listOf(1, 5, 7), set.asList())
        assertEquals(listOf(5, 7), set.getRange(1, 3))

        set.reverse()
        assertEquals(listOf(7, 5, 1), set.asList())

        val tail = set.splitOff(1)
        assertEquals(listOf(7), set.asList())
        assertEquals(listOf(5, 1), tail.asList())

        assertEquals(listOf(7), set.splice(0, 1, listOf(2, 3, 5)))
        assertEquals(listOf(2, 3, 5), set.asList())
        assertEquals(listOf(2), set.extractIf { it == 2 })

        set.append(tail)
        assertTrue(tail.isEmpty())
        assertEquals(listOf(3, 5, 1), set.asList())

        val other = IndexSet.from(listOf(5, 8))
        assertEquals(listOf(3, 1), set.difference(other))
        assertEquals(listOf(5), set.intersection(other))
        assertEquals(listOf(3, 1, 8), set.symmetricDifference(other))
        assertEquals(listOf(3, 5, 1, 8), set.union(other))
        assertTrue(IndexSet.from(listOf(5)).isSubset(set))
        assertTrue(set.isSuperset(IndexSet.from(listOf(5))))
        assertFalse(set.isDisjoint(other))
    }
}
