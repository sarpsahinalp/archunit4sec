package de.tum.rules.analyzer;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import java.io.FilePermission;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyzeSecurityCriticalViolations {

    public static void main(String[] args) throws IOException {
        JavaClasses classes = new ClassFileImporter().importClasspath();

        JavaClass filePermission = classes.get(FilePermission.class);

        Queue<JavaAccess<?>> frontier = filePermission.getAccessesToSelf().stream().filter(access -> !access.getOriginOwner().getFullName().equals(FilePermission.class.getName())).distinct().collect(Collectors.toCollection(LinkedList::new));

        Set<String> visitedMethods = new HashSet<>();
        Path path = Files.createFile(Path.of("test2.csv"));

        while (!frontier.isEmpty()) {
            System.out.println(frontier.size());
            JavaAccess<? extends AccessTarget> access = frontier.poll();
            if (access.getOrigin().getFullName().startsWith("de.tum")) {
                continue;
            }
            if (visitedMethods.contains(access.getOrigin().getFullName())) {
                continue;
            }
            visitedMethods.add(access.getOrigin().getFullName());
            String originName = access.getOrigin().getFullName() + "\n";
            if (isOriginOwnerPublicAndOriginPublic(access)) {
               // write to file
                Files.write(path, originName.getBytes(), java.nio.file.StandardOpenOption.APPEND);
            }
            frontier.addAll(access.getOrigin().getAccessesToSelf());
        }
    }

    public static boolean isOriginOwnerPublicAndOriginPublic(JavaAccess<?> access) {
        return access.getOriginOwner().getModifiers().contains(JavaModifier.PUBLIC) && access.getOrigin().getModifiers().contains(JavaModifier.PUBLIC);
    }
}
