package de.tum.cit.ase;

import qilin.CoreConfig;
import qilin.core.PTA;
import qilin.driver.PTAFactory;
import qilin.driver.PTAPattern;
import qilin.pta.PTAConfig;
import sootup.callgraph.CallGraph;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.bytecode.frontend.inputlocation.JrtFileSystemAnalysisInputLocation;
import sootup.java.core.views.JavaView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PTAGraph {

    // PTA not working currently on Java 21
    public static void main(String[] args) {
        String classPath = System.getProperty("java.class.path");
        List<AnalysisInputLocation> inputLocations = new ArrayList<>();

        inputLocations.add(new JavaClassPathAnalysisInputLocation(classPath));
        inputLocations.add(new JrtFileSystemAnalysisInputLocation());

        JavaView view = new JavaView(inputLocations);

        PTAPattern ptaPattern = new PTAPattern("2o");
        PTAConfig.v();
        PTA pta = PTAFactory.createPTA(ptaPattern, view, "de.tum.cit.ase.Student");
        pta.run();
        CallGraph cg = pta.getCallGraph();
    }
}
