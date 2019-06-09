package org.kommander.args


import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kommander.NonRepeatableArgException
import org.kommander.app
import org.kommander.args
import org.kommander.option

class FlagTest {

    @Test
    fun singleFlag() {
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
    fun repeatableFlag() {
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
    fun repeatedNonRepeatableFlag() {
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
    fun missingFlag() {
        val args = listOf("-v")
        val matches = app("My awesome app!") {
        }.matches(args)

        assertFalse(matches.isPresent("verbose"))
    }

}
