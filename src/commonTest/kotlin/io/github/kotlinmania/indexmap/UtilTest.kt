// port-lint: source src/util.rs
package io.github.kotlinmania.indexmap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UtilTest {
    @Test
    fun thirdReturnsTupleTail() {
        assertEquals("value", third(Triple(1, false, "value")))
    }

    @Test
    fun simplifyRangeHandlesUnboundedAndIncludedBounds() {
        val range = rangeBounds(Bound.Unbounded, Bound.Included(2))

        assertEquals(IndexRange(0, 3), simplifyRange(range, len = 5))
    }

    @Test
    fun simplifyRangeHandlesExcludedBounds() {
        val range = rangeBounds(Bound.Excluded(1), Bound.Excluded(4))

        assertEquals(IndexRange(2, 4), simplifyRange(range, len = 5))
    }

    @Test
    fun simplifyRangeRejectsOutOfRangeStart() {
        assertFailsWith<IndexOutOfBoundsException> {
            simplifyRange(rangeBounds(Bound.Included(6), Bound.Unbounded), len = 5)
        }
    }

    @Test
    fun simplifyRangeRejectsInvertedRange() {
        assertFailsWith<IndexOutOfBoundsException> {
            simplifyRange(rangeBounds(Bound.Included(4), Bound.Excluded(2)), len = 5)
        }
    }

    @Test
    fun trySimplifyRangeReturnsNullForInvalidBounds() {
        assertNull(trySimplifyRange(rangeBounds(Bound.Excluded(5), Bound.Unbounded), len = 5))
        assertNull(trySimplifyRange(rangeBounds(Bound.Included(4), Bound.Excluded(2)), len = 5))
    }

    @Test
    fun sliceEqUsesCustomComparator() {
        assertTrue(sliceEq(listOf("a", "bb"), listOf(1, 2)) { left, right -> left.length == right })
        assertFalse(sliceEq(listOf("a", "bb"), listOf(1, 3)) { left, right -> left.length == right })
        assertFalse(sliceEq(listOf("a"), listOf(1, 2)) { left, right -> left.length == right })
    }
}

private fun rangeBounds(start: Bound<Int>, end: Bound<Int>): RangeBounds<Int> =
    object : RangeBounds<Int> {
        override fun startBound(): Bound<Int> = start
        override fun endBound(): Bound<Int> = end
    }
