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
        assertEquals(IndexSet.from(listOf(3, 1)), set.sub(other))
        assertEquals(IndexSet.from(listOf(3, 1)), set - other)
        assertEquals(IndexSet.from(listOf(5)), set.bitand(other))
        assertEquals(IndexSet.from(listOf(3, 5, 1, 8)), set.bitor(other))
        assertEquals(IndexSet.from(listOf(3, 5, 1, 8)), set + other)
        assertEquals(IndexSet.from(listOf(3, 1, 8)), set.bitxor(other))
        assertTrue(IndexSet.from(listOf(5)).isSubset(set))
        assertTrue(set.isSuperset(IndexSet.from(listOf(5))))
        assertFalse(set.isDisjoint(other))
    }

    @Test
    fun setSliceEntriesAndTakeMethodsWorkAsExpected() {
        val set = IndexSet.from(listOf(10, 20, 30))
        assertEquals(listOf(10, 20, 30), set.asSlice())
        assertEquals(listOf(10, 20, 30), set.intoBoxedSlice())
        assertEquals(listOf(10, 20, 30), set.asEntries())
        assertEquals(listOf(10, 20, 30), set.intoEntries())
        assertEquals(20, set.index(1))

        val modifiedCount =
            set.withEntries { entries ->
                entries.add(40)
                entries.size
            }
        assertEquals(4, modifiedCount)
        assertEquals(listOf(10, 20, 30, 40), set.asList())

        assertEquals(30, set.take(30))
        assertNull(set.take(99))
        assertFalse(set.contains(30))
    }

    @Test
    fun itWorks() {
        val set = IndexSet.new<Int>()
        assertTrue(set.isEmpty())
        set.insert(1)
        set.insert(1)
        assertEquals(1, set.len())
        assertTrue(set.contains(1))
        assertEquals(1, set[0])
        assertFalse(set.isEmpty())
    }

    @Test
    fun insertFull() {
        val insert = listOf(9, 2, 7, 1, 4, 6, 13)
        val present = listOf(1, 6, 2)
        val set = IndexSet.withCapacity<Int>(insert.size)

        for ((i, elt) in insert.withIndex()) {
            assertEquals(i, set.len())
            val (index, success) = set.insertFull(elt)
            assertTrue(success)
            assertEquals(index, set.getFull(elt)?.first)
            assertEquals(i + 1, set.len())
        }

        val len = set.len()
        for (elt in present) {
            val (index, success) = set.insertFull(elt)
            assertFalse(success)
            assertEquals(index, set.getFull(elt)?.first)
            assertEquals(len, set.len())
        }
    }

    @Test
    fun insertDup() {
        val set = IndexSet.from(listOf(0, 2, 4, 6, 8))
        val (i, v) = set.getFull(0)!!
        assertEquals(5, set.len())
        assertEquals(0, i)
        assertEquals(0, v)

        val inserted = set.insert(0)
        val (i2, v2) = set.getFull(0)!!
        assertEquals(5, set.len())
        assertFalse(inserted)
        assertEquals(0, i2)
        assertEquals(0, v2)
    }

    @Test
    fun insertOrder() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val set = IndexSet.new<Int>()
        for (elt in insert) {
            set.insert(elt)
        }
        assertEquals(insert.size, set.len())
        assertEquals(insert, set.asList())
        for (i in insert.indices) {
            assertEquals(insert[i], set.getIndex(i))
        }
    }

    @Test
    fun replaceOrder() {
        val replace = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val set = IndexSet.new<Int>()
        for (elt in replace) {
            set.replace(elt)
        }
        assertEquals(replace.size, set.len())
        assertEquals(replace, set.asList())
        for (i in replace.indices) {
            assertEquals(replace[i], set.getIndex(i))
        }
    }

    @Test
    fun replaceDup() {
        val set = IndexSet.from(listOf(0, 2, 4, 6, 8))
        val (i, v) = set.getFull(0)!!
        assertEquals(5, set.len())
        assertEquals(0, i)
        assertEquals(0, v)

        val replaced = set.replace(0)
        val (i2, v2) = set.getFull(0)!!
        assertEquals(5, set.len())
        assertEquals(0, replaced)
        assertEquals(0, i2)
        assertEquals(0, v2)
    }

    @Test
    fun grow() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11)
        val notPresent = listOf(1, 3, 6, 9, 10)
        val set = IndexSet.withCapacity<Int>(insert.size)

        for ((i, elt) in insert.withIndex()) {
            assertEquals(i, set.len())
            set.insert(elt)
            assertEquals(i + 1, set.len())
            assertTrue(set.contains(elt))
            assertEquals(elt, set[i])
        }

        for (elt in insert) {
            set.insert(elt * 10)
        }
        for (elt in insert) {
            set.insert(elt * 100)
        }
        for (elt in notPresent) {
            assertFalse(set.contains(elt))
        }
    }

    @Test
    fun swapRemoveIndex() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val set = IndexSet.from(insert)
        val vector = insert.toMutableList()
        val removeSequence = listOf(3, 3, 10, 4, 5, 4, 3, 0, 1)

        for (rm in removeSequence) {
            val last = vector.removeAt(vector.size - 1)
            val outVec =
                if (rm < vector.size) {
                    val old = vector[rm]
                    vector[rm] = last
                    old
                } else {
                    last
                }
            val outSet = set.swapRemoveIndex(rm)
            assertEquals(outVec, outSet)
        }
        assertEquals(vector.size, set.len())
        assertEquals(vector, set.asList())
    }

    @Test
    fun partialEqAndEq() {
        val setA = IndexSet.new<Int>()
        setA.insert(1)
        setA.insert(2)
        val setB = setA.clone()
        assertEquals(setA.asList(), setB.asList())
        setB.swapRemove(1)
        assertFalse(setA.asList() == setB.asList())
    }

    @Test
    fun comparisons() {
        val setA = IndexSet.from(0 until 3)
        val setB = IndexSet.from(3 until 6)
        val setC = IndexSet.from(0 until 6)
        val setD = IndexSet.from(3 until 9)

        assertFalse(setA.isDisjoint(setA))
        assertTrue(setA.isSubset(setA))
        assertTrue(setA.isSuperset(setA))

        assertTrue(setA.isDisjoint(setB))
        assertTrue(setB.isDisjoint(setA))
        assertFalse(setA.isSubset(setB))
        assertFalse(setB.isSubset(setA))
        assertFalse(setA.isSuperset(setB))
        assertFalse(setB.isSuperset(setA))

        assertFalse(setA.isDisjoint(setC))
        assertFalse(setC.isDisjoint(setA))
        assertTrue(setA.isSubset(setC))
        assertFalse(setC.isSubset(setA))
        assertFalse(setA.isSuperset(setC))
        assertTrue(setC.isSuperset(setA))

        assertFalse(setC.isDisjoint(setD))
        assertFalse(setD.isDisjoint(setC))
        assertFalse(setC.isSubset(setD))
        assertFalse(setD.isSubset(setC))
        assertFalse(setC.isSuperset(setD))
        assertFalse(setD.isSuperset(setC))
    }

    @Test
    fun iterComparisons() {
        val setA = IndexSet.from(0 until 3)
        val setB = IndexSet.from(3 until 6)
        val setC = IndexSet.from(0 until 6)

        assertEquals(emptyList(), setA.difference(setA))
        assertEquals(emptyList(), setA.symmetricDifference(setA))
        assertEquals((0 until 3).toList(), setA.intersection(setA))
        assertEquals((0 until 3).toList(), setA.union(setA))

        assertEquals((0 until 3).toList(), setA.difference(setB))
        assertEquals((3 until 6).toList(), setB.difference(setA))
        assertEquals((0 until 6).toList(), setA.symmetricDifference(setB))
        assertEquals((0 until 6).toList(), setA.union(setB))

        assertEquals(emptyList(), setA.difference(setC))
        assertEquals((3 until 6).toList(), setC.difference(setA))
        assertEquals((3 until 6).toList(), setA.symmetricDifference(setC))
    }
}
