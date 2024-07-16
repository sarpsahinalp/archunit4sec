package de.tum.cit.ase.aspectj.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.spi.FileSystemProvider;
import java.nio.file.spi.FileTypeDetector;
import java.util.Scanner;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class HelloWorld {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("path/to/file.txt");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            ByteBuffer buffer = ByteBuffer.wrap("Hello, World!".getBytes());
            fileChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream("path/to/file.txt");
            int content;
            while ((content = fis.read()) != -1) {
                System.out.print((char) content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("path/to/file.txt");
            String content = "Hello, World!";
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

        Scanner scanner = new Scanner(System.in);

        path = Paths.get("path/to/file.txt");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = fileChannel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear();
                bytesRead = fileChannel.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // try to read a file
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("test.txt"));
            String line = reader.readLine();
            System.out.println(line);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            System.out.println("IO Exception");
        }
        RandomAccessFile file = new RandomAccessFile("test.txt", "rw");

        JarInputStream inputStream = new JarInputStream(new FileInputStream("test.jar"));

        JarOutputStream jos = new JarOutputStream(fos);

        FileSystemProvider provider = FileSystemProvider.installedProviders().get(0);

        FileTypeDetector detector = null;
    }
}
