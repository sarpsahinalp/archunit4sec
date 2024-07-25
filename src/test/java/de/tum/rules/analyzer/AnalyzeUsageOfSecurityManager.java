//package de.tum.rules.analyzer;
//
//import com.tngtech.archunit.base.DescribedPredicate;
//import com.tngtech.archunit.core.domain.JavaAccess;
//import com.tngtech.archunit.core.importer.ClassFileImporter;
//import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
//import org.jgrapht.Graph;
//import org.jgrapht.graph.DefaultDirectedGraph;
//import org.jgrapht.graph.DefaultEdge;
//import org.jgrapht.nio.Attribute;
//import org.jgrapht.nio.DefaultAttribute;
//import org.jgrapht.nio.dot.DOTExporter;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Writer;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class AnalyzeUsageOfSecurityManager {
//
//    static final Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
//
//    public static void main(String[] args) throws IOException {
//        /**
//         * Checks for classes accessing AccessController.checkPermission() method
//         */
////         ArchRuleDefinition.noClasses()
////                 .that(new DescribedPredicate<JavaClass>("is AccessController") {
////                     @Override
////                     public boolean test(JavaClass javaClass) {
////                         return javaClass.getFullName().equals("java.security.AccessController");
////                     }
////                 })
////                .should(new ArchCondition<JavaClass>("not use SecurityManager") {
////                    @Override
////                    public void check(JavaClass item, ConditionEvents events) {
////                        item.getAccessesToSelf()
////                                .stream()
////                                .filter(access -> access.getTarget().getFullName().startsWith("java.security.AccessController.checkPermission"))
////                                .filter(access -> !access.getOrigin().getFullName().equals("java.security.AccessController"))
////                                .forEach(access -> events.add(SimpleConditionEvent.satisfied(item, access.getOrigin().getFullName())));
////                    }
////                })
////                .check(new ClassFileImporter().importPackages(".."));
//        /**
//         * Checks for recursion access to checkPermission() method
//         */
//
////        ArchRuleDefinition.noClasses()
////                .that(new DescribedPredicate<JavaClass>("not our package") {
////                    @Override
////                    public boolean test(JavaClass javaClass) {
////                        return !javaClass.getFullName().startsWith("de.tum.cit");
////                    }
////                })
////                .should(new TransitivelyAccessesMethodsCondition(new DescribedPredicate<JavaAccess<?>>("accesses checkPermission") {
////                    @Override
////                    public boolean test(JavaAccess<?> input) {
////                        return input.getTarget().getFullName().equals("java.io.FilePermission.<init>(java.lang.String, java.lang.String)");
////                    }
////                }))
////                .check(new ClassFileImporter().importPackages(".."));
//
//
//        /**
//         * Analyze call graph
//         */
//
//        ArchRuleDefinition.noClasses()
//                .that()
//                .resideInAPackage("de.tum.cit.ase.aspectj.test")
//
//                .should(new TransitivelyAccessesMethodsCondition(new DescribedPredicate<JavaAccess<?>>("accesses checkPermission") {
//                    @Override
//                    public boolean test(JavaAccess<?> input) {
//                        return input.getTarget().getFullName().equals("java.io.FilePermission.<init>(java.lang.String, java.lang.String)");
//                    }
//                }))
//                .check(new ClassFileImporter().importPackages(".."));
//
//        exportToDotFile("callgraph.dot", graph);
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