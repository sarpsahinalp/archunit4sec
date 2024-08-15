package de.tum.rules.testenv3;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class AnalyzeNativeMethods {

    public static void main(String[] args) {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(location -> location.contains("jrt"))
                .importClasspath();

        for (var clazz : classes) {
            clazz.getMethods().stream()
                    .filter(method -> method.getModifiers().contains(JavaModifier.NATIVE))
                    .forEach(method -> {
                        try {
                            Files.write(Path.of("natives.txt"),(method.getFullName() + "\n").getBytes(), StandardOpenOption.APPEND);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
