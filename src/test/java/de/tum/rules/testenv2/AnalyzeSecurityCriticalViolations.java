package de.tum.rules.testenv2;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class AnalyzeSecurityCriticalViolations {

    public static void main(String[] args) throws IOException {
        withSuperclasses(args);
    }

    public static void withSuperclasses(String[] args) {
        JavaClasses classes = new ClassFileImporter().importClasspath();

        JavaClass filePermission = classes.get("java.lang.SecurityManager");

        filePermission.getAccessesToSelf().stream()
                .filter(access -> access.getTarget().getFullName().startsWith("java.lang.SecurityManager.checkRead"))
                .map(access -> access.getTarget().getFullName() + " ----> " + access.getOrigin().getFullName())
                .distinct()
                .sorted()
                .forEach(System.out::println);

        filePermission.getAccessesToSelf().stream()
                .filter(access -> access.getTarget().getFullName().equals("java.lang.SecurityManager.checkRead"))
                .flatMap(access -> access.getTargetOwner().getAllRawSuperclasses()
                        .stream()
                        .filter(superclass -> superclass.getAllSubclasses().size() <= 20)
                        .flatMap(javaClass -> javaClass.getAccessesToSelf().stream()))
                .map(access -> access.getTarget().getFullName() + " ----> " + access.getOrigin().getFullName())
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    public static void withEverything(String[] args) throws IOException {
//        Neo4jDriver driver = new Neo4jDriver("neo4j+s://6f125562.databases.neo4j.io", "neo4j", "NdCd-os00qFeuV_NtQ1mAFn7aU2KF1xL-zGXBathOcY");

        JavaClasses classes = new ClassFileImporter()
                .withImportOption(location -> location.contains("jrt"))
                .importClasspath();

        List<String> methods = Files.readAllLines(Path.of("test.txt"));

        Queue<JavaAccess<?>> frontier = new LinkedList<>();

        for (JavaClass javaClass : classes) {
            frontier.addAll(javaClass.getMethodCallsToSelf().stream()
                    .filter(javaAccess -> methods.contains(javaAccess.getTarget().getFullName()))
                    .toList());
        }

        Set<String> visitedMethods = new HashSet<>();



        while (!frontier.isEmpty()) {
            System.out.println(frontier.size());
            JavaAccess<? extends AccessTarget> access = frontier.poll();
            assert access != null;
            if (visitedMethods.contains(access.getOrigin().getFullName())) {
                continue;
            }
            visitedMethods.add(access.getOrigin().getFullName());
            String originName = access.getOrigin().getFullName() + "\n";
            if (isOriginOwnerPublicAndOriginPublic(access)) {
                Files.write(Path.of("methods.csv"), originName.getBytes(), StandardOpenOption.APPEND);
            }

            frontier.addAll(access.getOrigin().getAccessesToSelf());
        }
    }

    /**
     * This method checks if any class in the given package accesses the security manager.
     */
    public static void whereSecurityManagerIsCalled(String methodName, String javaClass) {
        Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        // Save them to a graph and export using graphviz
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(location -> location.contains("jrt"))
                .importClasspath();

        JavaClass filePermission = classes.get(javaClass);

        filePermission.getAccessesToSelf().stream()
//                .filter(access -> access.getTarget().getFullName().startsWith(methodName))
                .forEach(javaAccess -> {
                    graph.addVertex(javaAccess.getOrigin().getFullName());
                    graph.addVertex(javaAccess.getTarget().getFullName());
                    graph.addEdge(javaAccess.getOrigin().getFullName(), javaAccess.getTarget().getFullName());
                });

        filePermission.getAccessesToSelf().stream()
//                .filter(access -> access.getTarget().getFullName().equals(methodName))
                .flatMap(access -> access.getTargetOwner().getAllRawSuperclasses()
                        .stream()
                        .filter(superclass -> superclass.getAllSubclasses().size() <= 20)
                        .flatMap(javaClass2 -> javaClass2.getAccessesToSelf().stream()))
                .forEach(javaAccess -> {
                    graph.addVertex(javaAccess.getOrigin().getFullName());
                    graph.addVertex(javaAccess.getTarget().getFullName());
                    graph.addEdge(javaAccess.getOrigin().getFullName(), javaAccess.getTarget().getFullName());
                });

        // export graph
        exportToDotFile("read-advanced.dot", graph);
    }

    public static void exportToDotFile(String filePath, Graph<String, DefaultEdge> graph) {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v));
            return map;
        });

        try (Writer writer = new FileWriter(filePath)) {
            exporter.exportGraph(graph, writer);
        } catch (IOException e) {
            e.printStackTrace();
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
}
