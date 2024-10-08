package de.tum.cit.ase.wala;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static de.tum.cit.ase.wala.WalaGraphZeroCFA.writeCallGraphToDot;

public class WalaNCFA {

    public static void main(String[] args) throws ClassHierarchyException, CallGraphBuilderCancelException, IOException {
        // get entire classpath of the project
        String classpath = System.getProperty("java.class.path");
        AnalysisScope scope = AnalysisScopeReader.instance.makeJavaBinaryAnalysisScope(classpath, new File("C:\\Users\\sarps\\IdeaProjects\\archunit4sec\\src\\main\\de\\tum\\cit\\ase\\Exclusions.txt"));

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
        CallGraphBuilder<InstanceKey> builder = Util.makeNCFABuilder(1, options, new AnalysisCacheImpl(), cha);

        System.out.println("Building call graph...");
        long startTime = System.currentTimeMillis();
        // Generate the call graph
        CallGraph callGraph = builder.makeCallGraph(options, null);
        long endTime = System.currentTimeMillis();

        System.out.println("Call graph built in " + (endTime - startTime) + "ms");

        // Write the call graph to a DOT file
        String dotFilePath = "callgraph-n-cfa.dot";
        writeCallGraphToDot(callGraph, dotFilePath);

        MethodReference filePermission = MethodReference.findOrCreate(ClassLoaderReference.Application, "Ljava/io/FilePermission", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");


    }
}
