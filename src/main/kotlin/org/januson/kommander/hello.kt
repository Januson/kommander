package org.januson.kommander

fun main(args: Array<String>) {
    println("Hello")

    app(name = "MyApp") {
        about {
            version = "1.0"
            author = "Kevin K. <kbknapp@gmail.com>"
            description = "Does awesome things"
        }
        args {
            optional(name = "config") {
                short = "c"
                long = "config"
//                value_name = "FILE"
//                takes_value = true
                help = "Sets a custom config file"
            }
            positional("output") {
                help = "Sets an optional output file"
//                index = 1
            }
            optional("debug") {
                short = "d"
//                multiple = true
                help = "Turn debugging information on"
            }
        }
    }.matches(args)

//    app(name = "MyApp") {
//        about {
//            version = "1.0"
//            author = "Kevin K. <kbknapp@gmail.com>"
//            description = "Does awesome things"
//        }
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
