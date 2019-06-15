package org.kommander.args


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kommander.app
import org.kommander.args
import org.kommander.positional

class PositionalMatchingTest {

    @Test
    fun singleArg() {
        val from = "/tmp"
        val matches = app("mv") {
            args {
                positional(name = "from") {
                    index = 1
                }
            }
        }.matches(listOf(from))

        assertEquals(from, matches.valueOf("from"))
    }

    @Test
    fun twoArgs() {
        val from = "/tmp"
        val to = "/etc/target"
        val matches = app("mv") {
            args {
                positional(name = "from") {
                    index = 1
                }
                positional(name = "to") {
                    index = 2
                }
            }
        }.matches(listOf(from, to))

        assertEquals(from, matches.valueOf("from"))
        assertEquals(to, matches.valueOf("to"))
    }

    @Test
    fun indexIsOptional() {
        val from = "/tmp"
        val to = "/etc/target"
        val matches = app("mv") {
            args {
                positional(name = "from") {}
                positional(name = "to") {}
            }
        }.matches(listOf(from, to))

        assertEquals(from, matches.valueOf("from"))
        assertEquals(to, matches.valueOf("to"))
    }

    @Test
    fun indexTakesPrecedence() {
        val from = "/tmp"
        val to = "/etc/target"
        val matches = app("mv") {
            args {
                positional(name = "to") {
                    index = 2
                }
                positional(name = "from") {
                    index = 1
                }
            }
        }.matches(listOf(from, to))

        assertEquals(from, matches.valueOf("from"))
        assertEquals(to, matches.valueOf("to"))
    }

}
