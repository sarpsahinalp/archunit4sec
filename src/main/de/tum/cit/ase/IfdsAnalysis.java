//package de.tum.cit.ase;
//
//import heros.IFDSTabulationProblem;
//import heros.InterproceduralCFG;
//import sootup.analysis.interprocedural.icfg.JimpleBasedInterproceduralCFG;
//import sootup.analysis.interprocedural.ifds.JimpleIFDSSolver;
//import sootup.core.jimple.common.stmt.Stmt;
//import sootup.core.model.SootMethod;
//
//public class IfdsAnalysis {
//
//    public static void main(String[] args) {
//        JimpleBasedInterproceduralCFG icfg = new JimpleBasedInterproceduralCFG(view, entryMethod);
//
//         IFDSTabulationProblem problem =
//                 new IF(icfg, entryMethod);
//
//         JimpleIFDSSolver<?, InterproceduralCFG<Stmt, SootMethod>> solver =
//                 new JimpleIFDSSolver(problem);
//
//         solver.solve();
//    }
//}
