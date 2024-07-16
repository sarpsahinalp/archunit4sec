package de.tum.cit.ase.aspectj.test;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipImageReaderExample {
    public static void main(String[] args) {
        String zipFileName = "images.zip";
        String imageEntryName = "image.png";

        try (ZipFile zipFile = new ZipFile(zipFileName)) {
            ZipEntry zipEntry = zipFile.getEntry(imageEntryName);

            if (zipEntry != null) {
                try (InputStream is = zipFile.getInputStream(zipEntry);
                     ImageInputStream iis = new MemoryCacheImageInputStream(is)) {

                    BufferedImage image = ImageIO.read(iis);
                    if (image != null) {
                        // Display image information
                        System.out.println("Image read from ZIP:");
                        System.out.println("Width: " + image.getWidth());
                        System.out.println("Height: " + image.getHeight());
                    } else {
                        System.out.println("No image found in the ZIP entry.");
                    }
                }
            } else {
                System.out.println("Image entry not found in ZIP file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
