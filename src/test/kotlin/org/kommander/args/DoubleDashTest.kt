package org.kommander.args


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kommander.app
import org.kommander.args
import org.kommander.option
import org.kommander.positional

class DoubleDashTest {

    @Test
    fun matchesFollowingArgsAsPositional() {
        val args = listOf("-f", "--", "-r", "-d")
        val matches = app("test") {
            args {
                option("file") {
                    short = "f"
                }
                option("from") {
                    short = "r"
                }
                option("debug") {
                    short = "d"
                }
                positional(name = "source") {}
                positional(name = "target") {}
            }
        }.matches(args)

        assertTrue(matches.isPresent("file"))
        assertFalse(matches.isPresent("from"))
        assertFalse(matches.isPresent("debug"))
        assertTrue(matches.isPresent("source"))
        assertTrue(matches.isPresent("target"))
    }

}
