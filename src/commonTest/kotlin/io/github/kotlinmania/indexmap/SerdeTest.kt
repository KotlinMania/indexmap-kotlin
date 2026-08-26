// port-lint: tests serde.rs
package io.github.kotlinmania.indexmap

import io.github.kotlinmania.indexmap.map.IndexMapSeqSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SerdeTest {
    @Test
    fun testIndexMapSerialization() {
        val map = IndexMap.new<String, Int>()
        map.insert("a", 1)
        map.insert("b", 2)
        map.insert("c", 3)

        val serializer = IndexMapSerializer(String.serializer(), Int.serializer())
        val jsonString = Json.encodeToString(serializer, map)
        assertEquals("""{"a":1,"b":2,"c":3}""", jsonString)

        val deserialized = Json.decodeFromString(serializer, jsonString)
        assertEquals(3, deserialized.len())
        assertEquals(1, deserialized.get("a"))
        assertEquals(2, deserialized.get("b"))
        assertEquals(3, deserialized.get("c"))
    }

    @Test
    fun testIndexSetSerialization() {
        val set = IndexSet.new<String>()
        set.insert("x")
        set.insert("y")
        set.insert("z")

        val serializer = IndexSetSerializer(String.serializer())
        val jsonString = Json.encodeToString(serializer, set)
        assertEquals("""["x","y","z"]""", jsonString)

        val deserialized = Json.decodeFromString(serializer, jsonString)
        assertEquals(3, deserialized.len())
        assertEquals(listOf("x", "y", "z"), deserialized.toList())
    }

    @Test
    fun testIndexMapSeqSerialization() {
        val map = IndexMap.new<String, Int>()
        map.insert("first", 10)
        map.insert("second", 20)

        val serializer = IndexMapSeqSerializer(String.serializer(), Int.serializer())
        val jsonString = Json.encodeToString(serializer, map)
        val deserialized = Json.decodeFromString(serializer, jsonString)
        assertEquals(2, deserialized.len())
        assertEquals(10, deserialized.get("first"))
        assertEquals(20, deserialized.get("second"))
    }
}
