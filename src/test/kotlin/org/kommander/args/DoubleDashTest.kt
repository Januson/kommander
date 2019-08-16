package org.kommander.args


import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.kommander.ui.app
import org.kommander.ui.args
import org.kommander.ui.option
import org.kommander.ui.positional

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
