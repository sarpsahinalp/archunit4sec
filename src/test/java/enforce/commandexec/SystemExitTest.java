package enforce.commandexec;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class SystemExitTest {

    // TODO needs improvement can use Regex to identify the method name
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
