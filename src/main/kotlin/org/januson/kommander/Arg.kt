package org.januson.kommander

class Arg(val name: String) {}

class ArgBuilder(val name: String) {

    var long: String = ""
    var short: String = ""

    fun build(): Arg {
        return Arg(name)
    }
}