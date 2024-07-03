package de.tum.cit.ase.aspectj.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class WriteFilesUsingFilesClass {

    public static void main(String[] args) {
        String filePath = "fileUsingFilesClass.txt";

        try {
            Files.write(Paths.get(filePath), Arrays.asList("Hello, World!"), StandardOpenOption.CREATE);
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
