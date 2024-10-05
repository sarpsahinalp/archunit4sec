package de.tum.cit.ase;

import qilin.CoreConfig;
import qilin.core.PTA;
import qilin.driver.PTAFactory;
import qilin.driver.PTAPattern;
import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.RapidTypeAnalysisAlgorithm;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.bytecode.inputlocation.JrtFileSystemAnalysisInputLocation;
import sootup.java.core.language.JavaLanguage;
import sootup.java.core.views.JavaView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDKFileSystemAnalysisWithGraph {

    public static void main(String[] args) {
        String classPath = System.getProperty("java.class.path");
        List<AnalysisInputLocation> inputLocations = new ArrayList<>();

        for (String path : classPath.split(";")) {
            inputLocations.add(new JavaClassPathAnalysisInputLocation(path));
        }

        inputLocations.add(new JrtFileSystemAnalysisInputLocation());

        JavaView view = new JavaView(inputLocations);

//        String MAINCLASS = "de.tum.cit.ase.Student"; // just an example
//        PTAPattern ptaPattern = new PTAPattern("insens"); // "2o"=>2OBJ, "1c"=>1CFA, etc.
//        PTA pta = PTAFactory.createPTA(ptaPattern, view, MAINCLASS);
//        pta.run();
//        CallGraph cg = pta.getCallGraph();

        CallGraphAlgorithm rta = new RapidTypeAnalysisAlgorithm(view);

        CallGraph cg = rta.initialize(Collections.singletonList(view.getIdentifierFactory().getMethodSignature(
                "de.tum.cit.ase.Student", "accessFileSystem", "void", List.of())));

        cg.callsFrom(view.getIdentifierFactory().getMethodSignature(
                "java.io.FileInputStream", "<init>", "void", List.of("String")
        )).forEach(System.out::println);
    }
}
