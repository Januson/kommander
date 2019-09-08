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
        val matchedOption = mutableMapOf<String, MutableList<String>>()
        val matchedPositional = mutableMapOf<String, String>()
        val argsIt= args.iterator()
        while (argsIt.hasNext()) {
            val arg = argsIt.next()
            if (arg == "--") {
                while (argsIt.hasNext()) {
                    val it = argsIt.next()
                    val argDescriptor = this.expectedPositionals[matchedPositional.size]
                    matchedPositional[argDescriptor.name] = it
                }
                return Matches(matchedFlags, matchedOption, matchedPositional.toMap())
            }
            if (arg.startsWith("-") || arg.startsWith("--")) {
                val trimmedArg = arg.trimStart('-')
                val expectedFlag = this.expectedFlags.find { it.matches(trimmedArg)  }
                val expectedOption = this.expectedOptions.find { it.matches(arg.trimStart('-'))  }
                if (expectedFlag != null) {
                    matchedFlags[expectedFlag.name] = matchedFlags[expectedFlag.name]?.plus(1) ?: 1
                } else if (expectedOption != null) {
                    if (argsIt.hasNext()) {
                        matchedOption.putIfAbsent(expectedOption.name, mutableListOf())
                        matchedOption[expectedOption.name]?.add(argsIt.next())
                    } else {
                        throw OptionValueIsMissingException("asd")
                    }
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
            if (flag.value.size > 1 && descriptor?.repeatable?.not() == true) {
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
    private val optionArgs: MutableMap<String, MutableList<String>>,
    private val positionalArgs: Map<String, String>
) {
    fun isPresent(name: String): Boolean {
        return flagArgs.containsKey(name) ||
                optionArgs.containsKey(name) ||
                positionalArgs.containsKey(name)
    }

    fun occurrencesOf(name: String): Int {
        return flagArgs[name] ?:
            optionArgs[name]?.size ?: 0
    }

    fun valueOf(name: String): String? {
        return positionalArgs[name] ?: optionArgs[name]?.first()
    }

    fun valuesOf(name: String): List<String>? {
        return optionArgs[name]?.toList()
    }
}

class NonRepeatableArgException(arg: String) :
    Exception("ERROR: The argument '$arg' was provided more than once, but cannot be used multiple times!")

class UnexpectedArgException(arg: String) :
    Exception("ERROR: Found argument '$arg' which wasn't expected, or isn't valid in this context!")

class OptionValueIsMissingException(arg: String) :
    Exception("ERROR: Found argument '$arg' which wasn't expected, or isn't valid in this context!")
