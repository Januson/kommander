package org.januson.kommander

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.kotlintest.specs.FeatureSpec
import io.kotlintest.specs.FreeSpec

class AppTest : FeatureSpec() {
    init {
        feature("The argument matching") {
            scenario("should explode when I touch it") {
                // test here
            }
            scenario("and should do this when I wibble it") {
                // test heree
            }
        }
    }
}

class MyTests : BehaviorSpec() {
    init {
        given("a broomstick") {
            `when`("I sit on it") {
                then("I should be able to fly") {
                    // test code
                }
            }
            `when`("I throw it away") {
                then("it should come back") {
                    // test code
                }
            }
        }
    }
}

class MyTestss : FreeSpec() {
    init {
        "String.length" - {
            "should return the length of the string" {
                "sammy".length shouldBe 5
                "".length shouldBe 0
            }
        }
    }
}