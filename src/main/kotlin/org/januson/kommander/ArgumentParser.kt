package org.januson.kommander

class ArgumentParser() {

    fun parse(args: Array<String>) {
        args.let { list ->
            var lastRange = mutableListOf<String>()
            list.map {
                val previousElement = lastRange.lastOrNull() ?: it
                if (it == previousElement + 2) {
                    lastRange.add(it)
                } else {
                    lastRange = mutableListOf(it)
                }
                lastRange
            }
        }
        println()
    }
}

fun main(args: Array<String>) {
    ArgumentParser().parse(arrayOf("-h", "hello", "--help"))
}