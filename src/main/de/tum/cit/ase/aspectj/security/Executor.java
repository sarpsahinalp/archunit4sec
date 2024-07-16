package de.tum.cit.ase.aspectj.security;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import de.tum.cit.ase.aspectj.analyzer.TransitivelyAccessesMethodsCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Executor {

    private static final Logger log = LoggerFactory.getLogger(Executor.class);

    public static void main(String[] args) {
        noClassesShouldAccessFileSystem("de.tum.cit.ase.aspectj.test").check(new ClassFileImporter().importPackages(".."));
    }

    public static ArchRule noClassesShouldAccessFileSystem(String studentPackage) {
        // Packages that should not be accessed
        List<String> bannedPackages = List.of(
                "java.nio.file",
                "java.util.prefs",
                "sun.print",
                "sun.security",
                "java.util.jar",
                "sun.awt.X11",
                "javax.imageio.stream",
                "javax.sound.midi",
                "javax.swing.filechooser",
                "java.awt.desktop");

        return ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(studentPackage + "..")
                .should(new TransitivelyAccessesMethodsCondition(new DescribedPredicate<>("accesses file system") {
                    @Override
                    public boolean test(JavaAccess<?> javaAccess) {
                        if (javaAccess.getTarget().getFullName().equals("java.io.FilePermission.<init>(java.lang.String, java.lang.String)")) {
                            return true;
                        }

                        // These packages should not be accessed
                        return bannedPackages.contains(javaAccess.getTargetOwner().getPackageName());
                    }
                }));
    }
}
