package de.tum.cit.ase.aspectj.println;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Printer {

    public static void main(String[] args) throws FileNotFoundException {
//        FileOutputStream fileStream = new FileOutputStream("test.txt");
        System.out.println("Hello, AspectJ!");
    }
}
