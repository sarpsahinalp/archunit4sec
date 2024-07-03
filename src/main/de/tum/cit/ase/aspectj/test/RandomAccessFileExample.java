package de.tum.cit.ase.aspectj.test;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileExample {
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("randomAccessFile.txt", "rw")) {
            file.writeUTF("Hello, RandomAccessFile!");
            file.seek(0);
            String content = file.readUTF();
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

