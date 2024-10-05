package de.tum.cit.ase;

import java.io.FileNotFoundException;

public class Student {

    public static void main(String[] args) throws FileNotFoundException {
        accessFileSystem();
    }

    static void accessFileSystem() throws FileNotFoundException {
        Student2.main(new String[0]);
    }
}
