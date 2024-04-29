package enforce.network;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import de.tum.in.test.api.StrictTimeout;
import org.junit.jupiter.api.Test;

public class NetworkingTest {

    @StrictTimeout(1000)
    @Test
    void shouldNetworkingTest() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("org/sarps");

        ArchRule rule = ArchRuleDefinition.noClasses()
                .should().dependOnClassesThat().resideInAnyPackage("java.net..")
                .because("we don't want to use networking libraries");

        rule.check(importedClasses);
    }
}
