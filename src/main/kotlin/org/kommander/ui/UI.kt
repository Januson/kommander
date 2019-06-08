package org.kommander.ui

fun main() {
    val app = app("My awesome app!") {
        version = "1.0"
        author = "Kevin K. <kbknapp@gmail.com>"
        about = "Does awesome things"
        args {
            arg(name = "length") {
                short = "l"
                long = "length"
                help = "Sets a custom config file"
            }
            arg(name = "arg2") {
                name = "arg2"
            }
        }
    }
    println(app)
}

data class App(
    var name: String? = null,
    var version: String? = null,
    var author: String? = null,
    var about: String? = null,
    var args: Args? = null
)

data class Args(
    var args: MutableList<Arg>
)

data class Arg(
    var name: String? = null,
    var short: String? = null,
    var long: String? = null,
    var help: String? = null
)

fun app(name: String, block: App.() -> Unit): App {
    val p = App(name)
    block(p)
    return p
}

fun App.args(block: MutableList<Arg>.() -> Unit) {
    val arglist = mutableListOf<Arg>().apply(block)
    args = Args(arglist)
}

fun MutableList<Arg>.arg(name : String, block: Arg.() -> Unit) {
    add(Arg(name).apply(block))
}