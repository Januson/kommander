package org.kommander.help


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kommander.app
import java.io.ByteArrayOutputStream

class PrintVersionTest {

    @Test
    fun appNameOnly() {
        val out = ByteArrayOutputStream()
        app("My Test App") {
            config {
                output = out
            }
        }.printHelp()

        assertEquals("My Test App", out.toString())
    }

    @Test
    fun appNameAndVersion() {
        val out = ByteArrayOutputStream()
        app("My Test App") {
            version = "1.0.0"
            config {
                output = out
            }
        }.printHelp()

        assertEquals("My Test App 1.0.0", out.toString())
    }

    @Test
    fun versionFlagShort() {
        val out = ByteArrayOutputStream()
        app("My Test App") {
            config {
                output = out
            }
        }.matches(listOf("-v"))

        assertEquals("My Test App", out.toString())
    }

    @Test
    fun versionFlagLong() {
        val out = ByteArrayOutputStream()
        app("My Test App") {
            config {
                output = out
            }
        }.matches(listOf("--version"))

        assertEquals("My Test App", out.toString())
    }

}
