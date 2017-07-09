package org.januson.kommander

class Arg(val name: String) {

    var short = ""
    var long = ""
    var help = ""

    private constructor(builder: Builder) : this(builder.name)

    class Builder constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        lateinit var name: String
        var arguments = mutableListOf<String>()

        fun name(init: Builder.() -> String) = apply { name = init() }

        fun arg(name: String) = apply { arguments.add(name) }

        fun build() = Arg(this)
    }
}

fun arg(name: String, init: Arg.Builder.() -> Unit): Arg {
    val builder = Arg.Builder()
    builder.init()
    return builder.build()
}
