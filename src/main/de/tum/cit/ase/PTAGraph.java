package de.tum.cit.ase;

import qilin.core.PTA;
import qilin.core.builder.callgraph.OnFlyCallGraph;
import qilin.driver.PTAFactory;
import qilin.driver.PTAPattern;
import qilin.pta.PTAConfig;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.java.bytecode.inputlocation.JrtFileSystemAnalysisInputLocation;
import sootup.java.core.views.JavaView;

import java.util.ArrayList;
import java.util.List;

public class PTAGraph {

    // PTA not working currently on Java 21
    public static void main(String[] args) {
        String classPath = System.getProperty("java.class.path");
        List<AnalysisInputLocation> inputLocations = new ArrayList<>();

        inputLocations.add(new sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation(classPath));
        inputLocations.add(new JrtFileSystemAnalysisInputLocation());

        JavaView view = new JavaView(inputLocations);

         PTAPattern ptaPattern = new PTAPattern("insens");
//        PTAPattern ptaPattern = new PTAPattern("2c");
//        PTAPattern ptaPattern = new PTAPattern("M-1c");
//        PTAPattern ptaPattern = new PTAPattern("D-2c");
//        PTAPattern ptaPattern = new PTAPattern("Z-2c");
//        PTAPattern ptaPattern = new PTAPattern("s-2c");
        PTAConfig.v();
        PTA pta = PTAFactory.createPTA(ptaPattern, view, "de.tum.cit.ase.Student");
        // record how much time it needs
        long startTime = System.currentTimeMillis();
        pta.run();
        long endTime = System.currentTimeMillis();

        OnFlyCallGraph callGraph = pta.getCallGraph();

        System.out.println("Call Graph: ");
        System.out.println(callGraph.getMethodSignatures().size());
        // print the time elapsed
        System.out.println("Time elapsed: " + (endTime - startTime) + "ms");

        System.out.println("something");
    }
}
