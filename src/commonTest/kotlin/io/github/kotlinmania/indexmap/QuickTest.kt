// port-lint: tests quick.rs
package io.github.kotlinmania.indexmap

import io.github.kotlinmania.indexmap.map.Entry
import io.github.kotlinmania.indexmap.map.insertSorted
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class QuickTest {
    @Test
    fun testContains() {
        val random = Random(42)
        for (trial in 0 until 20) {
            val insert = List(random.nextInt(0, 50)) { random.nextInt(0, 100) }
            val map = IndexMap.new<Int, Unit>()
            for (key in insert) {
                map.insert(key, Unit)
            }
            for (key in insert) {
                assertNotNull(map.get(key))
            }
        }
    }

    @Test
    fun testContainsNot() {
        val random = Random(43)
        for (trial in 0 until 20) {
            val insert = List(random.nextInt(0, 40)) { random.nextInt(0, 100) }
            val not = List(random.nextInt(0, 40)) { random.nextInt(0, 100) }
            val map = IndexMap.new<Int, Unit>()
            for (key in insert) {
                map.insert(key, Unit)
            }
            val nots = not.toSet() - insert.toSet()
            for (key in nots) {
                assertNull(map.get(key))
            }
        }
    }

    @Test
    fun testInsertRemove() {
        val random = Random(44)
        for (trial in 0 until 20) {
            val insert = List(random.nextInt(0, 50)) { random.nextInt(0, 100) }
            val remove = List(random.nextInt(0, 30)) { random.nextInt(0, 100) }
            val map = IndexMap.new<Int, Unit>()
            for (key in insert) {
                map.insert(key, Unit)
            }
            for (key in remove) {
                map.swapRemove(key)
            }
            val elements = insert.toSet() - remove.toSet()
            assertEquals(elements.size, map.len())
            assertEquals(elements.size, map.count())
            for (k in elements) {
                assertNotNull(map.get(k))
            }
        }
    }

    @Test
    fun testInsertionOrder() {
        val random = Random(45)
        for (trial in 0 until 20) {
            val insert = List(random.nextInt(0, 50)) { random.nextInt(0, 100) }
            val map = IndexMap.new<Int, Unit>()
            for (key in insert) {
                map.insert(key, Unit)
            }
            val unique = insert.distinct()
            assertEquals(unique, map.keys())
        }
    }

    @Test
    fun testInsertSorted() {
        val random = Random(46)
        for (trial in 0 until 20) {
            val count = random.nextInt(0, 30)
            val insert = List(count) { random.nextInt(0, 100) to random.nextInt(0, 1000) }
            val hmap = mutableMapOf<Int, Int>()
            val map = IndexMap.new<Int, Int>()
            val map2 = IndexMap.new<Int, Int>()
            for ((key, value) in insert) {
                hmap[key] = value
                map.insertSorted(key, value)
                when (val e = map2.entry(key)) {
                    is Entry.Occupied -> {
                        e.entry.insert(value)
                    }
                    is Entry.Vacant -> {
                        e.entry.insertSorted(value)
                    }
                }
            }
            val hsorted = hmap.entries.sortedBy { it.key }.map { it.key to it.value }
            val mapEntries = map.asEntries()
            val map2Entries = map2.asEntries()
            assertEquals(hsorted, mapEntries)
            assertEquals(mapEntries, map2Entries)
        }
    }

    @Test
    fun testPop() {
        val random = Random(47)
        for (trial in 0 until 20) {
            val insert = List(random.nextInt(0, 40)) { random.nextInt(0, 100) }
            val map = IndexMap.new<Int, Unit>()
            for (key in insert) {
                map.insert(key, Unit)
            }
            val pops = mutableListOf<Int>()
            while (true) {
                val popped = map.pop() ?: break
                pops.add(popped.first)
            }
            pops.reverse()
            assertEquals(insert.distinct(), pops)
        }
    }

    @Test
    fun testWithCapacity() {
        for (cap in listOf(0, 1, 8, 16, 64, 128)) {
            val map = IndexMap.withCapacity<Int, Int>(cap)
            assertEquals(0, map.len())
            for (i in 0 until cap) {
                map.insert(i, i)
            }
            assertEquals(cap, map.len())
        }
    }

    @Test
    fun testDrainFull() {
        val random = Random(48)
        for (trial in 0 until 20) {
            val insert = List(random.nextInt(0, 40)) { random.nextInt(0, 100) }
            val map = IndexMap.new<Int, Unit>()
            for (key in insert) {
                map.insert(key, Unit)
            }
            val clone = map.clone()
            val drained = clone.drain()
            for ((key, _) in drained) {
                map.swapRemove(key)
            }
            assertTrue(map.isEmpty())
        }
    }

    @Test
    fun testShiftRemove() {
        val random = Random(49)
        for (trial in 0 until 20) {
            val insert = List(random.nextInt(0, 40)) { random.nextInt(0, 100) }
            val remove = List(random.nextInt(0, 20)) { random.nextInt(0, 100) }
            val map = IndexMap.new<Int, Unit>()
            for (key in insert) {
                map.insert(key, Unit)
            }
            for (key in remove) {
                map.shiftRemove(key)
            }
            val elements = insert.toSet() - remove.toSet()
            val remainingInInsertOrder = insert.distinct().filter { elements.contains(it) }
            assertEquals(remainingInInsertOrder, map.keys())
            assertEquals(elements.size, map.len())
        }
    }

    @Test
    fun testIndexing() {
        val insert = listOf(3, 1, 4, 1, 5, 9, 2, 6, 5)
        val map = IndexMap.new<Int, Int>()
        for (x in insert) {
            map.insert(x, x)
        }
        val set = IndexSet.new<Int>()
        for (k in map.keys()) {
            set.insert(k)
        }
        assertEquals(map.len(), set.len())

        for (i in 0 until set.len()) {
            val key = set.getIndex(i)!!
            assertEquals(key to key, map.getIndex(i))
            assertEquals(key, map[key])
            assertEquals(key, set[i])
        }
    }

    @Test
    fun testSetSwapIndices() {
        val set = IndexSet.new<Int>()
        set.extend(listOf(10, 20, 30, 40, 50))
        set.swapIndices(1, 3)
        assertEquals(listOf(10, 40, 30, 20, 50), set.asList())
        assertEquals(1, set.getIndexOf(40))
        assertEquals(3, set.getIndexOf(20))
    }

    @Test
    fun testMapSwapIndices() {
        val map = IndexMap.new<String, Int>()
        map.insert("a", 1)
        map.insert("b", 2)
        map.insert("c", 3)
        map.insert("d", 4)
        map.swapIndices(0, 2)
        assertEquals(listOf("c", "b", "a", "d"), map.keys())
        assertEquals(0, map.getIndexOf("c"))
        assertEquals(2, map.getIndexOf("a"))
    }

    @Test
    fun testSetMoveIndex() {
        val set = IndexSet.new<Int>()
        set.extend(listOf(10, 20, 30, 40, 50))
        set.moveIndex(1, 3)
        assertEquals(listOf(10, 30, 40, 20, 50), set.asList())
        assertEquals(3, set.getIndexOf(20))
        assertEquals(1, set.getIndexOf(30))
    }

    @Test
    fun testMapMoveIndex() {
        val map = IndexMap.new<String, Int>()
        map.insert("a", 1)
        map.insert("b", 2)
        map.insert("c", 3)
        map.insert("d", 4)
        map.moveIndex(0, 2)
        assertEquals(listOf("b", "c", "a", "d"), map.keys())
        assertEquals(2, map.getIndexOf("a"))
        assertEquals(0, map.getIndexOf("b"))
    }

    @Test
    fun testRetainOrdered() {
        val map = IndexMap.new<Int, String>()
        for (i in 0 until 20) {
            map.insert(i, "val_$i")
        }
        map.retain { k, _ -> k % 2 == 0 }
        val expectedKeys = (0 until 20 step 2).toList()
        assertEquals(expectedKeys, map.keys())
        assertEquals(10, map.len())
    }

    @Test
    fun testSort() {
        val map = IndexMap.new<Int, Int>()
        val input = listOf(5 to 50, 2 to 20, 8 to 80, 1 to 10, 3 to 30)
        for ((k, v) in input) {
            map.insert(k, v)
        }
        map.sortBy { k1, _, k2, _ -> k1.compareTo(k2) }
        assertEquals(listOf(1, 2, 3, 5, 8), map.keys())
        assertEquals(listOf(10, 20, 30, 50, 80), map.values())
    }

    @Test
    fun testReverse() {
        val map = IndexMap.new<Int, Int>()
        val input = listOf(1 to 10, 2 to 20, 3 to 30, 4 to 40)
        for ((k, v) in input) {
            map.insert(k, v)
        }
        map.reverse()
        assertEquals(listOf(4, 3, 2, 1), map.keys())
        assertEquals(listOf(40, 30, 20, 10), map.values())
    }
}
