package org.kommander

import java.lang.Exception

data class App(
    var name: String? = null,
    var version: String? = null,
    var author: String? = null,
    var about: String? = null,
    var args: Args = Args(emptyList(), emptyList())
) {
    fun matches(args: Collection<String>): Matches {
        val matchedOption = mutableMapOf<String, MutableList<String>>()
        val matchedPositional = mutableMapOf<String, String>()
        for (arg in args) {
            if (arg.startsWith("-")) {
                this.args.contains(arg.substring(1))?.let {
                    matchedOption.putIfAbsent(it.name, mutableListOf())
                    matchedOption[it.name]?.add(it.name)
                }
            } else {
                val argDescriptor = this.args.positionals[matchedPositional.size]
                matchedPositional[argDescriptor.name] = arg
            }
        }
        for (flag in matchedOption) {
            val descriptor = descriptorOf(flag.key)
            if (flag.value.size > 1 && descriptor?.repeatable?.not() == true) {
                throw NonRepeatableArgException(flag.key)
            }
        }
        return Matches(matchedOption, matchedPositional.toMap())
    }

    fun descriptorOf(name: String): OptionArg? {
        return args.options.find { it.name == name }
    }
}

data class ArgsBuilder(
    var positionals: MutableList<PositionalArg> = mutableListOf(),
    var options: MutableList<OptionArg> = mutableListOf()
) {

    fun add(arg: OptionArg) {
        options.add(arg)
    }

    fun add(arg: PositionalArg) {
        positionals.add(arg)
    }

}

class Args(
    val options: List<OptionArg>,
    positionals: List<PositionalArg>
) {

    val positionals: List<PositionalArg> = positionals.sortedBy { it.index }

    fun contains(short: String): Arg? {
        return options.find { arg -> arg.match(short) }
    }

    fun by(short: String): Arg? {
        return options.find { arg -> arg.match(short) }
    }

}

interface Arg {
    val name: String
    fun match(f: String): Boolean
}

data class OptionArg(
    override val name: String,
    var short: String? = null,
    var long: String? = null,
    var repeatable: Boolean = false,
    var help: String? = null
) : Arg {
    override fun match(f: String): Boolean {
        return short == f
    }
}

data class PositionalArg(
    override val name: String,
    var index: Int
) : Arg, Comparable<PositionalArg> {

    var required: Boolean? = null
    var help: String? = null

    override fun compareTo(other: PositionalArg): Int {
        return Comparator
            .comparingInt<PositionalArg> { it.index }
            .compare(this, other)
    }

    override fun match(f: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class Matches(
    private val optionArgs: MutableMap<String, MutableList<String>>,
    private val positionalArgs: Map<String, String>
) {
    fun isPresent(name: String): Boolean {
        return optionArgs.containsKey(name)
    }

    fun occurrencesOf(name: String): Int {
        return optionArgs[name]?.size ?: 0
    }

    fun valueOf(name: String): String? {
        return positionalArgs[name]
    }
}

fun app(name: String, block: App.() -> Unit): App {
    val p = App(name)
    block(p)
    return p
}

fun App.args(block: ArgsBuilder.() -> Unit) {
    val builder = ArgsBuilder().apply(block)
    args = Args(
        builder.options.toList(),
        builder.positionals.toList()
    )
}

fun ArgsBuilder.option(name: String, block: OptionArg.() -> Unit) {
    add(OptionArg(name).apply(block))
}

fun ArgsBuilder.positional(name: String, block: PositionalArg.() -> Unit) {
    add(PositionalArg(name, positionals.size + 1).apply(block))
}

class NonRepeatableArgException(arg: String) :
    Exception("ERROR: The argument '$arg' was provided more than once, but cannot be used multiple times!")
