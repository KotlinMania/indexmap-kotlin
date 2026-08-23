// port-lint: tests map/iter.rs
package io.github.kotlinmania.indexmap.map

import io.github.kotlinmania.indexmap.IndexMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IterTest {
    @Test
    fun testIter() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3))
        val iter = map.iter()
        assertEquals(3, iter.len())
        assertFalse(iter.isEmpty())

        assertEquals("a" to 1, iter.next())
        assertEquals(2, iter.len())
        assertEquals("c" to 3, iter.nextBack())
        assertEquals(1, iter.len())
        assertEquals("b" to 2, iter.next())
        assertEquals(0, iter.len())
        assertTrue(iter.isEmpty())
        assertFalse(iter.hasNext())
        assertNull(iter.nextBack())
    }

    @Test
    fun testIterMut() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3))
        val iter = map.iterMut()
        assertEquals(3, iter.len())
        assertEquals("a" to 1, iter.next())
        assertEquals("c" to 3, iter.nextBack())
        assertEquals("b" to 2, iter.next())
        assertFalse(iter.hasNext())
    }

    @Test
    fun testIntoIter() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3))
        val iter = map.intoIter()
        assertEquals(3, iter.len())
        val clone = iter.clone()
        assertEquals(listOf("a" to 1, "b" to 2, "c" to 3), iter.asSlice().toList())
        assertEquals("a" to 1, clone.next())
        assertEquals("c" to 3, clone.nextBack())
        assertEquals("b" to 2, clone.next())
    }

    @Test
    fun testKeysAndIntoKeys() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3))
        val keys = Keys(map.entries)
        assertEquals(3, keys.len())
        assertEquals("a", keys[0])
        assertEquals("b", keys[1])
        assertEquals("c", keys[2])
        assertEquals("a", keys.next())
        assertEquals("c", keys.nextBack())
        assertEquals("b", keys.next())
        assertNull(keys.nextBack())

        val intoKeys = IntoKeys<String, Int>(map.keys())
        assertEquals(3, intoKeys.len())
        assertEquals("a", intoKeys[0])
        val cloneKeys = intoKeys.clone()
        assertEquals("a", cloneKeys.next())
        assertEquals("c", cloneKeys.nextBack())
    }

    @Test
    fun testValuesAndIntoValues() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3))
        val values = Values(map.entries)
        assertEquals(3, values.len())
        assertEquals(1, values[0])
        assertEquals(2, values[1])
        assertEquals(3, values[2])
        assertEquals(1, values.next())
        assertEquals(3, values.nextBack())
        assertEquals(2, values.next())
        assertNull(values.nextBack())

        val intoValues = IntoValues<String, Int>(map.values())
        assertEquals(3, intoValues.len())
        assertEquals(1, intoValues[0])
        val cloneValues = intoValues.clone()
        assertEquals(1, cloneValues.next())
        assertEquals(3, cloneValues.nextBack())
    }

    @Test
    fun testDrain() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3, "d" to 4))
        val drained = Drain(map.entries.subList(1, 3))
        assertEquals(2, drained.len())
        assertEquals("b" to 2, drained.next())
        assertEquals("c" to 3, drained.nextBack())
        assertFalse(drained.hasNext())
        assertNull(drained.nextBack())
    }

    @Test
    fun testSplice() {
        val map = IndexMap.from(listOf("a" to 1, "b" to 2, "c" to 3))
        val drainedEntries = map.entries.subList(1, 2)
        val splice = Splice(map, drainedEntries, listOf("x" to 10, "y" to 20).iterator())
        assertEquals(1, splice.len())
        assertEquals("b" to 2, splice.next())
        splice.close()
        assertTrue(map.containsKey("x"))
        assertTrue(map.containsKey("y"))
    }
}
