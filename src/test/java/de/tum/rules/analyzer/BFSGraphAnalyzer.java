//package de.tum.rules.analyzer;
//
//import com.tngtech.archunit.core.domain.AccessTarget;
//import com.tngtech.archunit.core.domain.JavaClass;
//import com.tngtech.archunit.core.domain.JavaAccess;
//import com.tngtech.archunit.core.domain.JavaClasses;
//import com.tngtech.archunit.core.importer.ClassFileImporter;
//import org.jgrapht.Graph;
//import org.jgrapht.graph.DefaultEdge;
//import org.jgrapht.graph.SimpleDirectedGraph;
//import org.jgrapht.nio.Attribute;
//import org.jgrapht.nio.DefaultAttribute;
//import org.jgrapht.nio.dot.DOTExporter;
//
//import java.io.FilePermission;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Writer;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class BFSGraphAnalyzer {
//
//    private final Graph<String, DefaultEdge> dependencyGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
//
//    public static void main(String[] args) throws Exception {
//        BFSGraphAnalyzer analyzer = new BFSGraphAnalyzer();
//        analyzer.runAnalysis();
//    }
//
//    public void runAnalysis() throws Exception {
//        // Import classes from the classpath
//        JavaClasses classes = new ClassFileImporter().importClasspath();
//
//        // Get the JavaClass for FilePermission
//        JavaClass filePermission = classes.get(FilePermission.class);
//
//        // Initialize a queue with accesses to self excluding self-referential ones
//        Queue<JavaAccess<?>> frontier = filePermission.getAccessesToSelf().stream()
//                .filter(access -> !access.getOriginOwner().getFullName().equals(FilePermission.class.getName()))
//                .distinct()
//                .collect(Collectors.toCollection(LinkedList::new));
//
//        Set<String> visitedMethods = new HashSet<>();
//        Path path = Files.createFile(Path.of("test.txt"));
//
//        // Add the initial class to the graph
//        dependencyGraph.addVertex(filePermission.getFullName());
//
//        // Perform BFS
//        while (!frontier.isEmpty()) {
//            System.out.println("Queue size: " + frontier.size());
//
//            JavaAccess<? extends AccessTarget> access = frontier.poll();
//
//            // Skip accesses from specific packages
//            assert access != null;
//            if (access.getOrigin().getFullName().startsWith("de.tum")) {
//                continue;
//            }
//
//            // Skip already visited methods
//            if (visitedMethods.contains(access.getOrigin().getFullName())) {
//                continue;
//            }
//
//            // Mark method as visited
//            visitedMethods.add(access.getOrigin().getFullName());
//
//            String originName = access.getOrigin().getFullName() + "\n";
//
//            // Write to file if the origin method is public and its owner is public
//            if (isOriginOwnerPublicAndOriginPublic(access)) {
//                Files.write(path, originName.getBytes(), java.nio.file.StandardOpenOption.APPEND);
//            }
//
//            // Add the origin class and its dependencies to the graph
//            String originClassName = access.getOrigin().getFullName();
//            if (!dependencyGraph.containsVertex(originClassName)) {
//                dependencyGraph.addVertex(originClassName);
//            }
//
//            for (JavaAccess<?> targetAccess : access.getOrigin().getAccessesToSelf()) {
//                String targetClassName = targetAccess.getTarget().getFullName();
//                if (!dependencyGraph.containsVertex(targetClassName)) {
//                    dependencyGraph.addVertex(targetClassName);
//                }
//                dependencyGraph.addEdge(originClassName, targetClassName);
//            }
//
//            // Add all accesses to self for the current origin to the queue
//            frontier.addAll(access.getOrigin().getAccessesToSelf());
//        }
//
//        // Optionally, print the graph or export it
//        printGraph();
//    }
//
//    private boolean isOriginOwnerPublicAndOriginPublic(JavaAccess<? extends AccessTarget> access) {
//        // Implement your logic here. This is just a placeholder.
//        return true; // Adjust this condition based on your requirements
//    }
//
//    private void printGraph() {
//        exportToDotFile("callgraph.dot", dependencyGraph);
//    }
//
//    public static void exportToDotFile(String filePath, Graph<String, DefaultEdge> graph) {
//        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();
//        exporter.setVertexAttributeProvider((v) -> {
//            Map<String, Attribute> map = new LinkedHashMap<>();
//            map.put("label", DefaultAttribute.createAttribute(v));
//            return map;
//        });
//
//        try (Writer writer = new FileWriter(filePath)) {
//            exporter.exportGraph(graph, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
