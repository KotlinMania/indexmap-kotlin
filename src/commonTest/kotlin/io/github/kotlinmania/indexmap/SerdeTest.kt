// port-lint: tests serde.rs
package io.github.kotlinmania.indexmap

import io.github.kotlinmania.indexmap.map.IndexMapSeqSerializer
import io.github.kotlinmania.indexmap.map.asSequenced
import io.github.kotlinmania.serdecore.de.I32Deserialize
import io.github.kotlinmania.serdecore.de.StringDeserialize
import io.github.kotlinmania.serdetest.Token
import io.github.kotlinmania.serdetest.assertDeTokens
import io.github.kotlinmania.serdetest.assertSerTokens
import io.github.kotlinmania.serdetest.assertTokens
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

    @Test
    fun testVisitorsAndIntoDeserializer() {
        val mapVisitor = IndexMapVisitor<String, Int>()
        assertEquals("a map", mapVisitor.expecting())
        val map = mapVisitor.visitMap(listOf("k" to 1))
        assertEquals(1, map.len())
        assertEquals(listOf("k" to 1), map.intoDeserializer().asSequence().toList())

        val setVisitor = IndexSetVisitor<String>()
        assertEquals("a set", setVisitor.expecting())
        val set = setVisitor.visitSeq(listOf("v"))
        assertEquals(1, set.len())
        assertEquals(listOf("v"), set.intoDeserializer().asSequence().toList())

        val seqVisitor =
            io.github.kotlinmania.indexmap.map
                .SeqVisitor<String, Int>()
        assertEquals("a sequenced map", seqVisitor.expecting())
        val seqMap = seqVisitor.visitSeq(listOf("s" to 2))
        assertEquals(1, seqMap.len())
    }

    @Test
    fun testIndexMapSerdeTokens() {
        val map = IndexMap.new<String, Int>()
        map.insert("a", 1)
        map.insert("b", 2)
        map.insert("c", 3)

        assertTokens(
            map,
            indexMapDeserialize(StringDeserialize, I32Deserialize),
            listOf(
                Token.Map(3),
                Token.Str("a"),
                Token.I32(1),
                Token.Str("b"),
                Token.I32(2),
                Token.Str("c"),
                Token.I32(3),
                Token.MapEnd,
            ),
        )
    }

    @Test
    fun testIndexSetSerdeTokens() {
        val set = IndexSet.new<String>()
        set.insert("x")
        set.insert("y")
        set.insert("z")

        assertTokens(
            set,
            indexSetDeserialize(StringDeserialize),
            listOf(
                Token.Seq(3),
                Token.Str("x"),
                Token.Str("y"),
                Token.Str("z"),
                Token.SeqEnd,
            ),
        )
    }

    @Test
    fun testIndexMapSeqSerdeTokens() {
        val map = IndexMap.new<String, Int>()
        map.insert("first", 10)
        map.insert("second", 20)

        assertSerTokens(
            map.asSequenced(),
            listOf(
                Token.Seq(2),
                Token.Tuple(2),
                Token.Str("first"),
                Token.I32(10),
                Token.TupleEnd,
                Token.Tuple(2),
                Token.Str("second"),
                Token.I32(20),
                Token.TupleEnd,
                Token.SeqEnd,
            ),
        )

        assertDeTokens(
            map,
            io.github.kotlinmania.indexmap.map
                .deserialize(StringDeserialize, I32Deserialize),
            listOf(
                Token.Seq(2),
                Token.Tuple(2),
                Token.Str("first"),
                Token.I32(10),
                Token.TupleEnd,
                Token.Tuple(2),
                Token.Str("second"),
                Token.I32(20),
                Token.TupleEnd,
                Token.SeqEnd,
            ),
        )
    }

    @Test
    fun testSliceSerdeTokens() {
        val map = IndexMap.new<String, Int>()
        map.insert("a", 1)
        map.insert("b", 2)
        val slice = map.asSlice()

        assertSerTokens(
            slice,
            listOf(
                Token.Seq(2),
                Token.Tuple(2),
                Token.Str("a"),
                Token.I32(1),
                Token.TupleEnd,
                Token.Tuple(2),
                Token.Str("b"),
                Token.I32(2),
                Token.TupleEnd,
                Token.SeqEnd,
            ),
        )
    }

    @Test
    fun testSetSliceSerdeTokens() {
        val set = IndexSet.new<String>()
        set.insert("x")
        set.insert("y")
        val slice = set.asSetSlice()

        assertSerTokens(
            slice,
            listOf(
                Token.Seq(2),
                Token.Str("x"),
                Token.Str("y"),
                Token.SeqEnd,
            ),
        )
    }
}
