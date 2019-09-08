package org.kommander.args


import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.kommander.NonRepeatableArgException
import org.kommander.OptionValueIsMissingException
import org.kommander.UnexpectedArgException
import org.kommander.ui.app
import org.kommander.ui.args
import org.kommander.ui.option

class OptionMatchingTest {

    @Test
    fun optionValueIsMissing() {
        val args = listOf("-V")
        val app = app("My awesome app!") {
            args {
                option(name = "verbose") {
                    short = "V"
                }
            }
        }

        shouldThrowExactly<OptionValueIsMissingException> { app.matches(args) }
    }

    @Test
    fun singleShortOption() {
        val file = "test.txt"
        val matches = app("My awesome app!") {
            args {
                option(name = "file") {
                    short = "f"
                }
            }
        }.matches(listOf("-f", file))

        assertTrue(matches.isPresent("file"))
        assertEquals(file, matches.valueOf("file"))
    }

    @Test
    fun singleLongOption() {
        val file = "test.txt"
        val args = listOf("--file", file)
        val matches = app("My awesome app!") {
            args {
                option(name = "file") {
                    long = "file"
                }
            }
        }.matches(args)

        assertTrue(matches.isPresent("file"))
        assertEquals(file, matches.valueOf("file"))
    }

    @Test
    fun optionCanBeRepeatable() {
        val args = listOf("-f", "test.txt", "-f", "test.txt", "-f", "test.txt")
        val matches = app("My awesome app!") {
            args {
                option(name = "file") {
                    short = "f"
                    repeatable = true
                }
            }
        }.matches(args)

        assertEquals(3, matches.occurrencesOf("file"))
    }

    @Test
    fun optionCanBeRepeatableMixed() {
        val one = "first.txt"
        val two = "second.txt"
        val args = listOf("--file", one, "-f", two)
        val matches = app("My awesome app!") {
            args {
                option(name = "file") {
                    short = "f"
                    long = "file"
                    repeatable = true
                }
            }
        }.matches(args)

        assertEquals(2, matches.occurrencesOf("file"))
        assertEquals(listOf(one, two), matches.valuesOf("file"))
    }

    @Test
    fun repeatedNonRepeatableOption() {
        val args = listOf("-f", "test.txt", "-f", "test2.txt")
        val app = app("My awesome app!") {
            args {
                option(name = "file") {
                    short = "f"
                    repeatable = false
                }
            }
        }

        shouldThrowExactly<NonRepeatableArgException> { app.matches(args) }
    }

    @Test
    fun missingOptionShouldNotBePresent() {
        val args = listOf<String>()
        val matches = app("My awesome app!") {
        }.matches(args)

        assertFalse(matches.isPresent("verbose"))
    }

    @Test
    fun unknownOption() {
        val args = listOf("-f")
        val app = app("mv") {}

        shouldThrowExactly<UnexpectedArgException> { app.matches(args) }
    }

}
