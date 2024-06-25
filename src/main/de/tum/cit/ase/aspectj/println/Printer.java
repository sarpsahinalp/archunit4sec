package de.tum.cit.ase.aspectj.println;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Printer {

    public static void main(String[] args) throws FileNotFoundException {

        PrintStream fileStream = new PrintStream(new FileOutputStream("test.txt"));

        System.setOut(fileStream);
        System.out.println("Hello, World!");
    }
}
