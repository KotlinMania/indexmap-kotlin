// port-lint: source src/lib.rs
package io.github.kotlinmania.indexmap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class LibTest {
    @Test
    fun hashValueGetExposesUnderlyingRaw() {
        val hv = HashValue(0x1234_5678_9ABC_DEF0UL)
        assertEquals(0x1234_5678_9ABC_DEF0UL, hv.get())
    }

    @Test
    fun hashValueEqualityIsStructural() {
        assertEquals(HashValue(42UL), HashValue(42UL))
        assertNotEquals(HashValue(42UL), HashValue(43UL))
        assertEquals(HashValue(0UL).hashCode(), HashValue(0UL).hashCode())
    }

    @Test
    fun bucketAccessorsReturnFields() {
        val bucket = Bucket(HashValue(7UL), key = "k", value = 10)

        assertEquals("k", bucket.keyRef())
        assertEquals(10, bucket.valueRef())
        assertEquals(10, bucket.valueMut())
        assertEquals("k" to 10, bucket.refs())
        assertEquals("k" to 10, bucket.refMut())
        assertEquals("k" to 10, bucket.muts())
        assertEquals("k" to 10, bucket.keyValue())
        assertEquals("k", bucket.consumeKey())
        assertEquals(10, bucket.consumeValue())
    }

    @Test
    fun bucketCloneCopiesEveryField() {
        val original = Bucket(HashValue(3UL), key = 1, value = "v")
        val clone = original.clone()

        assertEquals(original.hash, clone.hash)
        assertEquals(original.key, clone.key)
        assertEquals(original.value, clone.value)
        assertTrue(original !== clone)
    }

    @Test
    fun bucketCloneFromOverwritesEveryField() {
        val a = Bucket(HashValue(1UL), key = "a", value = 100)
        val b = Bucket(HashValue(99UL), key = "z", value = 999)

        a.cloneFrom(b)

        assertEquals(HashValue(99UL), a.hash)
        assertEquals("z", a.key)
        assertEquals(999, a.value)
    }

    @Test
    fun tryReserveErrorStdVariantPreservesUnderlyingDisplay() {
        val error = TryReserveError.fromAlloc("memory allocation of 42 bytes failed")
        assertEquals("memory allocation of 42 bytes failed", error.message)
    }

    @Test
    fun tryReserveErrorCapacityOverflowMessage() {
        val error = TryReserveError.fromHashbrown(HashbrownTryReserveError.CapacityOverflow)
        assertEquals(
            "memory allocation failed because the computed capacity exceeded the collection's maximum",
            error.message,
        )
    }

    @Test
    fun tryReserveErrorAllocErrorMessage() {
        val error = TryReserveError.fromHashbrown(
            HashbrownTryReserveError.AllocError(Layout(size = 16UL, align = 8UL)),
        )
        assertEquals(
            "memory allocation failed because the memory allocator returned an error",
            error.message,
        )
    }

    @Test
    fun tryReserveErrorEqualityIsStructural() {
        val a = TryReserveError.fromHashbrown(HashbrownTryReserveError.CapacityOverflow)
        val b = TryReserveError.fromHashbrown(HashbrownTryReserveError.CapacityOverflow)
        val c = TryReserveError.fromAlloc("other")
        assertEquals(a, b)
        assertNotEquals(a, c)
    }

    @Test
    fun getDisjointMutErrorDisplay() {
        assertEquals("an index is out of bounds", GetDisjointMutError.IndexOutOfBounds.toString())
        assertEquals("there were overlapping indices", GetDisjointMutError.OverlappingIndices.toString())
    }
}
