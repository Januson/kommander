package org.kommander

import java.io.OutputStream
import java.lang.Exception

class ExpectedFlag(
    val name: String,
    val short: String = "",
    val long: String = "",
    val repeatable: Boolean = false
) {
    fun matches(arg:String): Boolean {
        return this.short == arg || this.long == arg
    }
}

class ExpectedPositional(
    val name: String,
    val index: Int
)

class MatchesOf(
    val expectedFlags: List<ExpectedFlag>,
    val expectedOptions: List<ExpectedFlag>,
    val expectedPositionals: List<ExpectedPositional>,
    val args: List<String>
) {
    fun matches(): Matches {
        val matchedFlags = mutableMapOf<String, Int>()
        val matchedOption = mutableMapOf<String, Int>()
        val matchedPositional = mutableMapOf<String, String>()
        for (arg in args) {
            if (arg == "--") {
                args.subList(args.indexOf("--") + 1, args.size)
                    .forEach {
                        val argDescriptor = this.expectedPositionals[matchedPositional.size]
                        matchedPositional[argDescriptor.name] = it
                    }
                return Matches(matchedFlags, matchedOption, matchedPositional.toMap())
            }
            if (arg.startsWith("-") || arg.startsWith("--")) {
                val expectedFlag = this.expectedFlags
                    .find { it.matches(arg.trimStart('-'))  }
                    ?: this.expectedOptions.find { it.matches(arg.trimStart('-'))  }
                if (expectedFlag != null) {
                    matchedFlags[expectedFlag.name] = matchedFlags[expectedFlag.name]?.plus(1) ?: 1
                } else {
                    if (this.expectedPositionals.size == matchedPositional.size) {
                        throw UnexpectedArgException(arg)
                    }
                }
            } else {
                if (this.expectedPositionals.size == matchedPositional.size) {
                    throw UnexpectedArgException(arg)
                }
                val argDescriptor = this.expectedPositionals[matchedPositional.size]
                matchedPositional[argDescriptor.name] = arg
            }
        }
        for (flag in matchedFlags) {
            val descriptor = descriptorOf(flag.key)
            if (flag.value > 1 && descriptor?.repeatable?.not() == true) {
                throw NonRepeatableArgException(flag.key)
            }
        }
        for (flag in matchedOption) {
            val descriptor = descriptorOf(flag.key)
            if (flag.value > 1 && descriptor?.repeatable?.not() == true) {
                throw NonRepeatableArgException(flag.key)
            }
        }
        return Matches(matchedFlags, matchedOption, matchedPositional.toMap())
    }

    private fun descriptorOf(name: String): ExpectedFlag? {
        return this.expectedFlags.find { it.name == name } ?:
            this.expectedOptions.find { it.name == name }
    }
}

data class VersionHelp(private val name: String, private val version: String = "") {
    fun printTo(media: OutputStream) {
        media.write(
            "${this.name} ${this.version}".trim()
                .toByteArray(Charsets.UTF_8)
        )
    }
}

data class AppConfig(var output: OutputStream)

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
    fun toExpected(): ExpectedFlag
}

data class OptionArg(
    override val name: String,
    var short: String = "",
    var long: String = "",
    var repeatable: Boolean = false,
    var help: String? = null
) : Arg {
    override fun toExpected(): ExpectedFlag {
        return ExpectedFlag(name, short, long, repeatable)
    }

    override fun match(f: String): Boolean {
        return short == f || long == f
    }
}

data class FlagArg(
    override val name: String,
    var short: String = "",
    var long: String = "",
    var repeatable: Boolean = false,
    var help: String? = null
) : Arg {
    override fun match(f: String): Boolean {
        return short == f || long == f
    }

    override fun toExpected(): ExpectedFlag {
        return ExpectedFlag(name, short, long, repeatable)
    }
}

data class PositionalArg(
    override val name: String,
    var index: Int
) : Arg, Comparable<PositionalArg> {

    var required: Boolean? = null
    var help: String? = null

    override fun toExpected(): ExpectedFlag {
        return ExpectedFlag(name)
    }

    fun toPositional(): ExpectedPositional {
        return ExpectedPositional(name, index)
    }

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
    private val flagArgs: MutableMap<String, Int>,
    private val optionArgs: MutableMap<String, Int>,
    private val positionalArgs: Map<String, String>
) {
    fun isPresent(name: String): Boolean {
        return flagArgs.containsKey(name) ||
        return optionArgs.containsKey(name) ||
                positionalArgs.containsKey(name)
    }

    fun occurrencesOf(name: String): Int {
        return flagArgs[name] ?:
            optionArgs[name] ?: 0
    }

    fun valueOf(name: String): String? {
        return positionalArgs[name]
    }
}

class NonRepeatableArgException(arg: String) :
    Exception("ERROR: The argument '$arg' was provided more than once, but cannot be used multiple times!")

class UnexpectedArgException(arg: String) :
    Exception("ERROR: Found argument '$arg' which wasn't expected, or isn't valid in this context!")
