package org.januson.kommander

class App {

    fun args(init: ArgBuilder.() -> Unit) {
        val builder = ArgBuilder()
        builder.init()
//        builder.arg
    }

    fun matches(args: Array<String>): Matches {
        val matches = Matches()
        args.map { Arg(it) }
                .forEach { matches.addMatch(it) }
        return matches
    }
}

fun app(init: App.() -> Unit): App {
    val app = App()
    app.init()
    return app
}

class ArgBuilder {
    fun arg(name: String, init: Arg.() -> Unit): Arg {
        val arg = Arg(name)
        arg.init()
        return arg
    }
}

class Arg(val name: String) {}

class Matches {

    private val args = hashMapOf<String, Arg>()

    fun addMatch(arg: Arg) {
        args.put(arg.name, arg)
    }

    fun isPresent(arg: String): Boolean {
        return args.containsKey(arg)
    }

}

fun main(args: Array<String>) {
    args.forEach { println(it) }
}
