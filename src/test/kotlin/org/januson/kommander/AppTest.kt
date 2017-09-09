package org.januson.kommander

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.FeatureSpec

class AppTest : FeatureSpec() {
    init {
        feature("The argument matching") {

            scenario("Argument passed to the program should be present in matches") {
                val args = arrayOf("help")
                val matches = app {
                    args {
                        arg(name = "help") {}
                    }
                }.matches(args)

                matches.isPresent("help") shouldBe true
            }

            scenario("Argument that is not passed to the program should not be present in matches") {
                val args = arrayOf<String>()
                val matches = App().matches(args)
                matches.isPresent("help") shouldBe false
            }
        }
    }
}