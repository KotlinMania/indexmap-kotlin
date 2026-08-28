// port-lint: tests arbitrary.rs
package io.github.kotlinmania.indexmap

import kotlin.test.Test
import kotlin.test.assertEquals

class ArbitraryTest {
    private class DummyUnstructured(
        val items: List<Int>,
    ) : Unstructured {
        override fun <T> arbitraryIter(generator: (Unstructured) -> T): Sequence<T> = sequence {
            for (item in items) {
                yield(generator(this@DummyUnstructured))
            }
        }

        override fun <T> arbitraryTakeRestIter(generator: (Unstructured) -> T): Sequence<T> = sequence {
            for (item in items) {
                yield(generator(this@DummyUnstructured))
            }
        }
    }

    @Test
    fun arbitraryMapCreation() {
        var counter = 0
        val u = DummyUnstructured(listOf(1, 2, 3))
        val map = IndexMap.arbitrary(u, { "k${counter++}" }, { counter * 10 })
        assertEquals(3, map.len())
    }

    @Test
    fun arbitrarySetCreation() {
        var counter = 0
        val u = DummyUnstructured(listOf(1, 2, 3))
        val set = IndexSet.arbitrary(u) { "elem${counter++}" }
        assertEquals(3, set.len())
    }
}
