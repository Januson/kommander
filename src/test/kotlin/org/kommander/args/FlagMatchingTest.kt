package org.kommander.args


import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kommander.*

class FlagMatchingTest {

    @Test
    fun singleShortFlag() {
        val args = listOf("-V")
        val matches = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "V"
                }
            }
        }.matches(args)

        assertTrue(matches.isPresent("verbose"))
    }

    @Test
    fun singleLongFlag() {
        val args = listOf("--verbose")
        val matches = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    long = "verbose"
                }
            }
        }.matches(args)

        assertTrue(matches.isPresent("verbose"))
    }

    @Test
    fun longAndShortShouldCountAsRepeated() {
        val args = listOf("--verbose", "-V")
        val matches = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "V"
                    long = "verbose"
                    repeatable = true
                }
            }
        }.matches(args)

        assertEquals(2, matches.occurrencesOf("verbose"))
    }

    @Test
    fun repeatableOption() {
        val args = listOf("-V", "-V", "-V")
        val matches = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "V"
                    repeatable = true
                }
            }
        }.matches(args)

        assertEquals(3, matches.occurrencesOf("verbose"))
    }

    @Test
    fun repeatedNonRepeatableFlagShort() {
        val args = listOf("-V", "-V")
        val app = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "V"
                    repeatable = false
                }
            }
        }

        shouldThrowExactly<NonRepeatableArgException> { app.matches(args) }
    }

    @Test
    fun repeatedNonRepeatableFlagLong() {
        val args = listOf("--verbose", "--verbose")
        val app = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    long = "verbose"
                    repeatable = false
                }
            }
        }

        shouldThrowExactly<NonRepeatableArgException> { app.matches(args) }
    }

    @Test
    fun repeatedNonRepeatableFlagShortAndLong() {
        val args = listOf("--verbose", "-V")
        val app = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "V"
                    long = "verbose"
                    repeatable = false
                }
            }
        }

        shouldThrowExactly<NonRepeatableArgException> { app.matches(args) }
    }

    @Test
    fun unknownFlagIsNotPresent() {
        val args = listOf<String>()
        val matches = app("My awesome app!") {
        }.matches(args)

        assertFalse(matches.isPresent("verbose"))
    }

    @Test
    fun unexpectedFlagSupplied() {
        val args = listOf("-f")
        val app = app("My awesome app!") {}

        shouldThrowExactly<UnexpectedArgException> { app.matches(args) }
    }

}
