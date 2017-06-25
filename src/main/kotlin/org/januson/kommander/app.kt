package org.januson.kommander

class App(val name: String, val arguments: List<String>) {

    private constructor(builder: Builder) : this(builder.name, builder.arguments)

    fun matches(arguments: Array<String>) {

    }

    class Builder constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        lateinit var name: String
        var arguments = mutableListOf<String>()

        fun name(init: Builder.() -> String) = apply { name = init() }

        fun arg(name: String) = apply { arguments.add(name) }

        fun build() = App(this)
    }
}

fun app(name: String, init: App.Builder.() -> Unit): App {
    val builder = App.Builder()
    builder.init()
    return builder.build()
}
