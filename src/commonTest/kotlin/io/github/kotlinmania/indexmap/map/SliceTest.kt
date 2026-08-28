// port-lint: tests map/slice.rs
package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.IndexMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SliceTest {
    @Test
    fun sliceNewBasic() {
        val slice = Slice.new<Int, String>()

        assertEquals(0, slice.len())
        assertTrue(slice.isEmpty())
        assertNull(slice.first())
        assertNull(slice.last())
    }

    @Test
    fun sliceIndexBasic() {
        val slice = sampleMap().asSlice()

        assertEquals(4, slice.len())
        assertFalse(slice.isEmpty())
        assertEquals(1 to "one", slice.getIndex(0))
        assertEquals(4 to "four", slice.getIndex(3))
        assertNull(slice.getIndex(4))
        assertEquals(listOf(1, 2, 3, 4), slice.keys())
        assertEquals(listOf("one", "two", "three", "four"), slice.values())
    }

    @Test
    fun sliceGetRangeBasic() {
        val slice = sampleMap().asSlice()

        assertEquals(listOf(2 to "two", 3 to "three"), slice.getRange(1, 3)?.toList())
        assertEquals(emptyList(), slice.getRange(2, 2)?.toList())
        assertNull(slice.getRange(-1, 2))
        assertNull(slice.getRange(3, 2))
        assertNull(slice.getRange(0, 5))
    }

    @Test
    fun sliceSplitFirstBasic() {
        val slice = sampleMap().asSlice()
        val split = assertNotNull(slice.splitFirst())

        assertEquals(1 to "one", split.first)
        assertEquals(listOf(2 to "two", 3 to "three", 4 to "four"), split.second.toList())
        assertNull(Slice.new<Int, String>().splitFirst())
    }

    @Test
    fun sliceSplitLastBasic() {
        val slice = sampleMap().asSlice()
        val split = assertNotNull(slice.splitLast())

        assertEquals(4 to "four", split.first)
        assertEquals(listOf(1 to "one", 2 to "two", 3 to "three"), split.second.toList())
        assertNull(Slice.new<Int, String>().splitLast())
    }

    @Test
    fun sliceSplitAtChecked() {
        val slice = sampleMap().asSlice()
        val split = assertNotNull(slice.splitAtChecked(2))

        assertEquals(listOf(1 to "one", 2 to "two"), split.first.toList())
        assertEquals(listOf(3 to "three", 4 to "four"), split.second.toList())
        assertNull(slice.splitAtChecked(5))
    }

    @Test
    fun sliceSearchAndPartition() {
        val slice = sampleMap().asSlice()

        assertEquals(SearchResult.found(2), slice.binarySearchBy { key, _ -> key.compareTo(3) })
        assertEquals(SearchResult.insertion(4), slice.binarySearchBy { key, _ -> key.compareTo(25) })
        assertEquals(SearchResult.found(2), slice.binarySearchKeys(3, naturalOrder()))
        assertEquals(SearchResult.found(1), slice.binarySearchByKey(2, { key, _ -> key }, naturalOrder()))
        assertTrue(slice.isSorted(naturalOrder()))
        assertTrue(slice.isSortedBy { leftKey, _, rightKey, _ -> leftKey <= rightKey })
        assertTrue(slice.isSortedByKey({ key, _ -> key }, naturalOrder()))
        assertEquals(2, slice.partitionPoint { key, _ -> key < 3 })
    }

    @Test
    fun sliceOwnedIteratorsAndComparisonHelpers() {
        val slice = sampleMap().asSlice()
        val clone = slice.clone()
        val pairComparator =
            Comparator<Pair<Int, String>> { left, right ->
                val keyOrder = left.first.compareTo(right.first)
                if (keyOrder != 0) keyOrder else left.second.compareTo(right.second)
            }

        assertEquals(listOf(1, 2, 3, 4), slice.intoKeys())
        assertEquals(listOf("one", "two", "three", "four"), slice.intoValues())
        assertEquals(listOf(1 to "one", 2 to "two", 3 to "three", 4 to "four"), slice.intoEntries())
        assertEquals(slice.intoEntries(), slice.iter().asSequence().toList())
        assertEquals("two", slice[1])
        assertEquals("two", slice.index(1))
        assertEquals(slice, clone)
        assertTrue(slice !== clone)
        assertEquals(slice.hashCode(), slice.hash())
        assertEquals(slice.toString(), slice.fmt())
        assertTrue(slice.eq(clone))
        assertEquals(0, slice.cmp(clone, pairComparator))
        assertEquals(0, slice.partialCmp(clone, pairComparator))
        assertEquals(Slice.default<Int, String>(), Slice.new())
        assertEquals(Slice.newMut<Int, String>(), Slice.new())
        assertEquals(slice, Slice.from(slice))
        assertEquals(slice, Slice.fromSlice(slice))
        assertEquals(slice, Slice.fromMutSlice(slice))
        assertEquals(slice, Slice.fromBoxed(slice))
    }

    @Test
    fun sliceMutatingViewsAndDisjointAccess() {
        val slice = sampleMap().asSlice()

        assertEquals(slice, slice.intoBoxed())
        assertEquals(1 to "one", slice.getIndexMut(0))
        assertEquals("one", slice.indexMut(0))
        assertEquals(1 to "one", slice.firstMut())
        assertEquals(4 to "four", slice.lastMut())
        assertEquals(listOf("one", "two", "three", "four"), slice.valuesMut())

        val splitMut = slice.splitAtMut(2)
        assertEquals(listOf(1 to "one", 2 to "two"), splitMut.first.toList())
        assertEquals(listOf(3 to "three", 4 to "four"), splitMut.second.toList())

        val splitMutChecked = slice.splitAtMutChecked(2)
        assertEquals(listOf(1 to "one", 2 to "two"), splitMutChecked?.first?.toList())
        assertNull(slice.splitAtMutChecked(10))

        val splitFirstMut = slice.splitFirstMut()
        assertEquals(1 to "one", splitFirstMut?.first)
        assertEquals(listOf(2 to "two", 3 to "three", 4 to "four"), splitFirstMut?.second?.toList())

        val splitLastMut = slice.splitLastMut()
        assertEquals(4 to "four", splitLastMut?.first)
        assertEquals(listOf(1 to "one", 2 to "two", 3 to "three"), splitLastMut?.second?.toList())

        assertEquals(listOf(1 to "one", 2 to "two", 3 to "three", 4 to "four"), slice.iterMut().asSequence().toList())
        assertEquals(listOf(1 to "one", 3 to "three"), slice.getDisjointMut(intArrayOf(0, 2)))
        assertEquals(listOf(2 to "two"), slice.getDisjointOptMut(intArrayOf(1)))
        assertEquals(listOf(2 to "two", 3 to "three"), slice.getRangeMut(1, 3)?.toList())
    }

    @Test
    fun indexMapSliceDelegates() {
        val map = sampleMap()

        assertEquals(listOf(1 to "one", 2 to "two", 3 to "three", 4 to "four"), map.asEntries())
        assertEquals(listOf(2 to "two", 3 to "three"), map.getRange(1, 3)?.toList())
        assertEquals(SearchResult.found(3), map.binarySearchBy { key, _ -> key.compareTo(4) })
        assertTrue(map.isSortedBy { leftKey, _, rightKey, _ -> leftKey <= rightKey })
        assertEquals(3, map.partitionPoint { key, _ -> key < 4 })
    }

    @Test
    fun sliceIndex() {
        fun check(
            vecSlice: List<Pair<Int, Int>>,
            mapSlice: Slice<Int, Int>,
            subSlice: Slice<Int, Int>,
        ) {
            assertEquals(mapSlice, subSlice)
            assertEquals(vecSlice, mapSlice.toList())
            assertEquals(vecSlice.map { it.second }, mapSlice.values())
        }

        val vec: List<Pair<Int, Int>> = (0 until 10).map { it to it * it }
        val map: IndexMap<Int, Int> = IndexMap.from(vec)
        val slice = map.asSlice()

        check(vec, map.asSlice(), slice)

        for (i in 0 until 10) {
            assertEquals(vec[i].second, map[i])
            assertEquals(vec[i].second, slice[i])
            assertEquals(map.get(i), map.getIndex(i)?.second)
            assertEquals(slice[i], map.getIndex(i)?.second)

            check(vec.subList(i, vec.size), map.getRange(i, map.len())!!, slice.getRange(i, slice.len())!!)
            check(vec.subList(0, i), map.getRange(0, i)!!, slice.getRange(0, i)!!)
            check(vec.subList(0, i + 1), map.getRange(0, i + 1)!!, slice.getRange(0, i + 1)!!)
            check(vec.subList(i + 1, vec.size), map.getRange(i + 1, map.len())!!, slice.getRange(i + 1, slice.len())!!)

            for (j in i..10) {
                check(vec.subList(i, j), map.getRange(i, j)!!, slice.getRange(i, j)!!)
            }

            for (j in i until 10) {
                check(vec.subList(i, j + 1), map.getRange(i, j + 1)!!, slice.getRange(i, j + 1)!!)
            }
        }
    }

    @Test
    fun sliceIndexMut() {
        fun checkMut(
            vecSlice: List<Pair<Int, Int>>,
            mapSlice: Slice<Int, Int>,
            subSlice: Slice<Int, Int>,
        ) {
            assertEquals(mapSlice, subSlice)
            assertEquals(vecSlice, mapSlice.toList())
            assertEquals(vecSlice.map { it.second }, mapSlice.values())
        }

        val vec: List<Pair<Int, Int>> = (0 until 10).map { it to it * it }
        val map: IndexMap<Int, Int> = IndexMap.from(vec)
        val slice = map.asMutSlice()

        checkMut(vec, map.asMutSlice(), slice)

        for (i in 0 until 10) {
            assertEquals(vec[i].second, map[i])
            assertEquals(vec[i].second, slice[i])
            assertEquals(map.get(i), map.getIndex(i)?.second)
            assertEquals(slice[i], map.getIndex(i)?.second)

            checkMut(vec.subList(i, vec.size), map.getRangeMut(i, map.len())!!, slice.getRangeMut(i, slice.len())!!)
            checkMut(vec.subList(0, i), map.getRangeMut(0, i)!!, slice.getRangeMut(0, i)!!)
            checkMut(vec.subList(0, i + 1), map.getRangeMut(0, i + 1)!!, slice.getRangeMut(0, i + 1)!!)
            checkMut(vec.subList(i + 1, vec.size), map.getRangeMut(i + 1, map.len())!!, slice.getRangeMut(i + 1, slice.len())!!)

            for (j in i..10) {
                checkMut(vec.subList(i, j), map.getRangeMut(i, j)!!, slice.getRangeMut(i, j)!!)
            }

            for (j in i until 10) {
                checkMut(vec.subList(i, j + 1), map.getRangeMut(i, j + 1)!!, slice.getRangeMut(i, j + 1)!!)
            }
        }
    }

    @Test
    fun sliceNew() {
        val slice: Slice<Int, Int> = Slice.new()
        assertTrue(slice.isEmpty())
        assertEquals(0, slice.len())
    }

    @Test
    fun sliceNewMut() {
        val slice: Slice<Int, Int> = Slice.newMut()
        assertTrue(slice.isEmpty())
        assertEquals(0, slice.len())
    }

    @Test
    fun sliceGetIndexMut() {
        val map: IndexMap<Int, Int> = IndexMap.from((0 until 10).map { it to it * it })
        val slice = map.asMutSlice()

        val entry = slice.getIndexMut(0)
        assertNotNull(entry)
        assertEquals(0 to 0, entry)

        assertNull(slice.getIndexMut(11))
    }

    @Test
    fun sliceSplitFirst() {
        val emptySlice: Slice<Int, Int> = Slice.newMut()
        assertNull(emptySlice.splitFirst())

        val map: IndexMap<Int, Int> = IndexMap.from((0 until 10).map { it to it * it })
        val slice = map.asMutSlice()

        val (first, rest) = assertNotNull(slice.splitFirst())
        assertEquals(0 to 0, first)
        assertEquals(9, rest.len())
        assertEquals(10, slice.len())
    }

    @Test
    fun sliceSplitFirstMut() {
        val emptySlice: Slice<Int, Int> = Slice.newMut()
        assertNull(emptySlice.splitFirstMut())

        val map: IndexMap<Int, Int> = IndexMap.from((0 until 10).map { it to it * it })
        val slice = map.asMutSlice()

        val (first, rest) = assertNotNull(slice.splitFirstMut())
        assertEquals(0 to 0, first)
        assertEquals(9, rest.len())
        assertEquals(10, slice.len())
    }

    @Test
    fun sliceSplitLast() {
        val emptySlice: Slice<Int, Int> = Slice.newMut()
        assertNull(emptySlice.splitLast())

        val map: IndexMap<Int, Int> = IndexMap.from((0 until 10).map { it to it * it })
        val slice = map.asMutSlice()

        val (last, rest) = assertNotNull(slice.splitLast())
        assertEquals(9 to 81, last)
        assertEquals(9, rest.len())
        assertEquals(10, slice.len())
    }

    @Test
    fun sliceSplitLastMut() {
        val emptySlice: Slice<Int, Int> = Slice.newMut()
        assertNull(emptySlice.splitLastMut())

        val map: IndexMap<Int, Int> = IndexMap.from((0 until 10).map { it to it * it })
        val slice = map.asMutSlice()

        val (last, rest) = assertNotNull(slice.splitLastMut())
        assertEquals(9 to 81, last)
        assertEquals(9, rest.len())
        assertEquals(10, slice.len())
    }

    @Test
    fun sliceGetRange() {
        val map: IndexMap<Int, Int> = IndexMap.from((0 until 10).map { it to it * it })
        val slice = map.asMutSlice()
        val subslice = assertNotNull(slice.getRange(3, 6))
        assertEquals(3, subslice.len())
        assertEquals(listOf(3 to 9, 4 to 16, 5 to 25), subslice.toList())
    }

    // Upstream mutable slice mutation tests require exclusive borrow semantics that Kotlin common code cannot expose.

    private fun sampleMap(): IndexMap<Int, String> =
        IndexMap.new<Int, String>().also {
            it.insert(1, "one")
            it.insert(2, "two")
            it.insert(3, "three")
            it.insert(4, "four")
        }
}
