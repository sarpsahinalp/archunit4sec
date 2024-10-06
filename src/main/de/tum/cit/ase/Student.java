package de.tum.cit.ase;

import java.io.*;
import java.nio.file.Paths;

public class Student {

    static void access() throws FileNotFoundException {
        new FileOutputStream("test.txt");
    }

    static void accessFileSystem() throws IOException {
        String path1 = "test.";
        String path2 = "txt";
        Paths.get(path1 + path2).getFileSystem().provider().createDirectory(Paths.get("test"));
    }
}
