// port-lint: tests map/tests.rs
package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.GetDisjointMutError
import io.github.kotlinmania.indexmap.IndexMap
import io.github.kotlinmania.indexmap.indexmapOf
import io.github.kotlinmania.indexmap.insertSorted
import io.github.kotlinmania.indexmap.isSorted
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapTests {
    @Test
    fun itWorks() {
        val map = IndexMap.new<Int, Unit>()
        assertTrue(map.isEmpty())
        map.insert(1, Unit)
        map.insert(1, Unit)
        assertEquals(1, map.len())
        assertTrue(map.containsKey(1))
        assertFalse(map.isEmpty())
    }

    @Test
    fun new() {
        val map = IndexMap.new<String, String>()
        assertEquals(0, map.len())
        assertTrue(map.isEmpty())
    }

    @Test
    fun insert() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5)
        val notPresent = listOf(1, 3, 6, 9, 10)
        val map = IndexMap.withCapacity<Int, Int>(insert.size)

        for ((i, elt) in insert.withIndex()) {
            assertEquals(i, map.len())
            map.insert(elt, elt)
            assertEquals(i + 1, map.len())
            assertEquals(elt, map[elt])
            assertEquals(elt, map.get(elt))
        }

        for (elt in notPresent) {
            assertNull(map[elt])
        }
    }

    @Test
    fun insertFull() {
        val insert = listOf(9, 2, 7, 1, 4, 6, 13)
        val present = listOf(1, 6, 2)
        val map = IndexMap.withCapacity<Int, Int>(insert.size)

        for ((i, elt) in insert.withIndex()) {
            assertEquals(i, map.len())
            val (index, existing) = map.insertFull(elt, elt)
            assertNull(existing)
            assertEquals(index, map.getFull(elt)?.first)
            assertEquals(i + 1, map.len())
        }

        val len = map.len()
        for (elt in present) {
            val (index, existing) = map.insertFull(elt, elt)
            assertEquals(elt, existing)
            assertEquals(index, map.getFull(elt)?.first)
            assertEquals(len, map.len())
        }
    }

    @Test
    fun insert2() {
        val map = IndexMap.withCapacity<Int, Unit>(16)
        val keys = (0 until 16).toList() + (32 until 64).toList()

        for (i in keys) {
            val oldMap = map.clone()
            map.insert(i, Unit)
            for (key in oldMap.keys()) {
                assertTrue(map.containsKey(key), "did not find $key in map")
            }
        }

        for (i in keys) {
            assertTrue(map.containsKey(i), "did not find $i")
        }
    }

    @Test
    fun insertOrder() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val map = IndexMap.new<Int, Unit>()

        for (elt in insert) {
            map.insert(elt, Unit)
        }

        assertEquals(map.len(), map.keys().size)
        assertEquals(insert.size, map.keys().size)
        assertEquals(insert, map.keys())
        for ((i, k) in insert.withIndex()) {
            assertEquals(k, map.getIndex(i)?.first)
        }
    }

    @Test
    fun shiftInsert() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val map = IndexMap.new<Int, Unit>()

        for (elt in insert) {
            map.shiftInsert(0, elt, Unit)
        }

        assertEquals(map.len(), map.keys().size)
        assertEquals(insert.size, map.keys().size)
        assertEquals(insert.reversed(), map.keys())
        for ((i, k) in insert.reversed().withIndex()) {
            assertEquals(k, map.getIndex(i)?.first)
        }

        // insert that moves an existing entry
        map.shiftInsert(0, insert[0], Unit)
        assertEquals(insert.size, map.keys().size)
        assertEquals(insert[0], map.keys()[0])
        assertEquals(insert.drop(1).reversed(), map.keys().drop(1))
    }

    @Test
    fun insertSortedBad() {
        val map = IndexMap.new<Int, Unit>()
        map.insert(10, Unit)
        for (i in 0 until 10) {
            map.insert(i, Unit)
        }

        assertEquals(10 to Unit, map.first())
        map.insertSorted(10, Unit)
        assertEquals(10 to Unit, map.last())
        assertEquals((0..10).toList(), map.keys())

        map.moveIndex(5, 0)
        map.moveIndex(6, 10)
        assertEquals(5 to Unit, map.first())
        assertEquals(6 to Unit, map.last())
        map.insertSorted(5, Unit)
        map.insertSorted(6, Unit)
        assertEquals((0..10).toList(), map.keys())
    }

    @Test
    fun grow() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11)
        val notPresent = listOf(1, 3, 6, 9, 10)
        val map = IndexMap.withCapacity<Int, Int>(insert.size)

        for ((i, elt) in insert.withIndex()) {
            assertEquals(i, map.len())
            map.insert(elt, elt)
            assertEquals(i + 1, map.len())
            assertEquals(elt, map[elt])
        }

        for (elt in insert) {
            map.insert(elt * 10, elt)
        }
        for (elt in insert) {
            map.insert(elt * 100, elt)
        }
        for (i in 0 until 100) {
            val elt = insert[i % insert.size]
            map.insert(elt * 100 + i, elt)
        }
        for (elt in notPresent) {
            assertNull(map[elt])
        }
    }

    @Test
    fun reserve() {
        val map = IndexMap.new<Int, Int>()
        map.reserve(100)
        for (i in 0 until 100) {
            assertEquals(i, map.len())
            map.insert(i, i * i)
            assertEquals(i + 1, map.len())
            assertEquals(i * i, map[i])
        }
        map.insert(100, Int.MAX_VALUE)
        assertEquals(101, map.len())
        assertEquals(Int.MAX_VALUE, map[100])
    }

    @Test
    fun tryReserve() {
        val map = IndexMap.new<Int, Int>()
        assertNull(map.tryReserve(100))
    }

    @Test
    fun shrinkToFit() {
        val map = IndexMap.new<Int, Int>()
        for (i in 0 until 100) {
            assertEquals(i, map.len())
            map.insert(i, i * i)
            assertEquals(i + 1, map.len())
            assertEquals(i * i, map[i])
            map.shrinkToFit()
            assertEquals(i + 1, map.len())
            assertEquals(i * i, map[i])
        }
    }

    @Test
    fun remove() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val map = IndexMap.new<Int, Int>()

        for (elt in insert) {
            map.insert(elt, elt)
        }

        assertEquals(map.len(), map.keys().size)
        assertEquals(insert.size, map.keys().size)
        assertEquals(insert, map.keys())

        val removeFail = listOf(99, 77)
        val remove = listOf(4, 12, 8, 7)

        for (key in removeFail) {
            assertNull(map.swapRemoveFull(key))
        }

        for (key in remove) {
            val index = map.getFull(key)!!.first
            assertEquals(Triple(index, key, key), map.swapRemoveFull(key))
        }

        for (key in insert) {
            assertEquals(map.containsKey(key), !remove.contains(key))
        }
        assertEquals(insert.size - remove.size, map.len())
        assertEquals(insert.size - remove.size, map.keys().size)
    }

    @Test
    fun removeToEmpty() {
        val map = indexmapOf(0 to 0, 4 to 4, 5 to 5)
        assertEquals(5, map.swapRemove(5))
        assertEquals(4, map.swapRemove(4))
        assertEquals(0, map.swapRemove(0))
        assertTrue(map.isEmpty())
    }

    @Test
    fun swapRemoveIndex() {
        val insert = listOf(0, 4, 2, 12, 8, 7, 11, 5, 3, 17, 19, 22, 23)
        val map = IndexMap.new<Int, Int>()

        for (elt in insert) {
            map.insert(elt, elt * 2)
        }

        val vector = insert.toMutableList()
        val removeSequence = listOf(3, 3, 10, 4, 5, 4, 3, 0, 1)

        for (rm in removeSequence) {
            val outVec = vector.removeAt(rm)
            if (rm < vector.size) {
                val last = vector.removeAt(vector.lastIndex)
                vector.add(rm, last)
            }
            val (outMapKey, _) = map.swapRemoveIndex(rm)!!
            assertEquals(outVec, outMapKey)
        }
        assertEquals(vector.size, map.len())
        assertEquals(vector, map.keys())
    }

    @Test
    fun partialEqAndEq() {
        val mapA = IndexMap.new<Int, String>()
        mapA.insert(1, "1")
        mapA.insert(2, "2")
        val mapB = mapA.clone()
        assertEquals(mapA, mapB)
        mapB.swapRemove(1)
        assertNotEquals(mapA, mapB)

        val mapC = IndexMap.new<Int, String>()
        for ((k, v) in mapB.asEntries()) {
            mapC.insert(k, v)
        }
        assertNotEquals(mapA, mapC)
    }

    @Test
    fun extend() {
        val map = IndexMap.new<Int, Int>()
        map.extend(listOf(1 to 2, 3 to 4))
        map.extend(listOf(5 to 6))
        assertEquals(
            listOf(1 to 2, 3 to 4, 5 to 6),
            map.intoEntries(),
        )
    }

    @Test
    fun entry() {
        val map = IndexMap.new<Int, String>()
        map.insert(1, "1")
        map.insert(2, "2")

        val e3 = map.entry(3)
        assertEquals(2, e3.index())
        assertEquals("3", e3.orInsert("3"))

        val e2 = map.entry(2)
        assertEquals(1, e2.index())
        assertEquals(2, e2.key())
        assertTrue(e2 is Entry.Occupied)
        assertEquals("2", e2.entry.get())
        assertEquals("2", e2.orInsert("4"))
    }

    @Test
    fun entryAndModify() {
        val map = IndexMap.new<Int, String>()
        map.insert(1, "1")
        map.entry(1).andModify { "2" }
        assertEquals("2", map[1])

        map.entry(2).andModify { "doesn't exist" }
        assertNull(map[2])
    }

    enum class TestEnum {
        DefaultValue,
        NonDefaultValue;

        companion object {
            fun default(): TestEnum = DefaultValue
        }
    }

    @Test
    fun entryOrDefault() {
        val map = IndexMap.new<Int, String>()
        map.insert(1, TestEnum.NonDefaultValue.name)
        assertEquals(TestEnum.NonDefaultValue.name, map.entry(1).orDefault(TestEnum.default().name))
        assertEquals(TestEnum.DefaultValue.name, map.entry(2).orDefault(TestEnum.default().name))
    }

    @Test
    fun occupiedEntryKey() {
        class Key(val value: Int) {
            override fun equals(other: Any?): Boolean = other is Key && other.value == value
            override fun hashCode(): Int = value
        }
        val k1 = Key(1)
        val k2 = Key(1)
        val map = IndexMap.new<Key, String>()
        map.insert(k1, "value")
        when (val e = map.entry(k2)) {
            is Entry.Occupied -> {
                assertTrue(e.key() === k1)
                assertFalse(e.key() === k2)
            }
            is Entry.Vacant -> error("expected occupied")
        }
    }

    @Test
    fun getIndexEntry() {
        val map = IndexMap.new<Int, String>()

        assertNull(map.getIndexEntry(0))
        assertNull(map.firstEntry())
        assertNull(map.lastEntry())

        map.insert(0, "0")
        map.insert(1, "1")
        map.insert(2, "2")
        map.insert(3, "3")

        assertNull(map.getIndexEntry(4))

        val e1 = map.getIndexEntry(1)!!.second
        assertEquals(1, e1.first)
        assertEquals("1", e1.second)
        assertEquals("1", map.swapRemove(1))

        assertEquals("3", map.insert(3, "4"))
        assertEquals("4", map[3])

        val firstE = map.firstEntry()!!.second
        assertEquals(0, firstE.first)
        assertEquals("0", firstE.second)

        val lastE = map.lastEntry()!!.second
        assertEquals(2, lastE.first)
        assertEquals("2", lastE.second)
    }

    @Test
    fun fromEntries() {
        val map = IndexMap.from(listOf(1 to "1", 2 to "2", 3 to "3"))
        when (val e = map.entry(1)) {
            is Entry.Occupied -> {
                val indexed = IndexedEntry.from(e.entry)
                assertEquals(0, indexed.index())
                assertEquals(1, indexed.key())
                assertEquals("1", indexed.get())
            }
            is Entry.Vacant -> error("expected occupied")
        }
        val e = map.getIndexEntry(1)
        assertTrue(e != null)
        val occupied = OccupiedEntry.from(IndexedEntry(map, 1))
        assertEquals(1, occupied.index())
        assertEquals(2, occupied.key())
        assertEquals("2", occupied.get())
    }

    @Test
    fun keys() {
        val vec = listOf(1 to 'a', 2 to 'b', 3 to 'c')
        val map = IndexMap.from(vec)
        val keys = map.keys()
        assertEquals(3, keys.size)
        assertTrue(keys.contains(1))
        assertTrue(keys.contains(2))
        assertTrue(keys.contains(3))
    }

    @Test
    fun intoKeys() {
        val vec = listOf(1 to 'a', 2 to 'b', 3 to 'c')
        val map = IndexMap.from(vec)
        val keys = map.intoKeys()
        assertEquals(3, keys.size)
        assertTrue(keys.contains(1))
        assertTrue(keys.contains(2))
        assertTrue(keys.contains(3))
    }

    @Test
    fun values() {
        val vec = listOf(1 to 'a', 2 to 'b', 3 to 'c')
        val map = IndexMap.from(vec)
        val values = map.values()
        assertEquals(3, values.size)
        assertTrue(values.contains('a'))
        assertTrue(values.contains('b'))
        assertTrue(values.contains('c'))
    }

    @Test
    fun valuesMut() {
        val vec = listOf(1 to 1, 2 to 2, 3 to 3)
        val map = IndexMap.from(vec)
        for ((k, _) in vec) {
            val v = map[k]!!
            map.insert(k, v * 2)
        }
        val values = map.values()
        assertEquals(3, values.size)
        assertTrue(values.contains(2))
        assertTrue(values.contains(4))
        assertTrue(values.contains(6))
    }

    @Test
    fun intoValues() {
        val vec = listOf(1 to 'a', 2 to 'b', 3 to 'c')
        val map = IndexMap.from(vec)
        val values = map.intoValues()
        assertEquals(3, values.size)
        assertTrue(values.contains('a'))
        assertTrue(values.contains('b'))
        assertTrue(values.contains('c'))
    }

    @Test
    fun drainRange() {
        for (range in listOf(IntRange(0, -1), 10 until 90, 80 until 90, 20 until 30)) {
            val vec = (0 until 100).toMutableList()
            val map = IndexMap.new<Int, Unit>()
            for (i in 0 until 100) {
                map.insert(i, Unit)
            }
            vec.subList(range.first, range.last + 1).clear()
            map.drain(range.first, range.last + 1)
            assertEquals(vec, map.keys())
            for ((i, x) in vec.withIndex()) {
                assertEquals(i, map.getIndexOf(x))
            }
        }
    }

    @Test
    fun fromArray() {
        val map = IndexMap.from(listOf(1 to 2, 3 to 4))
        val expected = IndexMap.new<Int, Int>()
        expected.insert(1, 2)
        expected.insert(3, 4)
        assertEquals(map, expected)
    }

    @Test
    fun iterDefault() {
        fun <T : Iterator<*>> assertDefault(iter: T) {
            assertFalse(iter.hasNext())
        }
        assertDefault(IndexMap.new<Int, String>().iterator())
        assertDefault(IndexMap.new<Int, String>().keys().iterator())
        assertDefault(IndexMap.new<Int, String>().values().iterator())
    }

    @Test
    fun getIndexMut2() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 2)
        map.insert(3, 4)
        map.insert(5, 6)

        val entry = map.getIndex(0)
        assertTrue(entry != null)
        assertEquals(1, entry.first)
        assertEquals(2, entry.second)
    }

    @Test
    fun shiftShiftRemoveIndex() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 2)
        map.insert(3, 4)
        map.insert(5, 6)
        map.insert(7, 8)
        map.insert(9, 10)

        val result1 = map.shiftRemoveIndex(1)
        assertEquals(3 to 4, result1)
        assertEquals(4, map.len())
        assertEquals(listOf(1 to 2, 5 to 6, 7 to 8, 9 to 10), map.asEntries())

        val result2 = map.shiftRemoveIndex(1)
        assertEquals(5 to 6, result2)
        assertEquals(3, map.len())
        assertEquals(listOf(1 to 2, 7 to 8, 9 to 10), map.asEntries())

        val result3 = map.shiftRemoveIndex(2)
        assertEquals(9 to 10, result3)
        assertEquals(2, map.len())
        assertEquals(listOf(1 to 2, 7 to 8), map.asEntries())

        val result4 = map.shiftRemoveIndex(2)
        assertNull(result4)
        assertEquals(2, map.len())
        assertEquals(listOf(1 to 2, 7 to 8), map.asEntries())
    }

    @Test
    fun shiftRemoveEntry() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 2)
        map.insert(3, 4)
        map.insert(5, 6)
        map.insert(7, 8)
        map.insert(9, 10)

        val result1 = map.shiftRemoveEntry(3)
        assertEquals(3 to 4, result1)
        assertEquals(4, map.len())
        assertEquals(listOf(1 to 2, 5 to 6, 7 to 8, 9 to 10), map.asEntries())

        val result2 = map.shiftRemoveEntry(9)
        assertEquals(9 to 10, result2)
        assertEquals(3, map.len())
        assertEquals(listOf(1 to 2, 5 to 6, 7 to 8), map.asEntries())

        val result3 = map.shiftRemoveEntry(9)
        assertNull(result3)
        assertEquals(3, map.len())
        assertEquals(listOf(1 to 2, 5 to 6, 7 to 8), map.asEntries())
    }

    @Test
    fun shiftRemoveFull() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 2)
        map.insert(3, 4)
        map.insert(5, 6)
        map.insert(7, 8)
        map.insert(9, 10)

        val result1 = map.shiftRemoveFull(3)
        assertEquals(Triple(1, 3, 4), result1)
        assertEquals(4, map.len())
        assertEquals(listOf(1 to 2, 5 to 6, 7 to 8, 9 to 10), map.asEntries())

        val result2 = map.shiftRemoveFull(9)
        assertEquals(Triple(3, 9, 10), result2)
        assertEquals(3, map.len())
        assertEquals(listOf(1 to 2, 5 to 6, 7 to 8), map.asEntries())

        val result3 = map.shiftRemoveFull(9)
        assertNull(result3)
        assertEquals(3, map.len())
        assertEquals(listOf(1 to 2, 5 to 6, 7 to 8), map.asEntries())
    }

    @Test
    fun sortedUnstableBy() {
        val map = IndexMap.new<Int, Int>()
        map.extend(listOf(1 to 10, 2 to 20, 3 to 30, 4 to 40, 5 to 50))
        val sorted = map.sortedUnstableBy { _, b, _, d -> d.compareTo(b) }
        assertEquals(
            listOf(5 to 50, 4 to 40, 3 to 30, 2 to 20, 1 to 10),
            sorted,
        )
    }

    @Test
    fun intoBoxedSlice() {
        val map = IndexMap.new<Int, Int>()
        for (i in 0 until 5) {
            map.insert(i, i * 10)
        }
        val boxedSlice = map.asSlice()
        assertEquals(5, boxedSlice.len())
        assertEquals(
            listOf(0 to 0, 1 to 10, 2 to 20, 3 to 30, 4 to 40),
            boxedSlice.toList(),
        )
    }

    @Test
    fun lastMut() {
        val map = IndexMap.new<String, Int>()
        assertNull(map.lastMut())

        map.insert("key1", 1)
        map.insert("key2", 2)
        map.insert("key3", 3)
        assertEquals("key3" to 3, map.lastMut())
        map.insert("key3", 4)
        assertEquals(4, map["key3"])
    }

    @Test
    fun insertBeforeOob() {
        val map = IndexMap.new<Char, Unit>()
        map.insertBefore(0, 'a', Unit)
        map.insertBefore(1, 'b', Unit)
        assertFailsWith<IllegalArgumentException> {
            map.insertBefore(3, 'd', Unit)
        }
    }

    @Test
    fun clear() {
        val map = IndexMap.new<Int, Int>()
        map.extend(listOf(1 to 10, 2 to 20, 3 to 30, 4 to 40, 5 to 50))
        map.clear()
        assertEquals(0, map.len())
    }

    @Test
    fun getRange() {
        val indexMap = IndexMap.new<Int, Int>()
        indexMap.insert(1, 10)
        indexMap.insert(2, 20)
        indexMap.insert(3, 30)
        indexMap.insert(4, 40)
        indexMap.insert(5, 50)

        val result1 = indexMap.getRange(IntRange(2, 1))
        assertTrue(result1!!.isEmpty())

        val result2 = indexMap.getRange(4, 2)
        assertNull(result2)

        val result3 = indexMap.getRange(2, 4)
        val slice = result3!!
        assertEquals(2, slice.len())
        assertEquals(listOf(3 to 30, 4 to 40), slice.toList())
    }

    @Test
    fun getRangeMut() {
        val indexMap = IndexMap.new<Int, Int>()
        indexMap.insert(1, 10)
        indexMap.insert(2, 20)
        indexMap.insert(3, 30)
        indexMap.insert(4, 40)
        indexMap.insert(5, 50)

        val result1 = indexMap.getRange(IntRange(2, 1))
        assertTrue(result1 != null && result1.isEmpty())

        val result2 = indexMap.getRange(4, 2)
        assertNull(result2)

        val result3 = indexMap.getRange(2, 4)
        val slice = result3!!
        assertEquals(2, slice.len())
        assertEquals(listOf(3 to 30, 4 to 40), slice.toList())
    }

    @Test
    fun shiftInsertOob() {
        val map = IndexMap.new<Int, Int>()
        map.shiftInsert(0, 1, 10)
        map.shiftInsert(1, 2, 20)
        map.shiftInsert(2, 3, 30)
        assertFailsWith<IllegalArgumentException> {
            map.shiftInsert(5, 4, 40)
        }
    }

    @Test
    fun testBinarySearchBy() {
        val b = IndexMap.new<Int, Int>()
        assertEquals(SearchResult.insertion(0), b.binarySearchBy { _, x -> x.compareTo(5) })

        val b1 = IndexMap.from(listOf(100 to 4))
        assertEquals(SearchResult.insertion(0), b1.binarySearchBy { _, x -> x.compareTo(3) })
        assertEquals(SearchResult.found(0), b1.binarySearchBy { _, x -> x.compareTo(4) })
        assertEquals(SearchResult.insertion(1), b1.binarySearchBy { _, x -> x.compareTo(5) })

        val b2 = IndexMap.from(listOf(1, 2, 4, 6, 8, 9).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.insertion(3), b2.binarySearchBy { _, x -> x.compareTo(5) })
        assertEquals(SearchResult.found(3), b2.binarySearchBy { _, x -> x.compareTo(6) })
        assertEquals(SearchResult.insertion(4), b2.binarySearchBy { _, x -> x.compareTo(7) })
        assertEquals(SearchResult.found(4), b2.binarySearchBy { _, x -> x.compareTo(8) })

        val b3 = IndexMap.from(listOf(1, 2, 4, 5, 6, 8).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.insertion(6), b3.binarySearchBy { _, x -> x.compareTo(9) })

        val b4 = IndexMap.from(listOf(1, 2, 4, 6, 7, 8, 9).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.found(3), b4.binarySearchBy { _, x -> x.compareTo(6) })
        assertEquals(SearchResult.insertion(3), b4.binarySearchBy { _, x -> x.compareTo(5) })
        assertEquals(SearchResult.found(5), b4.binarySearchBy { _, x -> x.compareTo(8) })

        val b5 = IndexMap.from(listOf(1, 2, 4, 5, 6, 8, 9).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.insertion(5), b5.binarySearchBy { _, x -> x.compareTo(7) })
        assertEquals(SearchResult.insertion(0), b5.binarySearchBy { _, x -> x.compareTo(0) })

        val b6 = IndexMap.from(listOf(1, 3, 3, 3, 7).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.insertion(0), b6.binarySearchBy { _, x -> x.compareTo(0) })
        assertEquals(SearchResult.found(0), b6.binarySearchBy { _, x -> x.compareTo(1) })
        assertEquals(SearchResult.insertion(1), b6.binarySearchBy { _, x -> x.compareTo(2) })
        val res3 = b6.binarySearchBy { _, x -> x.compareTo(3) }
        assertTrue(res3.found && res3.index in 1..3)
        assertEquals(SearchResult.insertion(4), b6.binarySearchBy { _, x -> x.compareTo(4) })
        assertEquals(SearchResult.insertion(4), b6.binarySearchBy { _, x -> x.compareTo(5) })
        assertEquals(SearchResult.insertion(4), b6.binarySearchBy { _, x -> x.compareTo(6) })
        assertEquals(SearchResult.found(4), b6.binarySearchBy { _, x -> x.compareTo(7) })
        assertEquals(SearchResult.insertion(5), b6.binarySearchBy { _, x -> x.compareTo(8) })
    }

    @Test
    fun testBinarySearchByKey() {
        val b = IndexMap.new<Int, Int>()
        assertEquals(SearchResult.insertion(0), b.binarySearchByKey(5) { _, x -> x })

        val b1 = IndexMap.from(listOf(100 to 4))
        assertEquals(SearchResult.insertion(0), b1.binarySearchByKey(3) { _, x -> x })
        assertEquals(SearchResult.found(0), b1.binarySearchByKey(4) { _, x -> x })
        assertEquals(SearchResult.insertion(1), b1.binarySearchByKey(5) { _, x -> x })

        val b2 = IndexMap.from(listOf(1, 2, 4, 6, 8, 9).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.insertion(3), b2.binarySearchByKey(5) { _, x -> x })
        assertEquals(SearchResult.found(3), b2.binarySearchByKey(6) { _, x -> x })
        assertEquals(SearchResult.insertion(4), b2.binarySearchByKey(7) { _, x -> x })
        assertEquals(SearchResult.found(4), b2.binarySearchByKey(8) { _, x -> x })

        val b3 = IndexMap.from(listOf(1, 2, 4, 5, 6, 8).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.insertion(6), b3.binarySearchByKey(9) { _, x -> x })

        val b4 = IndexMap.from(listOf(1, 2, 4, 6, 7, 8, 9).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.found(3), b4.binarySearchByKey(6) { _, x -> x })
        assertEquals(SearchResult.insertion(3), b4.binarySearchByKey(5) { _, x -> x })
        assertEquals(SearchResult.found(5), b4.binarySearchByKey(8) { _, x -> x })

        val b5 = IndexMap.from(listOf(1, 2, 4, 5, 6, 8, 9).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.insertion(5), b5.binarySearchByKey(7) { _, x -> x })
        assertEquals(SearchResult.insertion(0), b5.binarySearchByKey(0) { _, x -> x })

        val b6 = IndexMap.from(listOf(1, 3, 3, 3, 7).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(SearchResult.insertion(0), b6.binarySearchByKey(0) { _, x -> x })
        assertEquals(SearchResult.found(0), b6.binarySearchByKey(1) { _, x -> x })
        assertEquals(SearchResult.insertion(1), b6.binarySearchByKey(2) { _, x -> x })
        val res3 = b6.binarySearchByKey(3) { _, x -> x }
        assertTrue(res3.found && res3.index in 1..3)
        assertEquals(SearchResult.insertion(4), b6.binarySearchByKey(4) { _, x -> x })
        assertEquals(SearchResult.insertion(4), b6.binarySearchByKey(5) { _, x -> x })
        assertEquals(SearchResult.insertion(4), b6.binarySearchByKey(6) { _, x -> x })
        assertEquals(SearchResult.found(4), b6.binarySearchByKey(7) { _, x -> x })
        assertEquals(SearchResult.insertion(5), b6.binarySearchByKey(8) { _, x -> x })
    }

    @Test
    fun testPartitionPoint() {
        val b = IndexMap.new<Int, Int>()
        assertEquals(0, b.partitionPoint { _, x -> x < 5 })

        val b1 = IndexMap.from(listOf(100 to 4))
        assertEquals(0, b1.partitionPoint { _, x -> x < 3 })
        assertEquals(0, b1.partitionPoint { _, x -> x < 4 })
        assertEquals(1, b1.partitionPoint { _, x -> x < 5 })

        val b2 = IndexMap.from(listOf(1, 2, 4, 6, 8, 9).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(3, b2.partitionPoint { _, x -> x < 5 })
        assertEquals(3, b2.partitionPoint { _, x -> x < 6 })
        assertEquals(4, b2.partitionPoint { _, x -> x < 7 })
        assertEquals(4, b2.partitionPoint { _, x -> x < 8 })

        val b3 = IndexMap.from(listOf(1, 2, 4, 5, 6, 8).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(6, b3.partitionPoint { _, x -> x < 9 })

        val b4 = IndexMap.from(listOf(1, 2, 4, 6, 7, 8, 9).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(3, b4.partitionPoint { _, x -> x < 6 })
        assertEquals(3, b4.partitionPoint { _, x -> x < 5 })
        assertEquals(5, b4.partitionPoint { _, x -> x < 8 })

        val b5 = IndexMap.from(listOf(1, 2, 4, 5, 6, 8, 9).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(5, b5.partitionPoint { _, x -> x < 7 })
        assertEquals(0, b5.partitionPoint { _, x -> x < 0 })

        val b6 = IndexMap.from(listOf(1, 3, 3, 3, 7).mapIndexed { i, x -> (i + 100) to x })
        assertEquals(0, b6.partitionPoint { _, x -> x < 0 })
        assertEquals(0, b6.partitionPoint { _, x -> x < 1 })
        assertEquals(1, b6.partitionPoint { _, x -> x < 2 })
        assertEquals(1, b6.partitionPoint { _, x -> x < 3 })
        assertEquals(4, b6.partitionPoint { _, x -> x < 4 })
        assertEquals(4, b6.partitionPoint { _, x -> x < 5 })
        assertEquals(4, b6.partitionPoint { _, x -> x < 6 })
        assertEquals(4, b6.partitionPoint { _, x -> x < 7 })
        assertEquals(5, b6.partitionPoint { _, x -> x < 8 })
    }

    @Test
    fun testMoveIndexOutOfBounds() {
        val map = IndexMap.from((0 until 10).map { it to Unit })
        assertFailsWith<IllegalArgumentException> {
            map.moveIndex(0, 10)
        }
        assertFailsWith<IllegalArgumentException> {
            map.moveIndex(10, 0)
        }
    }

    @Test
    fun disjointMutEmptyMap() {
        val map = IndexMap.new<Int, Int>()
        assertEquals(listOf(null, null, null, null), map.getDisjointMut(listOf(0, 1, 2, 3)))
    }

    @Test
    fun disjointMutEmptyParam() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 10)
        assertEquals(emptyList(), map.getDisjointMut(emptyList()))
    }

    @Test
    fun disjointMutSingleFail() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 10)
        assertEquals(listOf(null), map.getDisjointMut(listOf(0)))
    }

    @Test
    fun disjointMutSingleSuccess() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 10)
        assertEquals(listOf(10), map.getDisjointMut(listOf(1)))
    }

    @Test
    fun disjointMutMultiSuccess() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 100)
        map.insert(2, 200)
        map.insert(3, 300)
        map.insert(4, 400)

        assertEquals(listOf(100, 200), map.getDisjointMut(listOf(1, 2)))
        assertEquals(listOf(100, 300), map.getDisjointMut(listOf(1, 3)))
        assertEquals(listOf(300, 100, 400, 200), map.getDisjointMut(listOf(3, 1, 4, 2)))
    }

    @Test
    fun disjointMutMultiSuccessUnsizedKey() {
        val map = IndexMap.new<String, Int>()
        map.insert("1", 100)
        map.insert("2", 200)
        map.insert("3", 300)
        map.insert("4", 400)

        assertEquals(listOf(100, 200), map.getDisjointMut(listOf("1", "2")))
        assertEquals(listOf(100, 300), map.getDisjointMut(listOf("1", "3")))
        assertEquals(listOf(300, 100, 400, 200), map.getDisjointMut(listOf("3", "1", "4", "2")))
    }

    @Test
    fun disjointMutMultiSuccessBorrowKey() {
        val map = IndexMap.new<String, Int>()
        map.insert("1", 100)
        map.insert("2", 200)
        map.insert("3", 300)
        map.insert("4", 400)

        assertEquals(listOf(100, 200), map.getDisjointMut(listOf("1", "2")))
        assertEquals(listOf(100, 300), map.getDisjointMut(listOf("1", "3")))
        assertEquals(listOf(300, 100, 400, 200), map.getDisjointMut(listOf("3", "1", "4", "2")))
    }

    @Test
    fun disjointMutMultiFailMissing() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 100)
        map.insert(2, 200)
        map.insert(3, 300)
        map.insert(4, 400)

        assertEquals(listOf(100, null), map.getDisjointMut(listOf(1, 5)))
        assertEquals(listOf(null, null), map.getDisjointMut(listOf(5, 6)))
        assertEquals(listOf(100, null, 400), map.getDisjointMut(listOf(1, 5, 4)))
    }

    @Test
    fun disjointMutMultiFailDuplicatePanic() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 100)
        assertFailsWith<IllegalArgumentException> {
            map.getDisjointMut(listOf(1, 2, 1))
        }
    }

    @Test
    fun disjointIndicesMutFailOob() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 10)
        map.insert(321, 20)
        assertEquals(null to GetDisjointMutError.IndexOutOfBounds, map.getDisjointIndicesMut(listOf(1, 3)))
    }

    @Test
    fun disjointIndicesMutEmpty() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 10)
        map.insert(321, 20)
        assertEquals(emptyList<Pair<Int, Int>>() to null, map.getDisjointIndicesMut(emptyList()))
    }

    @Test
    fun disjointIndicesMutSuccess() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 10)
        map.insert(321, 20)
        assertEquals(listOf(1 to 10) to null, map.getDisjointIndicesMut(listOf(0)))
        assertEquals(listOf(321 to 20) to null, map.getDisjointIndicesMut(listOf(1)))
        assertEquals(listOf(1 to 10, 321 to 20) to null, map.getDisjointIndicesMut(listOf(0, 1)))
    }

    @Test
    fun disjointIndicesMutFailDuplicate() {
        val map = IndexMap.new<Int, Int>()
        map.insert(1, 10)
        map.insert(321, 20)
        assertEquals(null to GetDisjointMutError.OverlappingIndices, map.getDisjointIndicesMut(listOf(1, 0, 1)))
    }

    @Test
    fun insertSortedByKey() {
        val values = mutableListOf(-1 to 8, 3 to 18, -27 to 2, -2 to 5)
        val map = IndexMap.new<Int, Int>()
        for ((key, value) in values) {
            val (_, old) = map.insertSortedByKey(key, value) { k, _ -> kotlin.math.abs(k) }
            assertNull(old)
        }
        val expected = values.sortedBy { kotlin.math.abs(it.first) }
        assertEquals(expected, map.asEntries())

        for (i in values.indices) {
            val (key, value) = values[i]
            val (_, old) = map.insertSortedByKey(key, -value) { k, _ -> kotlin.math.abs(k) }
            assertEquals(value, old)
            values[i] = key to -value
        }
        val expectedNeg = values.sortedBy { kotlin.math.abs(it.first) }
        assertEquals(expectedNeg, map.asEntries())
    }

    @Test
    fun insertSortedBy() {
        val values = mutableListOf(1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5)
        val map = IndexMap.new<Int, Int>()
        for ((key, value) in values) {
            val (_, old) = map.insertSortedBy(key, value) { key1, _, key2, _ -> key2.compareTo(key1) }
            assertNull(old)
        }
        assertEquals(values.reversed(), map.asEntries())

        for (i in values.indices) {
            val (key, value) = values[i]
            val (_, old) = map.insertSortedBy(key, -value) { key1, _, key2, _ -> key2.compareTo(key1) }
            assertEquals(value, old)
            values[i] = key to -value
        }
        assertEquals(values.reversed(), map.asEntries())
    }

    @Test
    fun isSorted() {
        fun expect(m: IndexMap<Int, Int>, e: List<Boolean>) {
            assertEquals(e[0], m.isSorted())
            assertEquals(e[1], m.isSortedBy { k1, _, k2, _ -> k1 < k2 })
            assertEquals(e[2], m.isSortedBy { k1, _, k2, _ -> k1 > k2 })
            assertEquals(e[3], m.isSortedBy { _, v1, _, v2 -> v1 < v2 })
            assertEquals(e[4], m.isSortedBy { _, v1, _, v2 -> v1 > v2 })
            assertEquals(e[5], m.isSortedByKey { k, _ -> k })
            assertEquals(e[6], m.isSortedByKey { _, v -> v })
        }

        val map = IndexMap.from((0 until 10).map { it to it * it })
        expect(map, listOf(true, true, false, true, false, true, true))

        map.insert(5, -1)
        expect(map, listOf(true, true, false, false, false, true, false))

        map.insert(5, 25)
        map.replaceIndex(5, -1)
        expect(map, listOf(false, false, false, true, false, false, true))
    }

    @Test
    fun isSortedTrivial() {
        fun expect(m: IndexMap<Int, Int>, e: List<Boolean>) {
            assertEquals(e[0], m.isSorted())
            assertEquals(e[1], m.isSortedBy { _, _, _, _ -> true })
            assertEquals(e[2], m.isSortedBy { _, _, _, _ -> false })
            assertEquals(e[3], m.isSortedByKey { _, _ -> 0.0 })
        }

        val map = IndexMap.new<Int, Int>()
        expect(map, listOf(true, true, true, true))

        map.insert(0, 0)
        expect(map, listOf(true, true, true, true))

        map.insert(1, 1)
        expect(map, listOf(true, true, false, true))

        map.reverse()
        expect(map, listOf(false, true, false, true))
    }
}
