package io.eleven19.mill.morphir.api
import upickle.default.*
import mill.api.JsonFormatters.*
import utest.*

object MorphirProjectConfigSuite extends utest.TestSuite {
    val tests = utest.Tests {
        test("MorphirProjectConfig should serialize to JSON") {
            val config = MorphirProjectConfig(
                name = "test",
                sourceDirectory = "test",
                exposedModules = List("TestModule"),
                dependencies = List("TestDependency"),
                localDependencies = List("TestLocalDependency"),
            )
            val json = write(config)
            val expectedJson =
                """{"name":"test","sourceDirectory":"test","exposedModules":["TestModule"],"dependencies":["TestDependency"],"localDependencies":["TestLocalDependency"]}"""
            assert(json == expectedJson)
        }

        test("MorphirProjectConfig should deserialize from JSON") {
            val json =
                """{"name":"test","sourceDirectory":"test","exposedModules":["TestModule"],"dependencies":["TestDependency"],"localDependencies":["TestLocalDependency"]}"""
            val config = read[MorphirProjectConfig](json)
            val expectedConfig = MorphirProjectConfig(
                name = "test",
                sourceDirectory = "test",
                exposedModules = List("TestModule"),
                dependencies = List("TestDependency"),
                localDependencies = List("TestLocalDependency"),
            )
            assert(config == expectedConfig)
        }
    }
}
