package de.tum.cit.ase.aspectj.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFilesUsingBufferedReader {
    public static void main(String[] args) {
        String filePath = "fileUsingFileClass.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

