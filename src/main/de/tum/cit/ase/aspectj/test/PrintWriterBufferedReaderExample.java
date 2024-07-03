package de.tum.cit.ase.aspectj.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PrintWriterBufferedReaderExample {
    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter("printWriterBufferedReader.txt")) {
            writer.println("Hello, PrintWriter!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("printWriterBufferedReader.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

