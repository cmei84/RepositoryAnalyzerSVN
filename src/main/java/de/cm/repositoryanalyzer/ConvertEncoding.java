package de.cm.repositoryanalyzer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConvertEncoding {
    public static void main(String[] args) {
        String directoryPath = "D:\\workspace.git\\RepositoryAnalyzerSVN\\src"; // Replace with your directory path
        try {
            convertFilesToUTF8(directoryPath);
            System.out.println("Conversion completed successfully.");
        } catch (IOException e) {
            System.err.println("Error during conversion: " + e.getMessage());
        }
    }

    public static void convertFilesToUTF8(String directoryPath) throws IOException {
        Files.walk(Paths.get(directoryPath))
             .filter(path -> path.toString().endsWith(".java"))
             .forEach(path -> {
                 try {
                     // Read file content with ANSI (Windows-1252) encoding
                     String content = new String(Files.readAllBytes(path), "Windows-1252");
                     // Write file content with UTF-8 encoding
                     Files.write(path, content.getBytes(StandardCharsets.UTF_8));
                     System.out.println("Converted: " + path);
                 } catch (IOException e) {
                     System.err.println("Failed to convert " + path + ": " + e.getMessage());
                 }
             });
    }
}
