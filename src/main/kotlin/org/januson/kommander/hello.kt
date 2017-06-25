package org.januson.kommander

fun main(args: Array<String>) {
    println("Hello")

    app(name = "MyApp") {
        arg("config")
    }.matches(args)

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
