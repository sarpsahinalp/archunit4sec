//package de.tum.cit.ase;
//
//
//import sootup.core.inputlocation.AnalysisInputLocation;
//import sootup.core.model.SootMethod;
//import sootup.java.bytecode.frontend.inputlocation.DefaultRuntimeAnalysisInputLocation;
//import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
//import sootup.java.core.views.JavaView;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class CfgGraph {
//
//    public static void main(String[] args) {
//        // Assuming `sootMethod` is obtained from the setup step
//        String classPath = System.getProperty("java.class.path");
//        List<AnalysisInputLocation> inputLocations = new ArrayList<>();
//
//        for (String path : classPath.split(";")) {
//            inputLocations.add(new JavaClassPathAnalysisInputLocation(path));
//        }
//
//        // TODO: For Java 21
////        inputLocations.add(new JrtFileSystemAnalysisInputLocation());
//        inputLocations.add(new DefaultRuntimeAnalysisInputLocation());
//
//        JavaView view = new JavaView(inputLocations);
//
//        SootMethod vulnerableMethod = view.getMethod(view.getIdentifierFactory().getMethodSignature(
//                "java.io.FileInputStream", "<init>", "void", Collections.singletonList("java.io.File"))).orElse(null);
//
//        // Create the CFG subgraph
//        CfgCreator cfgCreator = new CfgCreator();
//        PropertyGraph cfgGraph = cfgCreator.createGraph(vulnerableMethod);
//
//        // Print the DOT representation of the CFG
//        System.out.println(cfgGraph.toDotGraph());
//    }
//}
