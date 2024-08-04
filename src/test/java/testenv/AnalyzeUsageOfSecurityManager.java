package testenv;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyzeUsageOfSecurityManager {

    static final Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

    public static void main(String[] args) throws IOException {
        JavaClasses classes = new ClassFileImporter().importClasspath();

        JavaClass filePermission = classes.get("java.lang.SecurityManager");

        filePermission.getAccessesToSelf()
                .stream().filter(access -> access.getTarget().getFullName().equals("java.lang.SecurityManager.checkRead(java.lang.String)"))
                .forEach(javaAccess -> System.out.println(javaAccess.getOrigin().getFullName()));
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
}