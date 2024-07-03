package de.tum.cit.ase.aspectj.test;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.logging.FileHandler;

public class StudentSubmission {

    public static void main(String[] args) throws IOException {
        new PrintStream("test.txt").write(5);
        System.setOut(new PrintStream("output.txt"));
        FileSystem fs = FileSystems.newFileSystem(Path.of("test.zip"), StudentSubmission.class.getClassLoader());
        FileSystemProvider provider = FileSystemProvider.installedProviders().get(0);
        provider.delete(Path.of("test.txt"));
        System.out.println("Hello World");
        Process process = Runtime.getRuntime().exec("touch runtimeFile.txt");
        FileHandler fileHandler = new FileHandler();
        new PrintWriter("printWriter.txt").write("Hello World");
    }
}
