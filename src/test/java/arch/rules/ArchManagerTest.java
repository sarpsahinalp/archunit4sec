//package arch.rules;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tngtech.archunit.core.domain.JavaClasses;
//import com.tngtech.archunit.core.importer.ClassFileImporter;
//import com.tngtech.archunit.core.importer.ImportOption;
//import com.tngtech.archunit.junit.AnalyzeClasses;
//import com.tngtech.archunit.junit.ArchTest;
//import com.tngtech.archunit.lang.ArchRule;
//import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
//import com.tngtech.archunit.library.Architectures;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.condition.DisabledIf;
//import org.junitpioneer.jupiter.DisableIfTestFails;
//
//import java.io.IOException;
//import java.nio.file.Path;
//
///**
// * This class is used to run all the architectural tests.
// * It is a suite that runs all the tests in the enforce package.
// */
////@Suite
////@SuiteDisplayName("Demo")
////@SelectClasses()
//// Do not go on with tests if one of the tests fails
//@DisableIfTestFails
//
//// TODO find a way to configure the tests
//// TODO Take a look at junit pioneer https://junit-pioneer.org/
//@AnalyzeClasses(packages = "de.tum.cit.ase", importOptions = ImportOption.DoNotIncludeTests.class)
//class ArchManagerTest {
//
//    public static JavaClasses classes;
//    public static ArchitecturalTestProperties properties;
//
//    @BeforeAll
//    static void init() throws IOException {
//        classes = new ClassFileImporter().importPackages("de.tum.cit.ase");
//        properties = parseJsonFile("src/test/java/arch/rules/conf.json");
//    }
//
//    @Test
//    // Might be useful to have a test that checks if the configuration is correct
//    @DisabledIf("preventNetworkAccess")
//    void preventUnrestrictedAccessToFileSystem() {
//        ArchRuleDefinition.noClasses().should().transitivelyDependOnClassesThat().resideInAnyPackage("java.io.File", "java.nio.file").because("Access to the file system is not allowed")
//                .check(classes);
//    }
//
//    @Test
//    void testPreventUnrestrictedAccessToFileSystem() {
//        Architectures.
//                layeredArchitecture()
//                .consideringAllDependencies()
//                .layer("files")
//                .definedBy("java.io.File", "java.nio.file")
//                .whereLayer("student")
//                .mayNotBeAccessedByAnyLayer();
//    }
//
//
//    /**
//     * This rule prevents unrestricted access to the network.
//     */
//    @ArchTest
//    public static final ArchRule preventReflectionAccess = ArchRuleDefinition.noClasses().should().transitivelyDependOnClassesThat().resideInAnyPackage("java.lang.reflect").because("Reflective programming should be restricted for security reasons");
//
//
//    /**
//     * This rule prevents unrestricted access to the network.
//     */
//    @ArchTest
//    public static final ArchRule preventCommandExecution = ArchRuleDefinition.noClasses().should().transitivelyDependOnClassesThat().belongToAnyOf(Runtime.class, ProcessBuilder.class).because("Executing commands should be restricted for security reasons");
//
//    /**
//     * This rule prevents unrestricted access to the network.
//     */
//    @ArchTest
//    public static final ArchRule preventNetworkConnections = ArchRuleDefinition.noClasses().should().transitivelyDependOnClassesThat().resideInAnyPackage("java.net", "javax.net").because("Network connections should be restricted for security reasons");
//
//    /**
//     * This rule prevents unrestricted access to the network.
//     */
//    @ArchTest
//    public static final ArchRule preventModifyingSSL = ArchRuleDefinition.noClasses().should().transitivelyDependOnClassesThat().resideInAnyPackage("javax.net.ssl").because("Modifying SSL settings should be restricted for security reasons");
//
//    /**
//     * This rule prevents unrestricted access to the network.
//     */
//    @ArchTest
//    public static final ArchRule preventAuthentication = ArchRuleDefinition.noClasses().should().transitivelyDependOnClassesThat().resideInAnyPackage("javax.security.auth").because("Modifying authentication should be restricted for security reasons");
//
//    /**
//     * This rule prevents unrestricted access to the network.
//     */
//    @ArchTest
//    public static final ArchRule preventManagement = ArchRuleDefinition.noClasses().should().transitivelyDependOnClassesThat().resideInAnyPackage("java.lang.management", "javax.management").because("Modifying management should be restricted for security reasons");
//
//
//    /**
//     * Prevents the use of AWT and Swing.
//     */
//    @ArchTest
//    void preventAWT(JavaClasses classes) {
//        ArchRuleDefinition.noClasses().should().transitivelyDependOnClassesThat()
//                .resideInAnyPackage("java.awt", "javax.swing").because("Utilizing AWT should be restricted for security reasons")
//                .check(classes);
//    }
//
//    public static final ArchRule preventAWT = ArchRuleDefinition.noClasses().should().transitivelyDependOnClassesThat()
//            .resideInAnyPackage("java.awt", "javax.swing").because("Utilizing AWT should be restricted for security reasons");
//
//    public static ArchitecturalTestProperties parseJsonFile(String filePath) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(Path.of(filePath).toFile(), ArchitecturalTestProperties.class);
//    }
//
//    static boolean preventNetworkAccess() {
//        return properties.networkAccess();
//    }
//}
