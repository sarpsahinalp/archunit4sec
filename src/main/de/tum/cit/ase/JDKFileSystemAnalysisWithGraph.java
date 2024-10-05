package de.tum.cit.ase;

import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.RapidTypeAnalysisAlgorithm;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.bytecode.frontend.inputlocation.JrtFileSystemAnalysisInputLocation;
import sootup.java.core.views.JavaView;

import java.util.ArrayList;
import java.util.List;

public class JDKFileSystemAnalysisWithGraph {

    public static void main(String[] args) {
        String classPath = System.getProperty("java.class.path");
        List<AnalysisInputLocation> inputLocations = new ArrayList<>();

        inputLocations.add(new JavaClassPathAnalysisInputLocation(classPath));
        inputLocations.add(new JrtFileSystemAnalysisInputLocation());

        JavaView view = new JavaView(inputLocations);

        CallGraphAlgorithm rta = new RapidTypeAnalysisAlgorithm(view);

        // initialize the callgraph with the fileinput stream
        CallGraph cg = rta.initialize(List.of(view.getIdentifierFactory().getMethodSignature("java.io.FileInputStream", "<init>", "void", List.of("java.lang.String"))));
    }
}
