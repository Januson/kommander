package org.kommander.ui

import org.kommander.AppConfig
import org.kommander.ExpectedFlag
import org.kommander.ExpectedPositional
import org.kommander.FlagArg
import org.kommander.Matches
import org.kommander.MatchesOf
import org.kommander.OptionArg
import org.kommander.PositionalArg
import org.kommander.VersionHelp

data class AppBuilder(
    val name: String,
    var version: String? = null,
    var author: String? = null,
    var about: String? = null,
    var config: AppConfig = AppConfig(output = System.out),
    var args: ArgsBuilder = ArgsBuilder(mutableListOf(), mutableListOf())
) {
    fun matches(args: List<String>): Matches {
        if (args.contains("-v") || args.contains("--version")) {
            printHelp()
            return Matches(mutableMapOf(), mutableMapOf(), mapOf())
        }
        if (args.contains("-h") || args.contains("--help")) {
            printHelp()
            return Matches(mutableMapOf(), mutableMapOf(), mapOf())
        }
        return MatchesOf(
            this.args.flags.map { it.toExpected() }.toList(),
            this.args.options.map { it.toExpected() }.toList(),
            this.args.positionals.map { it.toPositional() }.sortedBy { it.index }.toList(),
            args
        ).matches()
    }

    fun descriptorOf(name: String): OptionArg? {
        return args.options.find { it.name == name }
    }

    fun config(block: AppConfig.() -> Unit) {
        this.config.apply(block)
    }

    fun printHelp() {
        VersionHelp(this.name, this.version ?: "")
            .printTo(this.config.output)
    }

    fun add(arg: FlagArg) {
        this.args.add(arg)
    }

    fun add(arg: OptionArg) {
        this.args.add(arg)
    }

    fun add(arg: PositionalArg) {
        this.args.add(arg)
    }

}

data class ArgsBuilder(
    var flags: MutableList<FlagArg> = mutableListOf(),
    var options: MutableList<OptionArg> = mutableListOf(),
    var positionals: MutableList<PositionalArg> = mutableListOf()
) {

    fun add(arg: FlagArg) {
        flags.add(arg)
    }

    fun add(arg: OptionArg) {
        options.add(arg)
    }

    fun add(arg: PositionalArg) {
        positionals.add(arg)
    }

}


fun app(name: String, block: AppBuilder.() -> Unit): AppBuilder {
    val p = AppBuilder(name)
    block(p)
    return p
}

fun AppBuilder.args(block: ArgsBuilder.() -> Unit) {
    this.args.apply(block)
//    args = Args(
//        builder.options.toList(),
//        builder.positionals.toList()
//    )
}

fun ArgsBuilder.flag(name: String, block: OptionArg.() -> Unit) {
    add(OptionArg(name).apply(block))
}

fun ArgsBuilder.option(name: String, block: OptionArg.() -> Unit) {
    add(OptionArg(name).apply(block))
}

fun AppBuilder.flag(name: String, block: OptionArg.() -> Unit) {
    add(OptionArg(name).apply(block))
}

fun AppBuilder.option(name: String, block: OptionArg.() -> Unit) {
    add(OptionArg(name).apply(block))
}

fun AppBuilder.positional(name: String, block: PositionalArg.() -> Unit) {
    add(PositionalArg(name, this.args.positionals.size + 1).apply(block))
}

fun ArgsBuilder.positional(name: String, block: PositionalArg.() -> Unit) {
    add(PositionalArg(name, this.positionals.size + 1).apply(block))
}

fun main() {
    app("a") {
        args {
            flag(name = "verbose") {
                short = "V"
                long = "verbose"
                repeatable = false
            }
            option(name = "file") {
                short = "f"
                long = "file"
                repeatable = false
            }
            positional(name = "from") {
                index = 1
            }
        }
        flag(name = "optional") {
            short = "o"
            long = "optional"
            repeatable = false
        }
        option(name = "target") {
            short = "t"
            long = "target"
            repeatable = false
        }
        positional(name = "to") {
            index = 2
        }
    }
}