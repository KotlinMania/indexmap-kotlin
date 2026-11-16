while (true) {
    when (val `__serdeNext0` = `access`.nextElementSeed(`seed`).getOrThrow()) {
        is io.github.kotlinmania.serdecore.de.SeqElement.Some -> {
            val `value` = `__serdeNext0`.value
            `values`.insert(`value`)
        }
        io.github.kotlinmania.serdecore.de.SeqElement.None -> break
    }
}
