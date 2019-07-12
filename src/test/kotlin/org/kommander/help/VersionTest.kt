package org.kommander.help


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kommander.app
import java.io.ByteArrayOutputStream

class VersionTest {

    @Test
    fun matchesFollowingArgsAsPositional() {
        val out = ByteArrayOutputStream()
        app("My Test App") {
            config {
                output = out
            }
        }.printHelp()

        assertEquals("My Test App", out.toString())
    }

    @Test
    fun matchesFollowingArgsAsPositional2() {
        val out = ByteArrayOutputStream()
        app("My Test App") {
            version = "1.0.0"
            config {
                output = out
            }
        }.printHelp()

        assertEquals("My Test App 1.0.0", out.toString())
    }

}
