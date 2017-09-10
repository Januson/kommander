package org.januson.kommander

class Matches {

    private val args = hashMapOf<String, Arg>()

    fun addMatch(arg: Arg) {
        args.put(arg.name, arg)
    }

    fun isPresent(arg: String): Boolean {
        return args.containsKey(arg)
    }

}