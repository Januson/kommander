package org.januson.kommander

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.FeatureSpec

class ArgumentMatchingTest : FeatureSpec() {
    init {
        feature("Matches") {

            scenario("Argument passed to the program should be present in matches") {
                val args = arrayOf("--help")
                val app = app {
                    args {
                        arg(name = "help") {
                            short = "h"
                            long = "help"
                        }
                    }
                }
                val matches = app.matches(args)

                matches.isPresent("help") shouldBe true
            }

            scenario("Argument that is not passed to the program should not be present in matches") {
                val args = arrayOf<String>()
                val matches = app {}.matches(args)
                matches.isPresent("help") shouldBe false
            }

            scenario("Argumenta passed to the program should be present in matches") {
                val args = arrayOf("--help")
                val app = app {
                    args {
                        arg(name = "help") {
                            short = "h"
                            long = "help"
                        }
                    }
                }
                val matches = app.matches(args)

                matches.isPresent("help") shouldBe true
            }
        }
    }
}