package student;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileAccess {

    public static void main(String[] args) throws Exception {
        Files.readAllLines(Path.of("/home/sarps/IdeaProjects/archunit4sec/src/main/java/student/hello.txt"));
    }
}
