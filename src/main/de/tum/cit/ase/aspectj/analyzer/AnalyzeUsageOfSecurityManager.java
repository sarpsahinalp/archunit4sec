package de.tum.cit.ase.aspectj.analyzer;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import java.io.IOException;

public class AnalyzeUsageOfSecurityManager {

    public static void main(String[] args) throws IOException {
        ArchRuleDefinition.noClasses()
                .that(new DescribedPredicate<>("extend SecurityManager") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        return javaClass.getFullName().equals("sun.nio.fs.UnixNativeDispatcher");
                    }
                })
                .should(new ArchCondition<>("access SecurityManager") {
                    @Override
                    public void check(JavaClass javaClass, ConditionEvents conditionEvents) {

                        javaClass.getAccessesToSelf().forEach(access -> conditionEvents.add(SimpleConditionEvent.satisfied(javaClass, access.getOrigin().getFullName())));
                    }
                })
                .check(new ClassFileImporter().importPackages(".."));
    }
}