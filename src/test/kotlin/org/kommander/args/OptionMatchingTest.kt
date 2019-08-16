package org.kommander.args


import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.kommander.NonRepeatableArgException
import org.kommander.UnexpectedArgException
import org.kommander.ui.app
import org.kommander.ui.args
import org.kommander.ui.option

class OptionMatchingTest {

//    @Test
//    fun optionShouldHaveAValue() {
//        val args = listOf("-V")
//        val app = app("My awesome app!") {
//            args {
//                option(name = "verbose") {
//                    short = "V"
//                }
//            }
//        }
//
//        shouldThrowExactly<NonRepeatableArgException> { app.matches(args) }
//    }

    @Test
    fun singleShortOption() {
        val args = listOf("-f")
        val matches = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "f"
                }
            }
        }.matches(args)

        assertTrue(matches.isPresent("verbose"))
    }

    @Test
    fun singleLongOption() {
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
    fun repeatedNonRepeatableOption() {
        val args = listOf("-V", "-V")
        val app = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "V"
                }
            }
        }

        shouldThrowExactly<NonRepeatableArgException> { app.matches(args) }
    }

    @Test
    fun repeatedShortAndLong() {
        val args = listOf("--verbose", "-V")
        val app = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "V"
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
