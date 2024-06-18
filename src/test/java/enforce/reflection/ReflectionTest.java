package enforce.reflection;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
 import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

    @Test
    void noReflectionTest() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("org.sarps");
        ArchRuleDefinition.classes()
                .should()
                .accessClassesThat()
                .resideInAnyPackage("java.lang.reflect..")
                .check(importedClasses);
    }

    @Test
    void shouldHaveStrictTimeOut() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("enforce.network");
        // check if every test has the annotation test with a name of should

        ArchRuleDefinition.methods()
                .that().haveName("should.*")
                .should().beAnnotatedWith("de.tum.in.test.api.StrictTimeout")
                .andShould().haveRawReturnType("void")
                .allowEmptyShould(true)
                .check(importedClasses);
    }
}
