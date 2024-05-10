package io.eleven19.mill.crossbuild

class VersionStringSuite extends munit.FunSuite {
    test("Should have correct number of parts") {
        val version = VersionString("1.2.3")
        assertEquals(version.parts.size, 3)
    }

    test("Should have correct major and minor parts") {
        val version = VersionString("1.2.3")
        assertEquals(version.majorMinorParts, Some((1, 2)))
    }

    test("Should have correct major and minor parts for single digit version") {
        val version = VersionString("1")
        assertEquals(version.majorMinorParts, Some((1, 0)))
    }

    test("Should return none for invalid version") {
        val version = VersionString("v20")
        assertEquals(version.majorMinorParts, None)
    }
}
