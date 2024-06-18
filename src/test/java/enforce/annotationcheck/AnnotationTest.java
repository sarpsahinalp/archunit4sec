package enforce.annotationcheck;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class AnnotationTest {

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
