// port-lint: tests set/slice.rs
package io.github.kotlinmania.indexmap.set

import io.github.kotlinmania.indexmap.IndexSet
import io.github.kotlinmania.indexmap.map.SearchResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SliceTest {
    @Test
    fun sliceNew() {
        val slice = Slice.new<Int>()

        assertEquals(0, slice.len())
        assertTrue(slice.isEmpty())
        assertNull(slice.first())
        assertNull(slice.last())
    }

    @Test
    fun sliceIndex() {
        val slice = sampleSet().asSetSlice()

        assertEquals(4, slice.len())
        assertFalse(slice.isEmpty())
        assertEquals(1, slice.getIndex(0))
        assertEquals(4, slice.getIndex(3))
        assertNull(slice.getIndex(4))
        assertEquals(listOf(1, 2, 3, 4), slice.toList())
    }

    @Test
    fun sliceGetRange() {
        val slice = sampleSet().asSetSlice()

        assertEquals(listOf(2, 3), slice.getRange(1, 3)?.toList())
        assertEquals(emptyList(), slice.getRange(2, 2)?.toList())
        assertNull(slice.getRange(-1, 2))
        assertNull(slice.getRange(3, 2))
        assertNull(slice.getRange(0, 5))
    }

    @Test
    fun sliceSplitFirst() {
        val slice = sampleSet().asSetSlice()
        val split = assertNotNull(slice.splitFirst())

        assertEquals(1, split.first)
        assertEquals(listOf(2, 3, 4), split.second.toList())
        assertNull(Slice.new<Int>().splitFirst())
    }

    @Test
    fun sliceSplitLast() {
        val slice = sampleSet().asSetSlice()
        val split = assertNotNull(slice.splitLast())

        assertEquals(4, split.first)
        assertEquals(listOf(1, 2, 3), split.second.toList())
        assertNull(Slice.new<Int>().splitLast())
    }

    @Test
    fun sliceSplitAtChecked() {
        val slice = sampleSet().asSetSlice()
        val split = assertNotNull(slice.splitAtChecked(2))

        assertEquals(listOf(1, 2), split.first.toList())
        assertEquals(listOf(3, 4), split.second.toList())
        assertNull(slice.splitAtChecked(5))
    }

    @Test
    fun sliceSearchAndPartition() {
        val slice = sampleSet().asSetSlice()

        assertEquals(SearchResult.found(2), slice.binarySearchBy { it.compareTo(3) })
        assertEquals(SearchResult.insertion(4), slice.binarySearchBy { it.compareTo(25) })
        assertEquals(SearchResult.found(2), slice.binarySearch(3, naturalOrder()))
        assertEquals(SearchResult.found(1), slice.binarySearchByKey(2, { it }, naturalOrder()))
        assertTrue(slice.isSorted(naturalOrder()))
        assertTrue(slice.isSortedBy { left, right -> left <= right })
        assertTrue(slice.isSortedByKey({ it }, naturalOrder()))
        assertEquals(2, slice.partitionPoint { it < 3 })
    }

    private fun check(vecSlice: List<Int>, setSlice: Slice<Int>, subSlice: Slice<Int>) {
        assertEquals(setSlice, subSlice)
        assertEquals(vecSlice, setSlice.toList())
    }

    @Test
    fun sliceIndexTestFromRust() {
        val vec: List<Int> = (0 until 10).map { it * it }
        val set: IndexSet<Int> = IndexSet.from(vec)
        val slice = set.asSetSlice()

        // RangeFull
        check(vec, set.asSetSlice(), slice)

        for (i in 0 until 10) {
            // Index
            assertEquals(vec[i], set[i])
            assertEquals(vec[i], slice[i])

            // RangeFrom
            check(vec.subList(i, vec.size), set.asSetSlice().getRange(i, set.len())!!, slice.getRange(i, slice.len())!!)

            // RangeTo
            check(vec.subList(0, i), set.asSetSlice().getRange(0, i)!!, slice.getRange(0, i)!!)

            // RangeToInclusive
            check(vec.subList(0, i + 1), set.asSetSlice().getRange(0, i + 1)!!, slice.getRange(0, i + 1)!!)

            // (Bound::Excluded(i), Bound::Unbounded)
            check(vec.subList(i + 1, vec.size), set.asSetSlice().getRange(i + 1, set.len())!!, slice.getRange(i + 1, slice.len())!!)

            for (j in i..10) {
                // Range
                check(vec.subList(i, j), set.asSetSlice().getRange(i, j)!!, slice.getRange(i, j)!!)
            }

            for (j in i until 10) {
                // RangeInclusive
                check(vec.subList(i, j + 1), set.asSetSlice().getRange(i, j + 1)!!, slice.getRange(i, j + 1)!!)
            }
        }
    }

    private fun sampleSet(): IndexSet<Int> =
        IndexSet.from(listOf(1, 2, 3, 4))
}
