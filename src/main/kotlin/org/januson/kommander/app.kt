package org.januson.kommander

// FIXME
@DslMarker annotation class AppDsl

@AppDsl
class App(val name: String) {

    val about = About()
    val arguments: List<String> = mutableListOf()

    fun about(init: About.() -> Unit) {
        about.init()
    }

    fun args(init: ArgBuilder.() -> Unit) {
        val builder = ArgBuilder()
        builder.init()
//        builder.arg
    }

    fun matches(arguments: Array<String>) {

    }
}

//@AppDsl
//class AppBuilder constructor() {
//
//    constructor(init: AppBuilder.() -> Unit) : this() {
//        init()
//    }
//
//    lateinit var about: About
//    val arguments = mutableListOf<String>()
//
//    fun about(init: About.() -> Unit) {
//        about.init()
//    }
//
//    fun arg(name: String) = apply { arguments.add(name) }
//
//    fun build(name: String) = App(name, about, arguments)
//}

class ArgBuilder {
    fun arg(name: String, init: Arg.() -> Unit) : Arg {
        val arg = Arg(name)
        arg.init()
        return arg
    }

    fun positional(name: String, init: Arg.() -> Unit) : Arg {
        val arg = Arg(name)
        arg.init()
        return arg
    }

    fun optional(name: String, init: Arg.() -> Unit) : Arg {
        val arg = Arg(name)
        arg.init()
        return arg
    }
}

class About(
        var version: String = "",
        var author: String = "",
        var description: String = ""
)


fun app(name: String, init: App.() -> Unit): App {
    val app = App(name)
    app.init()
    return app
}
