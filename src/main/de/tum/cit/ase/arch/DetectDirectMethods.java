package de.tum.cit.ase.arch;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import java.lang.reflect.ReflectPermission;

public class DetectDirectMethods {

    public static void main(String[] args) {
        detectDirectMethodsFromSourceTarget(
                "sun.nio.fs.WindowsFileSystemProvider.checkAccess(java.nio.file.Path, [Ljava.nio.file.AccessMode;)",
                "sun.nio.fs.WindowsFileSystemProvider");
    }

    public static void detectDirectMethodsFromSourceTarget(String targetMethod, String targetClassName) {
        JavaClasses classes = new ClassFileImporter().withImportOption(location -> location.contains("jrt")).importClasspath();

        JavaClass target = classes.get(targetClassName);

        target.getAccessesToSelf().stream()
                .filter(javaAccess -> {
                    if (targetMethod.isBlank()) {
                        return !javaAccess.getOriginOwner().getFullName().startsWith(targetClassName);
                    }
                    return javaAccess.getTarget().getFullName().equals(targetMethod);
                })
                .map(javaAccess -> javaAccess.getOrigin().getFullName())
                .distinct()
                .forEach(System.out::println);
    }
}
