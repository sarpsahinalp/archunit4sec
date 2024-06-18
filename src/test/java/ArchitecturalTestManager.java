import enforce.network.NetworkingTest;
import enforce.reflection.ReflectionTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * This class is used to run all the architectural tests.
 * It is a suite that runs all the tests in the enforce package.
 */
@Suite
@SuiteDisplayName("Demo")
@SelectClasses({ NetworkingTest.class, ReflectionTest.class})
// TODO find a way to configure the tests
public class ArchitecturalTestManager {
}
