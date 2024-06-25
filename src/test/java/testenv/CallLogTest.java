package testenv;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testenv.archconditions.TransitiveDependencyConditionExcluding;

import java.io.*;
import java.util.Set;

public class CallLogTest {

    private static final Logger log = LoggerFactory.getLogger(CallLogTest.class);
    public static JavaClasses classes;

    @BeforeAll
    static void init() throws IOException {
        classes = new ClassFileImporter().importPackages("de.tum.cit.ase");
    }

    @Test
    void testCallLog() {
        // recursively check for dependencies with custom condition
        ArchRuleDefinition.noClasses()
                .should(new TransitiveDependencyConditionExcluding(new DescribedPredicate<>("asd") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        return javaClass.getName().equals(File.class.getName());
                    }
                }, Set.of(Object.class.getName(), String.class.getName(), System.class.getName())))
                .check(classes);

    }
}
