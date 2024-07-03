package de.tum.cit.ase.aspectj.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CreateFileUsingFileChannel {

    public static void main(String[] args) {
        String filePath = "fileUsingFileChannel.txt";
        Path path = Path.of(filePath);

        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            ByteBuffer buffer = ByteBuffer.allocate(0);
            fileChannel.write(buffer);
            System.out.println("File created using FileChannel.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
