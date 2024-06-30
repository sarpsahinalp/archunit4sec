package testenv;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Stopwatch;
import testenv.archconditions.TransitiveDependencyConditionExcluding;
import testenv.archconditions.TransitivelyAccessesClassesConditionExcluding;
import testenv.archconditions.TransitivelyAccessesMethodsCondition;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CallLogTest {
    public static JavaClasses classes = new ClassFileImporter().importPackages("de.tum.cit.ase");


    @Test
    void testTransitiveBlacklistedClasses() {
        JavaClasses classes = new ClassFileImporter().importPackages("de.tum.cit.ase");
        ArchRuleDefinition.noClasses()
                .should()
                .transitivelyDependOnClassesThat(new DescribedPredicate<>("sdsdsds") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        return javaClass.getFullName().equals("java.io.FileOutputStream");
                    }
                })
                .check(classes);
    }

    @Test
    void testTransitiveDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("de.tum.cit.ase");
        // Works without searching for the base packages
        // recursively check for dependencies with custom condition
        ArchRuleDefinition.noClasses().should(new TransitiveDependencyConditionExcluding(new DescribedPredicate<>("depend on File class") {
            @Override
            public boolean test(JavaClass javaClass) {
                System.out.println(javaClass.getName());
                return javaClass.getName().equals(File.class.getName());
            }
        }, Set.of())).check(classes);
    }

    @Test
    void testTransitiveAccesses() {
        // TODO make a graph that has the path only since loading all the classes to memory gives an error
        List<String> packages = List.of("de.tum.cit.ase");
        ArchRuleDefinition.noClasses().should(new TransitivelyAccessesClassesConditionExcluding(new DescribedPredicate<>("access File class") {
            @Override
            public boolean test(JavaClass javaClass) {
                return javaClass.getName().equals(File.class.getName());
            }
        }, Set.of())).check(new ClassFileImporter().importPackages(packages));
    }

    @Test
    @Stopwatch
    void testSystemExit() {
        // TODO improve this takes too long
        List<String> packages = List.of("java.lang");
        ArchRuleDefinition.noClasses().should(new ArchCondition<>("transitively access File class") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                if (!item.getName().equals(System.class.getName())) {
                    return;
                }
                item.getCodeUnitCallsFromSelf().stream().filter(c -> c.getOrigin().getFullName().equals("java.lang.System.exit(int)")).forEach(System.out::println);
            }
        }).check(new ClassFileImporter().importPackages(packages));
    }

    @Test
    void studentsShouldNotAccessFilesMethod() {
        JavaClasses javaClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests()).importPackages("..");

        ArchCondition<JavaClass> noTransitiveAccessToFiles = new ArchCondition<>("not access File class") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                System.out.println("hello");
                Set<JavaCodeUnitAccess<?>> method = item.getCodeUnitAccessesFromSelf();
                System.out.println(method);
                System.out.println("TARGETS: ");
                System.out.println();
                for (JavaCodeUnitAccess<?> access : method) {
                    System.out.println(access.getTarget().getFullName());
                    printAccesses(access, new HashSet<>());
                }
            }

            private void printAccesses(JavaCodeUnitAccess<?> method, Set<String> visited) {
                Set<JavaCodeUnitAccess<?>> accesses= method.getTargetOwner().getCodeUnitAccessesFromSelf()
                        .stream().filter(a -> a.getOrigin().getFullName().equals(method.getTarget().getFullName()))
                        .collect(Collectors.toSet());

                for (JavaCodeUnitAccess<?> access : accesses) {
                    if (visited.contains(access.getTarget().getFullName())) {
                        continue;
                    }
                    if (access.getTarget().getFullName().startsWith("java.lang")) {
                        continue;
                    }
                    if (!access.getTarget().getFullName().startsWith("java") && !access.getTarget().getFullName().startsWith("sun") && !access.getTarget().getFullName().startsWith("jdk")) {
                        continue;
                    }
                    visited.add(access.getTarget().getFullName());
                    System.out.println(access.getTarget().getFullName());
                    printAccesses(access, visited);
                }
            }
        };

        ArchRuleDefinition.noClasses().that().resideInAnyPackage("de.tum.cit.ase.aspectj.println").should(noTransitiveAccessToFiles).check(javaClasses);
    }


    /**
     * This is the latest implementation for transitive checks for filesystem access
     */
    @Test
    void testTransitiveAccessesMethods() {
//        JavaClasses classes = new ClassFileImporter().importPackages(
//                "..");
        JavaClasses classes = new ClassFileImporter().importPackages(
                "java.io",
                "java.nio.file",
                "jdk.internal",
                "sun.nio",
                "sun.awt",
                "sun.print",
                "java.util.prefs",
                "java.util.zip",
                "java.util.jar",
                "java.security",
                "sun.security",
                "de.tum.cit.ase.aspectj.println");
        ArchRuleDefinition
                .noClasses()
                .that()
                .resideInAnyPackage("de.tum.cit.ase.aspectj.println")
                .should(new TransitivelyAccessesMethodsCondition(new DescribedPredicate<>("access File class") {
            @Override
            public boolean test(JavaAccess<?> javaClass) {
                return javaClass.getTarget().getFullName().equals("java.io.FileOutputStream.open0(java.lang.String, boolean)");
            }
        })).check(classes);
    }
}
