package org.januson.kommander

class App(val name: String, val about: About, val arguments: List<String>) {

    private constructor(builder: Builder) : this(builder.name, builder.about, builder.arguments)

    fun matches(arguments: Array<String>) {

    }

    class Builder constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        lateinit var name: String
        lateinit var about: About
        val arguments = mutableListOf<String>()

        fun about(init: About.Builder.() -> Unit) {
            val builder = About.Builder()
            builder.init()
            about = builder.build()
        }

        fun arg(name: String) = apply { arguments.add(name) }

        fun build() = App(this)
    }
}

class About(val varsion: String, val author: String, val description: String) {

    private constructor(builder: Builder) : this(builder.version, builder.author, builder.description)

    fun matches(arguments: Array<String>) {

    }

    class Builder constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        lateinit var version: String
        lateinit var author: String
        lateinit var description: String

        fun build() = About(this)
    }
}

fun app(name: String, init: App.Builder.() -> Unit): App {
    val builder = App.Builder()
    builder.init()
    builder.name =name
    return builder.build()
}
