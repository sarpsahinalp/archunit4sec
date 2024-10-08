package de.tum.cit.ase.arch;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import java.io.FilePermission;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class DetectMethodsViaCha {

    public static void main(String[] args) throws IOException {
        // do a breadth-first search to find all methods that access FilePermission
        // for each method
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(location -> location.contains("jrt"))
                .importClasspath();

        Queue<JavaAccess<?>> frontier = new LinkedList<>(classes.get(FilePermission.class).getAccessesToSelf().stream().filter(
                javaAccess -> !javaAccess.getOrigin().getFullName().startsWith("java.io.FilePermission")
        ).toList());

        Set<String> visitedMethods = new HashSet<>();

        while (!frontier.isEmpty()) {
            System.out.println(frontier.size());
            JavaAccess<? extends AccessTarget> access = frontier.poll();
            assert access != null;
            if (visitedMethods.contains(access.getOrigin().getFullName()) || isExceptionOrError(access.getOriginOwner())) {
                continue;
            }
            visitedMethods.add(access.getOrigin().getFullName());
            if (isOriginOwnerPublicAndOriginPublic(access)) {
                Files.write(Path.of("public_methods.txt"), (access.getOrigin().getFullName() + "\n").getBytes(), StandardOpenOption.APPEND);
            }

            frontier.addAll(access.getOriginOwner().getAllRawSuperclasses().stream()
                    .filter(superclass -> superclass.getAllSubclasses().size() <= 20)
                    .flatMap(javaClass -> javaClass.getAccessesToSelf().stream())
                    .filter(access1 -> access1.getTarget().getFullName().substring(access1.getTargetOwner().getFullName().length()).equals(access.getOrigin().getFullName().substring(access.getOriginOwner().getFullName().length())))
                    .toList());

            frontier.addAll(access.getOrigin().getAccessesToSelf());
        }
    }

    public static boolean isOriginOwnerPublicAndOriginPublic(JavaAccess<?> access) {
        return access.getOriginOwner().getModifiers().contains(JavaModifier.PUBLIC) && access.getOrigin().getModifiers().contains(JavaModifier.PUBLIC);
    }

    // has as parameter or argument a path, file, or string
    public static boolean hasPathFileString(JavaAccess<?> access) {
        // also check if the return type is a path, file, or string
        return access.getOrigin().getFullName().contains("Path") || access.getOrigin().getFullName().contains("File") || access.getOrigin().getFullName().contains("String");
    }

    public static boolean isExceptionOrError(JavaClass javaClass) {
        return javaClass.isAssignableTo(Exception.class) || javaClass.isAssignableTo(Error.class);
    }
}
