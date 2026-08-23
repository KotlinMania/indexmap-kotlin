// port-lint: tests set/tests.rs
package io.github.kotlinmania.indexmap.set

import io.github.kotlinmania.indexmap.IndexSet
import io.github.kotlinmania.indexmap.binarySearch
import io.github.kotlinmania.indexmap.insertSorted
import io.github.kotlinmania.indexmap.isSorted
import io.github.kotlinmania.indexmap.map.SearchResult
import io.github.kotlinmania.indexmap.sortUnstable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SetTests {
    @Test
    fun itWorks() {
        val set = IndexSet.new<Int>()
        assertTrue(set.isEmpty())
        set.insert(1)
        set.insert(1)
        assertEquals(1, set.len())
        assertTrue(set.contains(1))
        assertFalse(set.isEmpty())
    }

    @Test
    fun newSet() {
        val set = IndexSet.new<String>()
        assertEquals(0, set.len())
        assertTrue(set.isEmpty())
    }

    @Test
    fun insert() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5)
        val notPresent = listOf(1, 3, 6, 9, 10)
        val set = IndexSet.withCapacity<Int>(insert.size)

        for ((i, elt) in insert.withIndex()) {
            assertEquals(i, set.len())
            set.insert(elt)
            assertEquals(i + 1, set.len())
            assertEquals(elt, set.get(value = elt))
        }

        for (elt in notPresent) {
            assertNull(set.get(value = elt))
        }
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
    fun insert2() {
        val set = IndexSet.withCapacity<Int>(16)
        val values = (0 until 16).toList() + (32 until 64).toList()

        for (i in values) {
            val oldSet = set.clone()
            set.insert(i)
            for (value in oldSet.asList()) {
                assertTrue(set.contains(value), "did not find $value in set")
            }
        }

        for (i in values) {
            assertTrue(set.contains(i), "did not find $i")
        }
    }

    @Test
    fun insertDup() {
        val elements = listOf(0, 2, 4, 6, 8)
        val set = IndexSet.from(elements)

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

        assertEquals(set.len(), set.asList().size)
        assertEquals(insert.size, set.asList().size)
        assertEquals(insert, set.asList())
        for ((i, v) in insert.withIndex()) {
            assertEquals(v, set.getIndex(i))
        }
    }

    @Test
    fun shiftInsert() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val set = IndexSet.new<Int>()

        for (elt in insert) {
            set.shiftInsert(0, elt)
        }

        assertEquals(set.len(), set.asList().size)
        assertEquals(insert.size, set.asList().size)
        assertEquals(insert.reversed(), set.asList())
        for ((i, v) in insert.reversed().withIndex()) {
            assertEquals(v, set.getIndex(i))
        }

        // insert that moves an existing entry
        set.shiftInsert(0, insert[0])
        assertEquals(insert.size, set.asList().size)
        assertEquals(insert[0], set[0])
        assertEquals(insert.drop(1).reversed(), set.asList().drop(1))
    }

    @Test
    fun replace() {
        val replace = listOf(0, 4, 2, 12, 8, 7, 11, 5)
        val notPresent = listOf(1, 3, 6, 9, 10)
        val set = IndexSet.withCapacity<Int>(replace.size)

        for ((i, elt) in replace.withIndex()) {
            assertEquals(i, set.len())
            set.replace(elt)
            assertEquals(i + 1, set.len())
            assertEquals(elt, set.get(value = elt))
        }

        for (elt in notPresent) {
            assertNull(set.get(value = elt))
        }
    }

    @Test
    fun replaceFull() {
        val replace = listOf(9, 2, 7, 1, 4, 6, 13)
        val present = listOf(1, 6, 2)
        val set = IndexSet.withCapacity<Int>(replace.size)

        for ((i, elt) in replace.withIndex()) {
            assertEquals(i, set.len())
            val (index, replaced) = set.replaceFull(elt)
            assertNull(replaced)
            assertEquals(index, set.getFull(elt)?.first)
            assertEquals(i + 1, set.len())
        }

        val len = set.len()
        for (elt in present) {
            val (index, replaced) = set.replaceFull(elt)
            assertEquals(elt, replaced)
            assertEquals(index, set.getFull(elt)?.first)
            assertEquals(len, set.len())
        }
    }

    @Test
    fun replace2() {
        val set = IndexSet.withCapacity<Int>(16)
        val values = (0 until 16).toList() + (32 until 64).toList()

        for (i in values) {
            val oldSet = set.clone()
            set.replace(i)
            for (value in oldSet.asList()) {
                assertTrue(set.contains(value), "did not find $value in set")
            }
        }

        for (i in values) {
            assertTrue(set.contains(i), "did not find $i")
        }
    }

    @Test
    fun replaceDup() {
        val elements = listOf(0, 2, 4, 6, 8)
        val set = IndexSet.from(elements)

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
    fun replaceOrder() {
        val replace = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val set = IndexSet.new<Int>()

        for (elt in replace) {
            set.replace(elt)
        }

        assertEquals(set.len(), set.asList().size)
        assertEquals(replace.size, set.asList().size)
        assertEquals(replace, set.asList())
        for ((i, v) in replace.withIndex()) {
            assertEquals(v, set.getIndex(i))
        }
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
            assertEquals(elt, set.get(value = elt))
        }

        for (elt in insert) {
            set.insert(elt * 10)
        }
        for (elt in insert) {
            set.insert(elt * 100)
        }
        for (i in 0 until 100) {
            val elt = insert[i % insert.size]
            set.insert(elt * 100 + i)
        }
        for (elt in notPresent) {
            assertNull(set.get(value = elt))
        }
    }

    @Test
    fun reserve() {
        val set = IndexSet.new<Int>()
        set.reserve(100)
        for (i in 0 until 100) {
            assertEquals(i, set.len())
            set.insert(i)
            assertEquals(i + 1, set.len())
            assertEquals(i, set.get(value = i))
        }
        set.insert(100)
        assertEquals(101, set.len())
        assertEquals(100, set.get(value = 100))
    }

    @Test
    fun tryReserve() {
        val set = IndexSet.new<Int>()
        assertNull(set.tryReserve(100))
    }

    @Test
    fun shrinkToFit() {
        val set = IndexSet.new<Int>()
        for (i in 0 until 100) {
            assertEquals(i, set.len())
            set.insert(i)
            assertEquals(i + 1, set.len())
            assertEquals(i, set.get(value = i))
            set.shrinkToFit()
            assertEquals(i + 1, set.len())
            assertEquals(i, set.get(value = i))
        }
    }

    @Test
    fun remove() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val set = IndexSet.new<Int>()

        for (elt in insert) {
            set.insert(elt)
        }

        assertEquals(set.len(), set.asList().size)
        assertEquals(insert.size, set.asList().size)
        assertEquals(insert, set.asList())

        val removeFail = listOf(99, 77)
        val remove = listOf(4, 12, 8, 7)

        for (value in removeFail) {
            assertNull(set.swapRemoveFull(value))
        }

        for (value in remove) {
            val index = set.getFull(value)!!.first
            assertEquals(index to value, set.swapRemoveFull(value))
        }

        for (value in insert) {
            assertEquals(set.contains(value), !remove.contains(value))
        }
        assertEquals(insert.size - remove.size, set.len())
        assertEquals(insert.size - remove.size, set.asList().size)
    }

    @Test
    fun swapRemoveIndex() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val set = IndexSet.new<Int>()

        for (elt in insert) {
            set.insert(elt)
        }

        val vector = insert.toMutableList()
        val removeSequence = listOf(3, 3, 10, 4, 5, 4, 3, 0, 1)

        for (rm in removeSequence) {
            val outVec = vector.removeAt(rm)
            if (rm < vector.size) {
                val last = vector.removeAt(vector.lastIndex)
                vector.add(rm, last)
            }
            val outSet = set.swapRemoveIndex(rm)!!
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
        assertEquals(setA, setB)
        setB.swapRemove(1)
        assertNotEquals(setA, setB)

        val setC = IndexSet.from(setB.asList())
        assertNotEquals(setA, setC)
        assertNotEquals(setC, setA)
    }

    @Test
    fun extend() {
        val set = IndexSet.new<Int>()
        set.extend(listOf(1, 2, 3, 4))
        set.extend(listOf(5, 6))
        assertEquals(listOf(1, 2, 3, 4, 5, 6), set.intoList())
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
        val setD = IndexSet.from((3 until 9).reversed())

        assertEquals(emptyList<Int>(), setA.difference(setA))
        assertEquals(emptyList<Int>(), setA.symmetricDifference(setA))
        assertEquals((0 until 3).toList(), setA.intersection(setA))
        assertEquals((0 until 3).toList(), setA.union(setA))

        assertEquals((0 until 3).toList(), setA.difference(setB))
        assertEquals((3 until 6).toList(), setB.difference(setA))
        assertEquals((0 until 6).toList(), setA.symmetricDifference(setB))
        assertEquals((3 until 6).toList() + (0 until 3).toList(), setB.symmetricDifference(setA))
        assertEquals(emptyList<Int>(), setA.intersection(setB))
        assertEquals(emptyList<Int>(), setB.intersection(setA))
        assertEquals((0 until 6).toList(), setA.union(setB))
        assertEquals((3 until 6).toList() + (0 until 3).toList(), setB.union(setA))

        assertEquals(emptyList<Int>(), setA.difference(setC))
        assertEquals((3 until 6).toList(), setC.difference(setA))
        assertEquals((3 until 6).toList(), setA.symmetricDifference(setC))
        assertEquals((3 until 6).toList(), setC.symmetricDifference(setA))
        assertEquals((0 until 3).toList(), setA.intersection(setC))
        assertEquals((0 until 3).toList(), setC.intersection(setA))
        assertEquals((0 until 6).toList(), setA.union(setC))
        assertEquals((0 until 6).toList(), setC.union(setA))

        assertEquals((0 until 3).toList(), setC.difference(setD))
        assertEquals((6 until 9).reversed().toList(), setD.difference(setC))
        assertEquals((0 until 3).toList() + (6 until 9).reversed().toList(), setC.symmetricDifference(setD))
        assertEquals((6 until 9).reversed().toList() + (0 until 3).toList(), setD.symmetricDifference(setC))
        assertEquals((3 until 6).toList(), setC.intersection(setD))
        assertEquals((3 until 6).reversed().toList(), setD.intersection(setC))
        assertEquals((0 until 6).toList() + (6 until 9).reversed().toList(), setC.union(setD))
        assertEquals((3 until 9).reversed().toList() + (0 until 3).toList(), setD.union(setC))
    }

    @Test
    fun fromArray() {
        val set1 = IndexSet.from(listOf(1, 2, 3, 4))
        val set2 = IndexSet.from(listOf(1, 2, 3, 4))
        assertEquals(set1, set2)
    }

    @Test
    fun take() {
        val indexSet = IndexSet.new<Int>()
        indexSet.insert(10)
        assertEquals(1, indexSet.len())

        val result = indexSet.take(10)
        assertEquals(10, result)
        assertEquals(0, indexSet.len())

        val result2 = indexSet.take(20)
        assertNull(result2)
    }

    @Test
    fun swapTake() {
        val indexSet = IndexSet.new<Int>()
        indexSet.insert(10)
        indexSet.insert(20)
        indexSet.insert(30)
        indexSet.insert(40)
        assertEquals(4, indexSet.len())

        val result = indexSet.swapTake(20)
        assertEquals(20, result)
        assertEquals(3, indexSet.len())
        assertEquals(listOf(10, 40, 30), indexSet.asList())

        val result2 = indexSet.swapTake(50)
        assertNull(result2)
    }

    @Test
    fun sortUnstable() {
        val indexSet = IndexSet.new<Int>()
        indexSet.insert(30)
        indexSet.insert(20)
        indexSet.insert(10)

        indexSet.sortUnstable()
        assertEquals(listOf(10, 20, 30), indexSet.asList())
    }

    @Test
    fun tryReserveExact() {
        val indexSet = IndexSet.new<Int>()
        indexSet.insert(10)
        indexSet.insert(20)
        indexSet.insert(30)
        indexSet.shrinkToFit()
        assertNull(indexSet.tryReserveExact(2))
    }

    @Test
    fun shiftRemoveFull() {
        val set = IndexSet.new<Int>()
        set.insert(10)
        set.insert(20)
        set.insert(30)
        set.insert(40)
        set.insert(50)

        val result1 = set.shiftRemoveFull(20)
        assertEquals(1 to 20, result1)
        assertEquals(4, set.len())
        assertEquals(listOf(10, 30, 40, 50), set.asList())

        val result2 = set.shiftRemoveFull(50)
        assertEquals(3 to 50, result2)
        assertEquals(3, set.len())
        assertEquals(listOf(10, 30, 40), set.asList())

        val result3 = set.shiftRemoveFull(60)
        assertNull(result3)
        assertEquals(3, set.len())
        assertEquals(listOf(10, 30, 40), set.asList())
    }

    @Test
    fun shiftRemoveIndex() {
        val set = IndexSet.new<Int>()
        set.insert(10)
        set.insert(20)
        set.insert(30)
        set.insert(40)
        set.insert(50)

        val result1 = set.shiftRemoveIndex(1)
        assertEquals(20, result1)
        assertEquals(4, set.len())
        assertEquals(listOf(10, 30, 40, 50), set.asList())

        val result2 = set.shiftRemoveIndex(1)
        assertEquals(30, result2)
        assertEquals(3, set.len())
        assertEquals(listOf(10, 40, 50), set.asList())

        val result3 = set.shiftRemoveIndex(3)
        assertNull(result3)
        assertEquals(3, set.len())
        assertEquals(listOf(10, 40, 50), set.asList())
    }

    @Test
    fun sortUnstableBy() {
        val set = IndexSet.from((1..10).toList())
        set.sortUnstableBy { a, b -> b.compareTo(a) }
        assertEquals((1..10).reversed().toList(), set.asList())
    }

    @Test
    fun sortBy() {
        val set = IndexSet.new<Int>()
        set.insert(3)
        set.insert(1)
        set.insert(2)
        set.sortBy { a, b -> a.compareTo(b) }
        assertEquals(listOf(1, 2, 3), set.asList())
    }

    @Test
    fun drain() {
        val set = IndexSet.new<Int>()
        set.insert(1)
        set.insert(2)
        set.insert(3)

        val drained = set.drain(0 until 2)
        assertEquals(listOf(1, 2), drained)
        assertEquals(1, set.len())
        assertEquals(listOf(3), set.asList())
    }

    @Test
    fun splitOff() {
        val set = IndexSet.from(listOf(1, 2, 3, 4, 5))
        val splitSet = set.splitOff(3)

        assertEquals(2, splitSet.len())
        assertEquals(listOf(4, 5), splitSet.asList())

        assertEquals(3, set.len())
        assertEquals(listOf(1, 2, 3), set.asList())
    }

    @Test
    fun retain() {
        val set = IndexSet.from((1..10).toList())
        set.retain { it > 4 }
        assertEquals(6, set.len())
        assertEquals((5..10).toList(), set.asList())

        set.retain { false }
        assertEquals(0, set.len())
    }

    @Test
    fun first() {
        val indexSet = IndexSet.new<Int>()
        indexSet.insert(10)
        indexSet.insert(20)
        indexSet.insert(30)

        assertEquals(10, indexSet.first())

        indexSet.clear()
        assertNull(indexSet.first())
    }

    @Test
    fun sortByKey() {
        val indexSet = IndexSet.new<Int>()
        indexSet.insert(3)
        indexSet.insert(1)
        indexSet.insert(2)
        indexSet.insert(0)
        indexSet.sortByKey { -it }
        assertEquals(listOf(3, 2, 1, 0), indexSet.asList())
    }

    @Test
    fun sortUnstableByKey() {
        val indexSet = IndexSet.new<Int>()
        indexSet.insert(3)
        indexSet.insert(1)
        indexSet.insert(2)
        indexSet.insert(0)
        indexSet.sortUnstableByKey { -it }
        assertEquals(listOf(3, 2, 1, 0), indexSet.asList())
    }

    @Test
    fun sortByCachedKey() {
        val indexSet = IndexSet.new<Int>()
        indexSet.insert(3)
        indexSet.insert(1)
        indexSet.insert(2)
        indexSet.insert(0)
        indexSet.sortByCachedKey { -it }
        assertEquals(listOf(3, 2, 1, 0), indexSet.asList())
    }

    @Test
    fun insertSorted() {
        val set = IndexSet.new<Int>()
        set.insertSorted(1)
        set.insertSorted(3)
        assertEquals(1 to true, set.insertSorted(2))
    }

    @Test
    fun binarySearch() {
        val set = IndexSet.new<Int>()
        set.insert(100)
        set.insert(300)
        set.insert(200)
        set.insert(400)
        assertEquals(SearchResult.found(2), set.binarySearch(200))
        assertEquals(SearchResult.insertion(4), set.binarySearch(500))
    }

    @Test
    fun sortedUnstableBy() {
        val set = IndexSet.from((1..10).toList())
        set.sortUnstableBy { a, b -> b.compareTo(a) }
        assertEquals((1..10).reversed().toList(), set.asList())
    }

    @Test
    fun last() {
        val set = IndexSet.new<Int>()
        set.insert(1)
        set.insert(2)
        set.insert(3)
        set.insert(4)
        set.insert(5)
        set.insert(6)

        assertEquals(6, set.last())
        set.pop()
        assertEquals(5, set.last())
        set.clear()
        assertNull(set.last())
    }

    @Test
    fun getRange() {
        val set = IndexSet.from(listOf(1, 2, 3, 4, 5))
        val result1 = set.getRange(0 until 3)
        assertEquals(listOf(1, 2, 3), result1)

        val result2 = set.getRange(0 until 0)
        assertEquals(0, result2?.size)

        val result3 = set.getRange(2, 1)
        assertNull(result3)
    }

    @Test
    fun shiftTake() {
        val set = IndexSet.new<Int>()
        set.insert(1)
        set.insert(2)
        set.insert(3)
        set.insert(4)
        set.insert(5)

        val result1 = set.shiftTake(2)
        assertEquals(2, result1)
        assertEquals(4, set.len())
        assertEquals(listOf(1, 3, 4, 5), set.asList())

        val result2 = set.shiftTake(5)
        assertEquals(5, result2)
        assertEquals(3, set.len())
        assertEquals(listOf(1, 3, 4), set.asList())

        val result3 = set.shiftTake(5)
        assertNull(result3)
        assertEquals(3, set.len())
        assertEquals(listOf(1, 3, 4), set.asList())
    }

    @Test
    fun testBinarySearchBy() {
        val b = IndexSet.from(emptyList<Int>())
        assertEquals(SearchResult.insertion(0), b.binarySearchBy { it.compareTo(5) })

        val b1 = IndexSet.from(listOf(4))
        assertEquals(SearchResult.insertion(0), b1.binarySearchBy { it.compareTo(3) })
        assertEquals(SearchResult.found(0), b1.binarySearchBy { it.compareTo(4) })
        assertEquals(SearchResult.insertion(1), b1.binarySearchBy { it.compareTo(5) })

        val b2 = IndexSet.from(listOf(1, 2, 4, 6, 8, 9))
        assertEquals(SearchResult.insertion(3), b2.binarySearchBy { it.compareTo(5) })
        assertEquals(SearchResult.found(3), b2.binarySearchBy { it.compareTo(6) })
        assertEquals(SearchResult.insertion(4), b2.binarySearchBy { it.compareTo(7) })
        assertEquals(SearchResult.found(4), b2.binarySearchBy { it.compareTo(8) })

        val b3 = IndexSet.from(listOf(1, 2, 4, 5, 6, 8))
        assertEquals(SearchResult.insertion(6), b3.binarySearchBy { it.compareTo(9) })

        val b4 = IndexSet.from(listOf(1, 2, 4, 6, 7, 8, 9))
        assertEquals(SearchResult.found(3), b4.binarySearchBy { it.compareTo(6) })
        assertEquals(SearchResult.insertion(3), b4.binarySearchBy { it.compareTo(5) })
        assertEquals(SearchResult.found(5), b4.binarySearchBy { it.compareTo(8) })

        val b5 = IndexSet.from(listOf(1, 2, 4, 5, 6, 8, 9))
        assertEquals(SearchResult.insertion(5), b5.binarySearchBy { it.compareTo(7) })
        assertEquals(SearchResult.insertion(0), b5.binarySearchBy { it.compareTo(0) })

        val b6 = IndexSet.from(listOf(1, 3, 3, 3, 7))
        assertEquals(SearchResult.insertion(0), b6.binarySearchBy { it.compareTo(0) })
        assertEquals(SearchResult.found(0), b6.binarySearchBy { it.compareTo(1) })
        assertEquals(SearchResult.insertion(1), b6.binarySearchBy { it.compareTo(2) })
        val res3 = b6.binarySearchBy { it.compareTo(3) }
        assertTrue(res3.found && res3.index in 1..2)
        assertEquals(SearchResult.insertion(2), b6.binarySearchBy { it.compareTo(4) })
        assertEquals(SearchResult.insertion(2), b6.binarySearchBy { it.compareTo(5) })
        assertEquals(SearchResult.insertion(2), b6.binarySearchBy { it.compareTo(6) })
        assertEquals(SearchResult.found(2), b6.binarySearchBy { it.compareTo(7) })
        assertEquals(SearchResult.insertion(3), b6.binarySearchBy { it.compareTo(8) })
    }

    @Test
    fun testBinarySearchByKey() {
        val b = IndexSet.from(emptyList<Int>())
        assertEquals(SearchResult.insertion(0), b.binarySearchByKey(5) { it })

        val b1 = IndexSet.from(listOf(4))
        assertEquals(SearchResult.insertion(0), b1.binarySearchByKey(3) { it })
        assertEquals(SearchResult.found(0), b1.binarySearchByKey(4) { it })
        assertEquals(SearchResult.insertion(1), b1.binarySearchByKey(5) { it })

        val b2 = IndexSet.from(listOf(1, 2, 4, 6, 8, 9))
        assertEquals(SearchResult.insertion(3), b2.binarySearchByKey(5) { it })
        assertEquals(SearchResult.found(3), b2.binarySearchByKey(6) { it })
        assertEquals(SearchResult.insertion(4), b2.binarySearchByKey(7) { it })
        assertEquals(SearchResult.found(4), b2.binarySearchByKey(8) { it })

        val b3 = IndexSet.from(listOf(1, 2, 4, 5, 6, 8))
        assertEquals(SearchResult.insertion(6), b3.binarySearchByKey(9) { it })

        val b4 = IndexSet.from(listOf(1, 2, 4, 6, 7, 8, 9))
        assertEquals(SearchResult.found(3), b4.binarySearchByKey(6) { it })
        assertEquals(SearchResult.insertion(3), b4.binarySearchByKey(5) { it })
        assertEquals(SearchResult.found(5), b4.binarySearchByKey(8) { it })

        val b5 = IndexSet.from(listOf(1, 2, 4, 5, 6, 8, 9))
        assertEquals(SearchResult.insertion(5), b5.binarySearchByKey(7) { it })
        assertEquals(SearchResult.insertion(0), b5.binarySearchByKey(0) { it })

        val b6 = IndexSet.from(listOf(1, 3, 3, 3, 7))
        assertEquals(SearchResult.insertion(0), b6.binarySearchByKey(0) { it })
        assertEquals(SearchResult.found(0), b6.binarySearchByKey(1) { it })
        assertEquals(SearchResult.insertion(1), b6.binarySearchByKey(2) { it })
        val res3 = b6.binarySearchByKey(3) { it }
        assertTrue(res3.found && res3.index in 1..2)
        assertEquals(SearchResult.insertion(2), b6.binarySearchByKey(4) { it })
        assertEquals(SearchResult.insertion(2), b6.binarySearchByKey(5) { it })
        assertEquals(SearchResult.insertion(2), b6.binarySearchByKey(6) { it })
        assertEquals(SearchResult.found(2), b6.binarySearchByKey(7) { it })
        assertEquals(SearchResult.insertion(3), b6.binarySearchByKey(8) { it })
    }

    @Test
    fun testPartitionPoint() {
        val b = IndexSet.from(emptyList<Int>())
        assertEquals(0, b.partitionPoint { it < 5 })

        val b1 = IndexSet.from(listOf(4))
        assertEquals(0, b1.partitionPoint { it < 3 })
        assertEquals(0, b1.partitionPoint { it < 4 })
        assertEquals(1, b1.partitionPoint { it < 5 })

        val b2 = IndexSet.from(listOf(1, 2, 4, 6, 8, 9))
        assertEquals(3, b2.partitionPoint { it < 5 })
        assertEquals(3, b2.partitionPoint { it < 6 })
        assertEquals(4, b2.partitionPoint { it < 7 })
        assertEquals(4, b2.partitionPoint { it < 8 })

        val b3 = IndexSet.from(listOf(1, 2, 4, 5, 6, 8))
        assertEquals(6, b3.partitionPoint { it < 9 })

        val b4 = IndexSet.from(listOf(1, 2, 4, 6, 7, 8, 9))
        assertEquals(3, b4.partitionPoint { it < 6 })
        assertEquals(3, b4.partitionPoint { it < 5 })
        assertEquals(5, b4.partitionPoint { it < 8 })

        val b5 = IndexSet.from(listOf(1, 2, 4, 5, 6, 8, 9))
        assertEquals(5, b5.partitionPoint { it < 7 })
        assertEquals(0, b5.partitionPoint { it < 0 })

        val b6 = IndexSet.from(listOf(1, 3, 3, 3, 7))
        assertEquals(0, b6.partitionPoint { it < 0 })
        assertEquals(0, b6.partitionPoint { it < 1 })
        assertEquals(1, b6.partitionPoint { it < 2 })
        assertEquals(1, b6.partitionPoint { it < 3 })
        assertEquals(2, b6.partitionPoint { it < 4 })
        assertEquals(2, b6.partitionPoint { it < 5 })
        assertEquals(2, b6.partitionPoint { it < 6 })
        assertEquals(2, b6.partitionPoint { it < 7 })
        assertEquals(3, b6.partitionPoint { it < 8 })
    }

    @Test
    fun isSorted() {
        fun expect(set: IndexSet<Int>, e: List<Boolean>) {
            assertEquals(e[0], set.isSorted())
            assertEquals(e[1], set.isSortedBy { v1, v2 -> v1 < v2 })
            assertEquals(e[2], set.isSortedBy { v1, v2 -> v1 > v2 })
            assertEquals(e[3], set.isSortedByKey { it })
        }

        val set = IndexSet.from(0 until 10)
        expect(set, listOf(true, true, false, true))

        set.replaceIndex(5, -1)
        expect(set, listOf(false, false, false, false))
    }

    @Test
    fun isSortedTrivial() {
        fun expect(set: IndexSet<Int>, e: List<Boolean>) {
            assertEquals(e[0], set.isSorted())
            assertEquals(e[1], set.isSortedBy { _, _ -> true })
            assertEquals(e[2], set.isSortedBy { _, _ -> false })
            assertEquals(e[3], set.isSortedByKey { 0.0 })
        }

        val set = IndexSet.new<Int>()
        expect(set, listOf(true, true, true, true))

        set.insert(0)
        expect(set, listOf(true, true, true, true))

        set.insert(1)
        expect(set, listOf(true, true, false, true))

        set.reverse()
        expect(set, listOf(false, true, false, true))
    }
}
