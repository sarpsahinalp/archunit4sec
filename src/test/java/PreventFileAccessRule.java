import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

public class PreventFileAccessRule {

    @Test
    void shouldPreventFileAccessCompletely() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("student");

        ArchRule rule = ArchRuleDefinition.noClasses()
                .should().accessClassesThat().resideInAnyPackage("java.io..");
        rule.check(importedClasses);
    }

    @Test
    void shouldEnableFileAccessOnlyToSpecifiedFile() {

    }


}
