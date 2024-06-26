package testenv;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Stopwatch;
import testenv.archconditions.TransitiveDependencyConditionExcluding;

import java.io.*;
import java.util.List;
import java.util.Set;

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
        List<String> packages = List.of("..");
        ArchRuleDefinition.noClasses()
                .should(new ArchCondition<>("transitively access File class") {
                    @Override
                    public void check(JavaClass item, ConditionEvents events) {
                        System.out.println(item.getName());
                    }
                })
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
}
