package de.tum.cit.ase.aspectj.analyzer;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import java.io.IOException;
import java.util.List;

public class AnalyzeUsageOfSecurityManager {

    public static void main(String[] args) throws IOException {
        List<String> bannedPackages = List.of(
                "java.util.prefs",
                "sun.security",
                "java.util.jar",
                "javax.imageio.stream",
                "javax.sound",
                "javax.swing.filechooser",
                "java.awt.desktop");
         ArchRuleDefinition.noClasses()
                .should(new ArchCondition<>("hello") {
                    @Override
                    public void check(JavaClass item, ConditionEvents events) {
//                        if (item.getFullName().equals("java.lang.System.getSecurityManager")) {
//                            return;
//                        }
                        item.getAccessesFromSelf().stream().filter(access -> access.getTarget().getFullName().startsWith("java.lang.System.getSecurityManager")).forEach(access -> events.add(SimpleConditionEvent.satisfied(item, access.getOrigin().getFullName() + " accesses " + access.getTarget().getFullName())));
                    }
                })
                .check(new ClassFileImporter().importClasspath());
    }
}