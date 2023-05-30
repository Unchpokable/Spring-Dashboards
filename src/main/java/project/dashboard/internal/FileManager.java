package project.dashboard.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
public class FileManager {
    public static byte[] loadFileByteArray(String path) throws IOException {
        return Files.readAllBytes(Path.of(path));
    }

    public static void saveFileBytes(byte[] content, String path) throws IOException {
        Files.write(Path.of(path), content);
    }
}
