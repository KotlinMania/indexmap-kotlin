while (true) {
    when (val `__serdeNext0` = `access`.nextElementSeed(`pairSeed`).getOrThrow()) {
        is io.github.kotlinmania.serdecore.de.SeqElement.Some -> {
            val (`key`, `value`) = `__serdeNext0`.value
            `map`.insert(`key`, `value`)
        }
        io.github.kotlinmania.serdecore.de.SeqElement.None -> break
    }
}
