package de.tum.cit.ase.aspectj.println;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class Printer {

    public static void main(String[] args) throws IOException {
//        FileOutputStream fileStream = new FileOutputStream("test.txt");
//
//        PrintStream fileStream = new PrintStream(new FileOutputStream("test.txt"));
//
//        System.setOut(fileStream);
//        JarOutputStream jarStream = new JarOutputStream(new FilterOutputStream(OutputStream.nullOutputStream()));
//        JarFile file = new JarFile("test.jar");
        Files.readAllLines(Path.of("test.txt"));
    }
}
