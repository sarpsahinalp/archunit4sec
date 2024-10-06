package de.tum.cit.ase.wala;

import com.ibm.wala.classLoader.Language;
import com.ibm.wala.core.util.config.AnalysisScopeReader;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WalaGraphZeroCFA {

    public static void main(String[] args) throws ClassHierarchyException, CallGraphBuilderCancelException, IOException {
        // Create an AnalysisScope for the Java 21 JRT modules
        AnalysisScope.createJavaAnalysisScope();
        AnalysisScope scope = AnalysisScopeReader.instance.makeJavaBinaryAnalysisScope("/home/sarps/IdeaProjects/archunit4sec/build/classes/java/main/de/tum/cit/ase", new File("/home/sarps/IdeaProjects/archunit4sec/src/main/de/tum/cit/ase/Exclusions.txt"));

        // Build the class hierarchy
        ClassHierarchy cha = ClassHierarchyFactory.make(scope);

        List<Entrypoint> customEntryPoints = new ArrayList<>();

        MethodReference methodToAnalyze = MethodReference.findOrCreate(
                ClassLoaderReference.Application,
                "Lde/tum/cit/ase/Student",  // Class name (with slashes)
                "accessFileSystem",         // Method name
                "()V"                       // Method signature: void return type, no parameters
        );

        customEntryPoints.add(new DefaultEntrypoint(methodToAnalyze, cha));

        // Create AnalysisOptions for call graph
        AnalysisOptions options = new AnalysisOptions(scope, customEntryPoints);


        // Create call graph builder (n-CFA, context-sensitive, etc.)
        CallGraphBuilder<InstanceKey> builder = Util.makeZeroCFABuilder(Language.JAVA, options, new AnalysisCacheImpl(), cha);

        System.out.println("Building call graph...");
        long startTime = System.currentTimeMillis();
        // Generate the call graph
        CallGraph callGraph = builder.makeCallGraph(options, null);
        long endTime = System.currentTimeMillis();

        System.out.println("Call graph built in " + (endTime - startTime) + "ms");

        // Write the call graph to a DOT file
        String dotFilePath = "callgraph.dot";
        writeCallGraphToDot(callGraph, dotFilePath);
    }

    // Write the CallGraph to a DOT file
    public static void writeCallGraphToDot(CallGraph callGraph, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("digraph G {\n");

            // Iterate through the nodes in the call graph
            for (CGNode node : callGraph) {
                // Write the node representation
                writer.write("    \"" + node.getMethod().getSignature() + "\";\n");

                // Iterate through the edges (call sites) from the current node
                for (Iterator<CGNode> it = callGraph.getSuccNodes(node); it.hasNext(); ) {
                    CGNode successor = it.next();
                    // Write the edge representation
                    writer.write("    \"" + node.getMethod().getSignature() + "\" -> \"" + successor.getMethod().getSignature() + "\";\n");
                }
            }
            writer.write("}\n");
        }
    }
}
