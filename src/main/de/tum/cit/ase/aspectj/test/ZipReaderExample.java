package de.tum.cit.ase.aspectj.test;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.spi.AudioFileWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipReaderExample {
    public static void main(String[] args) {
        String zipFileName = "example.zip";
        try (ZipFile zipFile = new ZipFile(zipFileName)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                System.out.println("Entry: " + entry.getName());

                try (InputStream is = zipFile.getInputStream(entry)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        System.out.write(buffer, 0, bytesRead);
                    }
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        AudioFileWriter writer = new AudioFileWriter() {
            @Override
            public AudioFileFormat.Type[] getAudioFileTypes() {
                return new AudioFileFormat.Type[0];
            }

            @Override
            public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream stream) {
                return new AudioFileFormat.Type[0];
            }

            @Override
            public int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, OutputStream outputStream) throws IOException {
                return 0;
            }

            @Override
            public int write(AudioInputStream stream, AudioFileFormat.Type fileType, File out) {
                return 0;
            }
        };
    }
}
