// port-lint: tests equivalent_trait.rs
package io.github.kotlinmania.indexmap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EquivalentTraitTest {
    data class Pair<A, B>(
        val first: A,
        val second: B,
    )

    @Test
    fun testLookup() {
        val map =
            indexmapOf(
                Pair("a", "b") to 1,
                Pair("a", "x") to 2,
            )

        assertTrue(map.containsKey(Pair("a", "b")))
        assertFalse(map.containsKey(Pair("b", "a")))
    }

    @Test
    fun testStringStr() {
        val map =
            indexmapOf(
                "a" to 1,
                "b" to 2,
                "x" to 3,
                "y" to 4,
            )

        assertTrue(map.containsKey("a"))
        assertFalse(map.containsKey("z"))
        assertEquals(2, map.swapRemove("b"))
    }
}
