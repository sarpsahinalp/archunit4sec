package de.tum.cit.ase;

import java.awt.*;
import java.io.*;
import java.nio.file.Paths;

public class Student {

    static void access() throws FileNotFoundException {
        new FileOutputStream("test.txt");
    }

    static void accessFileSystem() throws IOException {
        Desktop.getDesktop().open(new File("test.txt"));
        String path1 = "test.";
        String path2 = "txt";
        Paths.get(path1 + path2).getFileSystem().provider().createDirectory(Paths.get("test"));
    }
}
