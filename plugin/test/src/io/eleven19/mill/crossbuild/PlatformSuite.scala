package io.eleven19.mill.crossbuild
import io.eleven19.mill.crossbuild.Platform

class PlatformSuite extends munit.FunSuite {
    test("Should have correct number of Platform variations") {
        val platforms = Platform.all
        assertEquals(platforms.size, 3)
    }
}
