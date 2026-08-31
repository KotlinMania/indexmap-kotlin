// port-lint: source macros.rs
package io.github.kotlinmania.indexmap

/**
 * Create an [IndexMap] from a list of key-value pairs.
 */
public fun <K, V> indexmapOf(vararg pairs: Pair<K, V>): IndexMap<K, V> {
    val map = IndexMap.withCapacity<K, V>(pairs.size)
    for ((key, value) in pairs) {
        map.insert(key, value)
    }
    return map
}

/**
 * Create an [IndexMap] from a list of key-value pairs.
 */
public fun <K, V> indexmap(vararg pairs: Pair<K, V>): IndexMap<K, V> = indexmapOf(*pairs)

/**
 * Create an [IndexMap] from a list of key-value pairs with a custom hasher.
 */
public fun <K, V, S> indexmapWithDefault(hasher: S, vararg pairs: Pair<K, V>): IndexMap<K, V> {
    val map = IndexMap.withCapacityAndHasher<K, V, S>(pairs.size, hasher)
    for ((key, value) in pairs) {
        map.insert(key, value)
    }
    return map
}

/**
 * Create an [IndexSet] from a list of values.
 */
public fun <T> indexsetOf(vararg elements: T): IndexSet<T> {
    val set = IndexSet.withCapacity<T>(elements.size)
    for (element in elements) {
        set.insert(element)
    }
    return set
}

/**
 * Create an [IndexSet] from a list of values.
 */
public fun <T> indexset(vararg elements: T): IndexSet<T> = indexsetOf(*elements)

/**
 * Create an [IndexSet] from a list of values with a custom hasher.
 */
public fun <T, S> indexsetWithDefault(hasher: S, vararg elements: T): IndexSet<T> {
    val set = IndexSet.withCapacityAndHasher<T, S>(elements.size, hasher)
    for (element in elements) {
        set.insert(element)
    }
    return set
}


