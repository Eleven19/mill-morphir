package io.eleven19.mill.morphir.itest

import mill.testkit.IntegrationTester
import utest.*

object MorphirSpec extends TestSuite:
  val resourceFolder = os.Path(sys.env("MILL_ITEST_PROJECT_ROOT"))
  
  // We need to pass the mill executable path to the integration tester
  val millExecutable = os.Path(sys.env("MILL_EXECUTABLE_PATH"))

  val tester = new IntegrationTester(
    daemonMode = true,
    workspaceSourcePath = resourceFolder,
    millExecutable = millExecutable,
  )
    
  // The basic-standalone project expects to find the plugin in the local repo
  // We are not publishing to a local repo in the test setup in build.mill yet, 
  // but let's assume standard Mill testing flow where we can just compile and run.
  // Wait, Mill 1.x IntegrationTester usually handles copying sources.
  // We need to make sure the plugin classpath is available to the test project.
  // The `mill-morphir` plugin is imported via $ivy in the test project.
  // Since we are running in a test suite defined IN the main build, we rely on `publishLocalTestRepo`.
  
  // In `build.mill`, we mapped "MILL_USER_TEST_REPO" to the local test repo path.
  // But wait, the `mill-aliases` example injects this into `build.mill`.
  // Our `basic-standalone/build.mill` has `import $ivy...`.
  // We should do the same: replace a placeholder with the actual version/repo if needed
  // OR just rely on standard mill testkit behavior if it helps.

  // Let's stick to the manual injection for now as it's explicit.
  // However, for simplicity, I will first just try to run the `verify` command defined in `build.mill` 
  
  println("Running tests in dir: " + tester.workspacePath)

  val testRepo = sys.env.getOrElse("MILL_USER_TEST_REPO", "/tmp")
  println(s"DEBUG: testRepo env var: $testRepo")

  val tests: Tests = Tests:
    test("verify"):
      // No file modification needed if we use COURSIER_REPOSITORIES env var


      // Prepare NPM modules
      val prepRes = tester.eval("prepare")
      assert(prepRes.isSuccess)
      
      // Run the verification task which builds the Morphir IR and asserts its existence
      val res = tester.eval("verify")
      assert(res.isSuccess)

end MorphirSpec
