package org.januson.kommander

import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

class App constructor(val name: String, val arguments: List<String>) {

    private constructor(builder: Builder) : this(builder.name, builder.arguments)

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
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

fun main(args: Array<String>) {
    println("Hello")
    app(name = "MyApp") {
        arg("config")
    }
//            .matches(args)

    App.create {
        name = "Peter"
    }


//    app(name = "MyApp",
//        version = "1.0",
//        author = "Kevin K. <kbknapp@gmail.com>",
//        about = "Does awesome things" ) {
//        args {
//            arg("config") {
//                short = "c"
//                long = "config"
//                value_name = "FILE"
//                help = "Sets a custom config file"
//                takes_value = true
//            }
//            arg("output") {
//                help = "Sets an optional output file"
//                index = 1
//            }
//            arg("debug") {
//                short = "d"
//                multiple = true
//                help = "Turn debugging information on"
//            }
//        }
//        subcommand("test") {
//            about = "does testing things"
//            args {
//                arg("list") {
//                    short = "l"
//                    help = "lists test values"
//                }
//            }
//        }
//    }
}
