package testenv;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.dependencies.Slice;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Stopwatch;
import testenv.archconditions.TransitiveDependencyConditionExcluding;
import testenv.archconditions.TransitivelyAccessesClassesConditionExcluding;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class CallLogTest {
    public static JavaClasses classes = new ClassFileImporter().importPackages("de.tum.cit.ase");

    @Test
    void testTransitiveDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("de.tum.cit.ase");
        // Works without searching for the base packages
        // recursively check for dependencies with custom condition
        ArchRuleDefinition.noClasses()
                .should(new TransitiveDependencyConditionExcluding(new DescribedPredicate<>("depend on File class") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        System.out.println(javaClass.getName());
                        return javaClass.getName().equals(File.class.getName());
                    }
                }, Set.of()))
                .check(classes);
    }

    @Test
    void testTransitiveAccesses() {
        // TODO make a graph that has the path only since loading all the classes to memory gives an error
        List<String> packages = List.of("de.tum.cit.ase");
        ArchRuleDefinition.noClasses()
                .should(new TransitivelyAccessesClassesConditionExcluding(new DescribedPredicate<>("access File class") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        return javaClass.getName().equals(File.class.getName());
                    }
                }, Set.of()))
                .check(new ClassFileImporter().importPackages(packages));
    }

    @Test
    @Stopwatch
    void testSystemExit() {
        // TODO improve this takes too long
        List<String> packages = List.of("java.lang");
        ArchRuleDefinition.noClasses()
                .should(new ArchCondition<>("transitively access File class") {
                    @Override
                    public void check(JavaClass item, ConditionEvents events) {
                        if (!item.getName().equals(System.class.getName())) {
                            return;
                        }
                        item.getCodeUnitCallsFromSelf().stream().filter(c -> c.getOrigin().getFullName().equals("java.lang.System.exit(int)")).forEach(System.out::println);
                    }
                })
                .check(new ClassFileImporter().importPackages(packages));
    }

    @Test
    void studentsShouldNotAccessFilesMethod() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("de.tum.cit.ase.aspectj.println");

        ArchCondition<JavaClass> noTransitiveAccessToFiles = new ArchCondition<>("not access File class") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                System.out.println("hello");
                item.getAllAccessesFromSelf().forEach(System.out::println);
                System.out.println("TARGETS: ");
                System.out.println();
                item.getMethodCallsFromSelf().forEach(call -> {
                    System.out.println(call.getTargetOwner().getMethods());
                });
            }
        };

        ArchRuleDefinition.noClasses()
                .should(noTransitiveAccessToFiles)
                .check(javaClasses);

    }
}
