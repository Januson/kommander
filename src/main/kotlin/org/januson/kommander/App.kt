package org.januson.kommander

class App(private val args: List<Arg>) {

    fun matches(args: Array<String>): Matches {

        val matches = Matches(this)
        args.map { Arg(it) }
                .forEach { matches.addMatch(it) }
        return matches
    }
}

class AppBuilder {

    private lateinit var args: List<Arg>

    fun args(init: ArgsBuilder.() -> Unit) {
        val builder = ArgsBuilder()
        builder.init()
        args = builder.build()
    }

    fun build() = App(args)

}

class ArgsBuilder {

    private val args = mutableListOf<Arg>()

    fun arg(name: String, init: ArgBuilder.() -> Unit) {
        val arg = ArgBuilder(name)
        arg.init()
        args.add(arg.build())
    }

    fun build() = args.toList()

}

fun app(init: AppBuilder.() -> Unit): App {
    val app = AppBuilder()
    app.init()
    return app.build()
}
