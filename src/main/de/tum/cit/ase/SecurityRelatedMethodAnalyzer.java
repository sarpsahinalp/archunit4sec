package de.tum.cit.ase;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

public class SecurityRelatedMethodAnalyzer {

    // FilePermission
    public static void analyzeFileSystemAccess(JavaClasses classes) {

    }

    // NetPermission, SocketPermission
    public static void analyzeNetworkAccess(JavaClasses classes) {

    }

    // ProcessBuilder and Runtime.exec
    public static void analyzeExecTerminal(JavaClasses classes) {

    }

    // checkExit
    public static void analyzeJVMTermination(JavaClasses classes) {

    }

    // ReflectionPermission
    public static void analyzeReflection(JavaClasses classes) {

    }

    // SecurityManager
    public static void analyzeSecurityManager(JavaClasses classes) {
        ArchRuleDefinition.noClasses()
                .should()
                .accessTargetWhere(new DescribedPredicate<JavaAccess<?>>("read") {
                    @Override
                    public boolean test(JavaAccess<?> javaAccess) {
                        return javaAccess.getTarget().getFullName().startsWith("java.io.File.delete()");
                    }
                })
                .check(classes);
    }

    public static void analyzeNativeMethods(JavaClasses classes) {

    }


    public static void main(String[] args) throws IOException {
        JavaClasses classes = new ClassFileImporter().withImportOption(location -> location.contains("jrt")).importClasspath();
        analyzeSecurityManager(classes);

        Paths.get("de", "tum", "cit", "ase", "ares", "api",
                "templates", "architecture", "java", "archunit", "rules", "key" + ".txt");

        System.out.println(FileSystems.getDefault().getFileStores().iterator().next().type());
    }
}
