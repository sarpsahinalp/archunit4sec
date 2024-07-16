package de.tum.cit.ase.aspectj.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class OpenFileUsingDesktopClass {
    public static void main(String[] args) {
        String filePath = "/home/sarps/IdeaProjects/archunit4sec/fileInputOutputStream.txt";
        File file = new File(filePath);

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            desktop.setOpenFileHandler((f) -> System.out.println("Opening file: " + f));
            try {
                desktop.open(file);
                System.out.println("Opened the file with the default application.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop is not supported on this platform.");
        }
    }
}

