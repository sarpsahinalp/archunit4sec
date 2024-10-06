package de.tum.cit.ase;

import com.ibm.wala.classLoader.Language;
import com.ibm.wala.core.util.config.AnalysisScopeReader;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.util.NullProgressMonitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class WalaGraph {

    public static void main(String[] args) throws ClassHierarchyException, CallGraphBuilderCancelException, IOException {
        // Create an AnalysisScope for the Java 21 JRT modules
        AnalysisScope.createJavaAnalysisScope();
        AnalysisScope scope = AnalysisScopeReader.instance.makeJavaBinaryAnalysisScope("/home/sarps/IdeaProjects/archunit4sec/build/classes/java/main/de/tum/cit/ase", new File("/home/sarps/IdeaProjects/archunit4sec/src/main/de/tum/cit/ase/Exclusions.txt"));

        // Build the class hierarchy
        ClassHierarchy cha = ClassHierarchyFactory.make(scope);

        // Create AnalysisOptions for call graph
        AnalysisOptions options = new AnalysisOptions(scope, null);

        // Set entry points (optional): Customize as per your requirement
        Iterable<Entrypoint> entryPoints = Util.makeMainEntrypoints(cha);
        options.setEntrypoints(entryPoints);

        // Create call graph builder (n-CFA, context-sensitive, etc.)
        CallGraphBuilder<InstanceKey> builder = Util.makeZeroCFABuilder(Language.JAVA, options, new AnalysisCacheImpl(), cha);

        // Generate the call graph
        CallGraph callGraph = builder.makeCallGraph(options, null);

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
