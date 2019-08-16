package org.kommander.help


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kommander.ui.app
import java.io.ByteArrayOutputStream

class PrintHelpTest {

    private val appName = "My Test App"

    @Test
    fun helpMethodCall() {
        val out = ByteArrayOutputStream()
        app(appName) {
            config {
                output = out
            }
        }.printHelp()

        assertEquals(appName, out.toString())
    }

    @Test
    fun helpFlagShort() {
        val out = ByteArrayOutputStream()
        app(appName) {
            config {
                output = out
            }
        }.matches(listOf("-h"))

        assertEquals(appName, out.toString())
    }

    @Test
    fun helpFlagLong() {
        val out = ByteArrayOutputStream()
        app("My Test App") {
            config {
                output = out
            }
        }.matches(listOf("--help"))

        assertEquals(appName, out.toString())
    }

}
