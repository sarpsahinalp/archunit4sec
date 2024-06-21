package enforce.reflection;

import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
 import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.thirdparty.com.google.common.collect.ImmutableCollection;
import com.tngtech.archunit.thirdparty.com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ch.qos.logback.classic.pattern.Util.match;

public class ReflectionTest {

    @Test
    void noReflectionTest() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("org.sarps");
        ArchRuleDefinition.noClasses()
                .should()
                .accessClassesThat()
                .resideInAnyPackage("java.lang.reflect..")
                .check(importedClasses);
    }

    @Test
    void shouldHaveStrictTimeOut() {
        ClassFileImporter importer = new ClassFileImporter();
        // check if every test has the annotation test with a name of should


        // TODO recursively check if any of the other classes are using reflection


        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("org.sarps")
                .should()
                .transitivelyDependOnClassesThat()
                .resideInAPackage("java.lang.reflect..")
                .check(importer.importPackages("org.sarps"));
    }


}
