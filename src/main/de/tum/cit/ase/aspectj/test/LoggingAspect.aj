//package de.tum.cit.ase.aspectj;
//
//public aspect LoggingAspect {
//
//    pointcut helloPointcut(): execution(* java.io.FileOutputStream.open(..));
//
//    before(): helloPointcut() {
//        System.out.println("About to say hello");
//        throw new SecurityException("File read is not allowed");
//    }
//}
