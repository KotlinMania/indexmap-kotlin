// port-lint: tests inner.rs
package io.github.kotlinmania.indexmap.inner

import io.github.kotlinmania.indexmap.HashValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InnerTest {
    @Test
    fun coreBasicOperations() {
        val core = Core.new<String, Int>()
        assertEquals(0, core.len())
        assertTrue(core.intoEntries().isEmpty())

        val (idx1, old1) = core.insertFull(HashValue(1uL), "a", 10)
        assertEquals(0, idx1)
        assertNull(old1)
        assertEquals(1, core.len())

        val (idx2, old2) = core.insertFull(HashValue(1uL), "a", 20)
        assertEquals(0, idx2)
        assertEquals(10, old2)

        core.insertFull(HashValue(2uL), "b", 30)
        assertEquals(2, core.len())
        assertEquals(0, core.getIndexOf(HashValue(1uL), "a"))
        assertEquals(1, core.getIndexOf(HashValue(2uL), "b"))

        core.swapIndices(0, 1)
        assertEquals(0, core.getIndexOf(HashValue(2uL), "b"))
        assertEquals(1, core.getIndexOf(HashValue(1uL), "a"))

        core.reverse()
        assertEquals(0, core.getIndexOf(HashValue(1uL), "a"))
        assertEquals(1, core.getIndexOf(HashValue(2uL), "b"))

        val (k, v) = core.shiftRemoveIndex(0)!!
        assertEquals("a", k)
        assertEquals(20, v)
        assertEquals(1, core.len())

        val popped = core.pop()
        assertEquals("b" to 30, popped)
        assertEquals(0, core.len())
    }

    @Test
    fun entryAndExtractOperations() {
        val core = Core.new<String, Int>()
        core.insertFull(HashValue(1uL), "one", 1)
        core.insertFull(HashValue(2uL), "two", 2)
        core.insertFull(HashValue(3uL), "three", 3)

        val occ = OccupiedEntry.fromHash(core, HashValue(2uL)) { it == "two" }
        assertTrue(occ != null)
        assertEquals("two", occ.key())
        assertEquals(2, occ.get())
        assertEquals(2, occ.insert(200))
        assertEquals(200, occ.get())

        val vac = VacantEntry(core, HashValue(4uL), "four")
        vac.insert(400)
        assertEquals(4, core.len())

        val extractor = core.extract(0..3)
        assertEquals(4, extractor.remaining())
        val extracted = extractor.extractIf { it.value == 200 }
        assertTrue(extracted != null)
        assertEquals("two", extracted.key)
        assertEquals(200, extracted.value)
    }
}
