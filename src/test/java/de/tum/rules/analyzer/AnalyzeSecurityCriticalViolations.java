package de.tum.rules.analyzer;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import jdk.net.NetworkPermission;

import java.io.FilePermission;
import java.io.IOException;
import java.net.NetPermission;
import java.net.SocketPermission;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class AnalyzeSecurityCriticalViolations {

    private static final Set<String> packages = Set.of(
            "org.eclipse",
            "oshi",
            "com.google",
            "com.ibm",
            "com.tngtech",
            "org.apache",
            "org.antlr",
            "org.apfloat",
            "org.jgrapth"
    );

//    public static void main(String[] args) {
//        JavaClasses classes = new ClassFileImporter().importClasspath();
//
//        JavaClass filePermission = classes.get(SocketPermission.class);
//
//        filePermission.getAccessesToSelf().stream()
//                .filter(access -> !access.getOriginOwner().getFullName().startsWith(SocketPermission.class.getName()))
////                .filter(access -> access.getTarget().getFullName().equals("java.awt.Font.checkFontFile(int, java.io.File)"))
//                .map(access -> access.getOrigin().getFullName())
//                .distinct()
//                .forEach(System.out::println);
//    }

    public static void main(String[] args) throws IOException {
        JavaClasses classes = new ClassFileImporter().importClasspath();

        JavaClass filePermission = classes.get("java.lang.SecurityManager");

        Queue<JavaAccess<?>> frontier = filePermission.getAccessesToSelf()
                .stream()
                .filter(access -> access.getTarget().getFullName().equals("java.lang.SecurityManager.checkDelete(java.lang.String)"))
                .distinct()
                .collect(Collectors.toCollection(LinkedList::new));

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
