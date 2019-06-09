package org.kommander

import java.lang.Exception

data class App(
    var name: String? = null,
    var version: String? = null,
    var author: String? = null,
    var about: String? = null,
    var args: Args? = null
) {
    fun matches(args: Collection<String>): Matches {
        val matchedFlags = mutableMapOf<String, MutableList<String>>()
        for (arg in args) {
            if (arg.startsWith("-")) {
                this.args?.contains(arg.substring(1))?.let {
                    matchedFlags.putIfAbsent(it.name, mutableListOf())
                    matchedFlags[it.name]?.add(it.name)
                }
            }
        }
        for (flag in matchedFlags) {
            val descriptor = descriptorOf(flag.key)
            if (flag.value.size > 1 && descriptor?.repeatable?.not() == true) {
                throw NonRepeatableArgException(flag.key)
            }
        }
        return Matches(matchedFlags)
    }

    fun descriptorOf(name: String) : FlagArg?  {
        return args?.options?.find { it.name == name }
    }
}

data class Args(
    var args: MutableList<Arg> = mutableListOf(),
    var options: MutableList<FlagArg> = mutableListOf()
) {

    fun add(flag: FlagArg) {
        options.add(flag)
    }

    fun contains(short: String): Arg? {
        return options.find { arg -> arg.match(short) }
    }

}

interface Arg {
    val name: String
    fun match(f: String): Boolean
}

data class FlagArg(
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
    var index: Int? = null,
    var required: Boolean? = null,
    var help: String? = null
) : Arg {
    override fun match(f: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class Matches(private val matchedFlags: MutableMap<String, MutableList<String>>) {
    fun isPresent(name: String): Boolean {
        return matchedFlags.containsKey(name)
    }

    fun occurrencesOf(name: String): Int {
        return matchedFlags[name]?.size ?: 0
    }
}

fun app(name: String, block: App.() -> Unit): App {
    val p = App(name)
    block(p)
    return p
}

fun App.args(block: Args.() -> Unit) {
    args = Args().apply(block)
}

fun Args.option(name: String, block: FlagArg.() -> Unit) {
    add(FlagArg(name).apply(block))
}

fun MutableList<Arg>.positional(name: String, block: PositionalArg.() -> Unit) {
    add(PositionalArg(name).apply(block))
}

class NonRepeatableArgException(arg: String)
    : Exception("ERROR: The argument '$arg' was provided more than once, but cannot be used multiple times!")
