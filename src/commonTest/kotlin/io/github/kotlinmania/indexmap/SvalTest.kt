// port-lint: tests sval.rs
package io.github.kotlinmania.indexmap

import kotlin.test.Test
import kotlin.test.assertEquals

class SvalTest {
    private class TestStream : SvalStream {
        val events = mutableListOf<String>()

        override fun mapBegin(len: Int?) {
            events.add("mapBegin($len)")
        }

        override fun mapKeyBegin() {
            events.add("mapKeyBegin")
        }

        override fun mapKeyEnd() {
            events.add("mapKeyEnd")
        }

        override fun mapValueBegin() {
            events.add("mapValueBegin")
        }

        override fun mapValueEnd() {
            events.add("mapValueEnd")
        }

        override fun mapEnd() {
            events.add("mapEnd")
        }

        override fun seqBegin(len: Int?) {
            events.add("seqBegin($len)")
        }

        override fun seqValueBegin() {
            events.add("seqValueBegin")
        }

        override fun seqValueEnd() {
            events.add("seqValueEnd")
        }

        override fun seqEnd() {
            events.add("seqEnd")
        }

        override fun value(v: Any?) {
            events.add("value($v)")
        }
    }

    @Test
    fun mapStreamEvents() {
        val map = IndexMap<String, Int>()
        map.insert("a", 1)
        map.insert("b", 2)

        val stream = TestStream()
        map.svalStream(stream)

        val expected = listOf(
            "mapBegin(2)",
            "mapKeyBegin", "value(a)", "mapKeyEnd",
            "mapValueBegin", "value(1)", "mapValueEnd",
            "mapKeyBegin", "value(b)", "mapKeyEnd",
            "mapValueBegin", "value(2)", "mapValueEnd",
            "mapEnd",
        )
        assertEquals(expected, stream.events)
    }

    @Test
    fun setStreamEvents() {
        val set = IndexSet<String>()
        set.insert("x")
        set.insert("y")

        val stream = TestStream()
        set.svalStream(stream)

        val expected = listOf(
            "seqBegin(2)",
            "seqValueBegin", "value(x)", "seqValueEnd",
            "seqValueBegin", "value(y)", "seqValueEnd",
            "seqEnd",
        )
        assertEquals(expected, stream.events)
    }
}
