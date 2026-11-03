// port-lint: tests macros_full_path.rs
package io.github.kotlinmania.indexmap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MacrosFullPathTest {
    @Test
    fun testCreateMap() {
        val m =
            indexmapOf(
                1 to 2,
                7 to 1,
                2 to 2,
                3 to 3,
            )
        assertEquals(4, m.len())
        assertEquals(2, m[1])
        assertEquals(1, m[7])
        assertEquals(2, m[2])
        assertEquals(3, m[3])
    }

    @Test
    fun testCreateSet() {
        val s =
            indexsetOf(
                1,
                7,
                2,
                3,
            )
        assertEquals(4, s.len())
        assertTrue(s.contains(1))
        assertTrue(s.contains(7))
        assertTrue(s.contains(2))
        assertTrue(s.contains(3))
    }
}
