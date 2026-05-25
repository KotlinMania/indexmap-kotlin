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
    fun sliceNew() {
        val slice = Slice.new<Int, String>()

        assertEquals(0, slice.len())
        assertTrue(slice.isEmpty())
        assertNull(slice.first())
        assertNull(slice.last())
    }

    @Test
    fun sliceIndex() {
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
    fun sliceGetRange() {
        val slice = sampleMap().asSlice()

        assertEquals(listOf(2 to "two", 3 to "three"), slice.getRange(1, 3)?.toList())
        assertEquals(emptyList(), slice.getRange(2, 2)?.toList())
        assertNull(slice.getRange(-1, 2))
        assertNull(slice.getRange(3, 2))
        assertNull(slice.getRange(0, 5))
    }

    @Test
    fun sliceSplitFirst() {
        val slice = sampleMap().asSlice()
        val split = assertNotNull(slice.splitFirst())

        assertEquals(1 to "one", split.first)
        assertEquals(listOf(2 to "two", 3 to "three", 4 to "four"), split.second.toList())
        assertNull(Slice.new<Int, String>().splitFirst())
    }

    @Test
    fun sliceSplitLast() {
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
        val pairComparator = Comparator<Pair<Int, String>> { left, right ->
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
        assertEquals(slice, Slice.from(slice))
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

    // Upstream mutable slice mutation tests require exclusive borrow semantics that Kotlin common code cannot expose.

    private fun sampleMap(): IndexMap<Int, String> =
        IndexMap.new<Int, String>().also {
            it.insert(1, "one")
            it.insert(2, "two")
            it.insert(3, "three")
            it.insert(4, "four")
        }
}
