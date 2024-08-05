package de.tum.rules.testenv2;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import java.util.Set;

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
            "org.jgrapth",
            "de.tum"
    );

    private static final Set<String> bannedClasses = Set.of(
            "java.lang.Object",
            "sun.security",
            "java.lang.String",
            "jdk.javadoc",
            "sun.java2d",
            "org.jcp",
            "java.security",
            "java.awt.font",
            "jdk.jfr",
            "org.junit.jupiter",
            "org.junit",
            "jdk.vm.ci",
            "javax.swing",
            "java.security.AccessControl",
            "java.util",
            "sun.util",
            "java.net",
            "sun.rmi",
            "org.graalvm",
            "jdk.internal",
            "sun.jvm",
            "jdk.jshell",
            "sun.tools",
            "com.sun.tools",
            "javax.management",
            "java.time",
            "com.oracle.graal",
            "java.awt.Font",
            "com.sun.media.sound",
            "sun.awt.www.content.audio",
            "javax.sound.midi",
            "com.sun.rowset",
            "javax.sound.sampled",
            "com.sun.org"
    );

    public static void main(String[] args) {
        JavaClasses classes = new ClassFileImporter().importClasspath();

        JavaClass filePermission = classes.get("java.lang.SecurityManager");

        filePermission.getAccessesToSelf().stream()
                .filter(access -> access.getTarget().getFullName().startsWith("java.lang.SecurityManager.checkRead"))
                .map(access -> access.getOrigin().getFullName())
                .distinct()
                .sorted()
                .forEach(System.out::println);

        filePermission.getAccessesToSelf().stream()
                .filter(access -> access.getTarget().getFullName().equals("sun.nio.fs.WindowsFileSystem.getRootDirectories()"))
                .flatMap(access -> access.getTargetOwner().getAllRawSuperclasses()
                        .stream()
                        .filter(superclass -> superclass.getAllSubclasses().size() <= 20)
                        .flatMap(javaClass -> javaClass.getAccessesToSelf().stream()))
                .map(access -> access.getOrigin().getFullName())
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

//    public static void main(String[] args) throws IOException {
//        JavaClasses classes = new ClassFileImporter().importClasspath();
//
//        JavaClass filePermission = classes.get("sun.nio.fs.WindowsFileSystem");
//
//        Queue<JavaAccess<?>> frontier = filePermission.getAccessesToSelf()
//                .stream()
//                .filter(access -> access.getTarget().getFullName().startsWith("sun.nio.fs.WindowsFileSystem.getRootDirectories()"))
//                .distinct()
//                .collect(Collectors.toCollection(LinkedList::new));
//
//        Set<String> visitedMethods = new HashSet<>();
//        Path path = Files.createFile(Path.of("filesystemtest2.csv"));
//
//        while (!frontier.isEmpty()) {
//            System.out.println(frontier.size());
//            JavaAccess<? extends AccessTarget> access = frontier.poll();
//            assert access != null;
//            if (packages.stream().anyMatch(access.getOrigin().getFullName()::startsWith)) {
//                continue;
//            }
//            if (bannedClasses.stream().anyMatch(access.getOrigin().getFullName()::startsWith)) {
//                continue;
//            }
//            if (visitedMethods.contains(access.getOrigin().getFullName())) {
//                continue;
//            }
//            visitedMethods.add(access.getOrigin().getFullName());
//            String originName = access.getOrigin().getFullName() + "\n";
//            if (isOriginOwnerPublicAndOriginPublic(access)) {
//               // write to file
//                Files.write(path, originName.getBytes(), java.nio.file.StandardOpenOption.APPEND);
//            }
//
//            frontier.addAll(access.getOriginOwner().getAllRawSuperclasses()
//                    .stream()
//                    .filter(superclass -> superclass.getAllSubclasses().size() <= 20)
//                    .flatMap(javaClass -> javaClass.getAccessesToSelf().stream())
//                            .filter(a -> a.getTarget().getName().equals(access.getOrigin().getName()))
//                    .collect(Collectors.toSet())
//            );
//            frontier.addAll(access.getOrigin().getAccessesToSelf());
//        }
//    }

    public static boolean isOriginOwnerPublicAndOriginPublic(JavaAccess<?> access) {
        return access.getOriginOwner().getModifiers().contains(JavaModifier.PUBLIC) && access.getOrigin().getModifiers().contains(JavaModifier.PUBLIC);
    }
}
