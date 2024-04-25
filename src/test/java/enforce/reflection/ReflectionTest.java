package enforce.reflection;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.AccessTarget;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

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

    @Test
    void shouldNotCallSystemExit() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("org.sarps");

        ArchRule rule = ArchRuleDefinition.noFields()
                .should()
                .haveFullName("System.exit(0)");

        rule.check(importedClasses);
    }
}
