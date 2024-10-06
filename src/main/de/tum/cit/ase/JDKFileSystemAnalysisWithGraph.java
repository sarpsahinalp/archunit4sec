package de.tum.cit.ase;

import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.RapidTypeAnalysisAlgorithm;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.bytecode.inputlocation.JrtFileSystemAnalysisInputLocation;
import sootup.java.core.JavaSootMethod;
import sootup.java.core.views.JavaView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JDKFileSystemAnalysisWithGraph {

    public static void main(String[] args) {
        String classPath = System.getProperty("java.class.path");
        List<AnalysisInputLocation> inputLocations = new ArrayList<>();

        inputLocations.add(new JavaClassPathAnalysisInputLocation(classPath));
        inputLocations.add(new JrtFileSystemAnalysisInputLocation());

        JavaView view = new JavaView(inputLocations);

        CallGraphAlgorithm rta = new RapidTypeAnalysisAlgorithm(view);

        // initialize the callgraph with the fileinput stream
        CallGraph cg = rta.initialize(Collections.singletonList(view.getIdentifierFactory().getMethodSignature("de.tum.cit.ase.Student", "accessFileSystem", "void", List.of())));
    }
}
