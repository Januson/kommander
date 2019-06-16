package org.kommander.args


import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kommander.*

class OptionMatchingTest {

    @Test
    fun singleShortFlag() {
        val args = listOf("-v")
        val matches = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "v"
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
        val args = listOf("--verbose", "-v")
        val matches = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "v"
                    long = "verbose"
                    repeatable = true
                }
            }
        }.matches(args)

        assertEquals(2, matches.occurrencesOf("verbose"))
    }

    @Test
    fun repeatableOption() {
        val args = listOf("-v", "-v", "-v")
        val matches = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "v"
                    repeatable = true
                }
            }
        }.matches(args)

        assertEquals(3, matches.occurrencesOf("verbose"))
    }

    @Test
    fun repeatedNonRepeatableOption() {
        val args = listOf("-v", "-v")
        val app = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "v"
                }
            }
        }

        shouldThrowExactly<NonRepeatableArgException> { app.matches(args) }
    }

    @Test
    fun repeatedShortAndLong() {
        val args = listOf("--verbose", "-v")
        val app = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "v"
                    long = "verbose"
                }
            }
        }

        shouldThrowExactly<NonRepeatableArgException> { app.matches(args) }
    }

    @Test
    fun missingOption() {
        val args = listOf<String>()
        val matches = app("My awesome app!") {
        }.matches(args)

        assertFalse(matches.isPresent("verbose"))
    }

    @Test
    fun tooManyArgsSupplied() {
        val args = listOf("-f")
        val app = app("mv") {}

        shouldThrowExactly<UnexpectedArgException> { app.matches(args) }
    }

}
