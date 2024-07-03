package de.tum.cit.ase.aspectj.test;

import java.io.File;
import java.io.IOException;

public class CreateFileUsingFileClass {

    public static void main(String[] args) {
        String filePath = "fileUsingFileClass.txt";
        File file = new File(filePath);

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
