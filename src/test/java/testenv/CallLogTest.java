package testenv;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testenv.archconditions.TransitiveDependencyConditionExcluding;

import java.io.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.util.Set;

public class CallLogTest {

    private static final Logger log = LoggerFactory.getLogger(CallLogTest.class);
    public static JavaClasses classes;

    @BeforeAll
    static void init() throws IOException {
        classes = new ClassFileImporter().importPackages("de.tum.cit.ase");
    }

    @Test
    void testTransitiveDependencies() {
        // recursively check for dependencies with custom condition
        ArchRuleDefinition.noClasses()
                .should(new TransitiveDependencyConditionExcluding(new DescribedPredicate<>("depend on File class") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        return javaClass.getName().equals(File.class.getName());
                    }
                }, Set.of()))
                .check(classes);
    }

    @Test
    void testTransitiveAccesses() {
        ArchRuleDefinition.noClasses()
                .should(new ArchCondition<>("transitively access classes") {
                    @Override
                    public void check(JavaClass item, ConditionEvents events) {
                        System.out.println(item.getName());
                        item.getAccessesFromSelf()
                                .forEach(access -> System.out.println(access.getTarget().getOwner().getAllAccessesFromSelf()));
                    }
                })
                .check(classes);
    }
}
