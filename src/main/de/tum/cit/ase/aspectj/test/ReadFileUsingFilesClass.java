package de.tum.cit.ase.aspectj.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadFileUsingFilesClass {

    public static void main(String[] args) {
        String filePath = "/home/sarps/IdeaProjects/archunit4sec/fileInputOutputStream.txt";

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            Thread.sleep(100000);
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
