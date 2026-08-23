// port-lint: tests set/iter.rs
package io.github.kotlinmania.indexmap.set

import io.github.kotlinmania.indexmap.IndexSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IterTest {
    @Test
    fun testIter() {
        val set = IndexSet.from(listOf("a", "b", "c"))
        val iter = Iter(set.map.entries)
        assertEquals(3, iter.len())
        assertFalse(iter.isEmpty())

        assertEquals("a", iter.next())
        assertEquals(2, iter.len())
        assertEquals("c", iter.nextBack())
        assertEquals(1, iter.len())
        assertEquals("b", iter.next())
        assertEquals(0, iter.len())
        assertTrue(iter.isEmpty())
        assertFalse(iter.hasNext())
        assertNull(iter.nextBack())
    }

    @Test
    fun testIntoIter() {
        val set = IndexSet.from(listOf("a", "b", "c"))
        val iter = IntoIter(set.map.entries)
        assertEquals(3, iter.len())
        val clone = iter.clone()
        assertEquals(listOf("a", "b", "c"), iter.asSlice().toList())
        assertEquals("a", clone.next())
        assertEquals("c", clone.nextBack())
        assertEquals("b", clone.next())
    }

    @Test
    fun testDifferenceAndIntersection() {
        val set1 = IndexSet.from(listOf(1, 2, 3, 4))
        val set2 = IndexSet.from(listOf(3, 4, 5, 6))

        val diff = Difference(Iter(set1.map.entries), set2)
        assertEquals(listOf(1, 2), diff.asSequence().toList())

        val intersect = Intersection(Iter(set1.map.entries), set2)
        assertEquals(listOf(3, 4), intersect.asSequence().toList())
    }

    @Test
    fun testSymmetricDifferenceAndUnion() {
        val set1 = IndexSet.from(listOf(1, 2, 3))
        val set2 = IndexSet.from(listOf(2, 3, 4))

        val symDiff =
            SymmetricDifference(
                Difference(Iter(set1.map.entries), set2),
                Difference(Iter(set2.map.entries), set1),
            )
        assertEquals(listOf(1, 4), symDiff.asSequence().toList())

        val union =
            Union(
                Iter(set1.map.entries),
                Difference(Iter(set2.map.entries), set1),
            )
        assertEquals(listOf(1, 2, 3, 4), union.asSequence().toList())
    }

    @Test
    fun testDrain() {
        val set = IndexSet.from(listOf("a", "b", "c", "d"))
        val drained = Drain(set.map.entries.subList(1, 3))
        assertEquals(2, drained.len())
        assertEquals("b", drained.next())
        assertEquals("c", drained.nextBack())
        assertFalse(drained.hasNext())
        assertNull(drained.nextBack())
    }

    @Test
    fun testSplice() {
        val set = IndexSet.from(listOf("a", "b", "c"))
        val drainedEntries = set.map.entries.subList(1, 2)
        val splice = Splice(set, drainedEntries, listOf("x", "y").iterator())
        assertEquals(1, splice.len())
        assertEquals("b", splice.next())
        splice.close()
        assertTrue(set.contains("x"))
        assertTrue(set.contains("y"))
    }
}
