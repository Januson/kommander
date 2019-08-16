package org.kommander.help


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kommander.ui.app
import java.io.ByteArrayOutputStream

class PrintVersionTest {

    private val appName = "My Test App"

    @Test
    fun appNameOnly() {
        val out = ByteArrayOutputStream()
        app(appName) {
            config {
                output = out
            }
        }.printHelp()

        assertEquals(appName, out.toString())
    }

    @Test
    fun appNameAndVersion() {
        val out = ByteArrayOutputStream()
        app(appName) {
            version = "1.0.0"
            config {
                output = out
            }
        }.printHelp()

        assertEquals("$appName 1.0.0", out.toString())
    }

    @Test
    fun versionFlagShort() {
        val out = ByteArrayOutputStream()
        app(appName) {
            config {
                output = out
            }
        }.matches(listOf("-v"))

        assertEquals(appName, out.toString())
    }

    @Test
    fun versionFlagLong() {
        val out = ByteArrayOutputStream()
        app(appName) {
            config {
                output = out
            }
        }.matches(listOf("--version"))

        assertEquals(appName, out.toString())
    }

}
