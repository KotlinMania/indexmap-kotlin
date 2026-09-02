// port-lint: tests tests/tests.rs
package io.github.kotlinmania.indexmap

import kotlin.test.Test
import kotlin.test.assertEquals

class TestsTest {
    @Test
    fun testSort() {
        val m =
            indexmapOf(
                1 to 2,
                7 to 1,
                2 to 2,
                3 to 3,
            )

        val sorted = m.sortedBy { _: Int, v1: Int, _: Int, v2: Int -> v1.compareTo(v2) }
        assertEquals(
            listOf(7 to 1, 1 to 2, 2 to 2, 3 to 3),
            sorted,
        )
    }

    @Test
    fun testSortSet() {
        val s =
            indexsetOf(
                1,
                7,
                2,
                3,
            )

        assertEquals(
            listOf(1, 2, 3, 7),
            s.sortedBy { v1: Int, v2: Int -> v1.compareTo(v2) },
        )
    }

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

        val m2 =
            indexmap(
                "a" to 1,
                "b" to 2,
            )
        assertEquals(2, m2.len())
        assertEquals(1, m2.get("a"))
        assertEquals(2, m2.get("b"))

        val m3 =
            indexmapWithDefault(
                Unit,
                "a" to 1,
                "b" to 2,
            )
        assertEquals(2, m3.len())
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

        val s2 =
            indexset(
                "a",
                "b",
            )
        assertEquals(2, s2.len())
        assertEquals(true, s2.contains("a"))
        assertEquals(false, s2.contains("c"))

        val s3 =
            indexsetWithDefault(
                Unit,
                "a",
                "b",
            )
        assertEquals(2, s3.len())
    }
}
