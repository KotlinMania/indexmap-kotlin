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

// Alias matching upstream indexmap! macro.
public fun <K, V> indexmap(vararg pairs: Pair<K, V>): IndexMap<K, V> = indexmapOf(*pairs)

// Create an IndexMap with custom hasher builder matching upstream indexmap_with_default! macro.
public fun <K, V, S> indexmapWithDefault(hasher: S, vararg pairs: Pair<K, V>): IndexMap<K, V> {
    val map = IndexMap.withCapacityAndHasher<K, V, S>(pairs.size, hasher)
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

// Alias matching upstream indexset! macro.
public fun <T> indexset(vararg elements: T): IndexSet<T> = indexsetOf(*elements)

// Create an IndexSet with custom hasher builder matching upstream indexset_with_default! macro.
public fun <T, S> indexsetWithDefault(hasher: S, vararg elements: T): IndexSet<T> {
    val set = IndexSet.withCapacityAndHasher<T, S>(elements.size, hasher)
    for (element in elements) {
        set.insert(element)
    }
    return set
}


