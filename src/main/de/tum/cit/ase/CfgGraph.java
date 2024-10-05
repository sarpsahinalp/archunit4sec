package de.tum.cit.ase;

import sootup.core.model.SootMethod;

public class CfgGraph {

    public static void main(String[] args) {
        // Assuming `sootMethod` is obtained from the setup step
        SootMethod vulnerableMethod = getVulnerableMethod();

        // Create the CFG subgraph
        CfgCreator cfgCreator = new CfgCreator();
        PropertyGraph cfgGraph = cfgCreator.createGraph(vulnerableMethod);

        // Print the DOT representation of the CFG
        System.out.println(cfgGraph.toDotGraph());
    }
}
