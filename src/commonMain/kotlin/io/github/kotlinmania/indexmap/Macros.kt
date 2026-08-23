// port-lint: source macros.rs
package io.github.kotlinmania.indexmap

// Create an IndexMap from a list of key-value pairs.
public fun <K, V> indexmapOf(vararg pairs: Pair<K, V>): IndexMap<K, V> {
    val map = IndexMap.withCapacity<K, V>(pairs.size)
    for ((key, value) in pairs) {
        map.insert(key, value)
    }
    return map
}

// Create an IndexSet from a list of values.
public fun <T> indexsetOf(vararg elements: T): IndexSet<T> {
    val set = IndexSet.withCapacity<T>(elements.size)
    for (element in elements) {
        set.insert(element)
    }
    return set
}
