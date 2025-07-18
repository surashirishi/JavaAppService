package com.example;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;

@Service
public class FileService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    
    /**
     * Copies test.txt from %HOME%\site\wwwroot to %SystemDrive%\local directory
     * and reads the content to log it.
     */
    public void copyFileAndReadContent() {
        try {
            // Get HOME environment variable
            String homeDir = System.getenv("HOME");
            if (homeDir == null) {
                // Fallback for development - try user.home
                homeDir = System.getProperty("user.home");
                logger.warn("HOME environment variable not found. Using user.home: {}", homeDir);
            }
            
            // Get the system drive (typically C: on Windows)
            String systemDrive = System.getenv("SystemDrive");
            if (systemDrive == null) {
                // Fallback for non-Windows systems during development
                systemDrive = System.getProperty("user.home");
                logger.warn("SystemDrive environment variable not found. Using user.home: {}", systemDrive);
            }
            
            // Define source and target paths
            Path sourcePath = Paths.get(homeDir, "site", "wwwroot", "test.txt");
            Path targetDir = Paths.get(systemDrive, "local");
            Path targetPath = targetDir.resolve("test.txt");
            
            logger.info("Source file: {}", sourcePath);
            logger.info("Target directory: {}", targetDir);
            logger.info("Target file: {}", targetPath);
            
            // Ensure the source file exists (create it if needed)
            ensureSourceFileExists(sourcePath);
            
            // Create the target directory if it doesn't exist
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
                logger.info("Created target directory: {}", targetDir);
            }
            
            // Copy the file from source to target
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Successfully copied file from {} to {}", sourcePath, targetPath);
            
            // Read the content back and log it
            if (Files.exists(targetPath)) {
                String readContent = Files.readString(targetPath);
                logger.info("Content read from target file {}: '{}'", targetPath, readContent);
            } else {
                logger.error("Target file not found after copying: {}", targetPath);
            }
            
        } catch (IOException e) {
            logger.error("Error during file copy operation: {}", e.getMessage(), e);
            throw new RuntimeException("File copy operation failed", e);
        }
    }
    
    /**
     * Ensures the source file exists in %HOME%\site\wwwroot\test.txt
     * If it doesn't exist, creates it from the bundled resource file.
     */
    private void ensureSourceFileExists(Path sourcePath) throws IOException {
        if (!Files.exists(sourcePath)) {
            // Create the directory structure if needed
            Path sourceDir = sourcePath.getParent();
            if (!Files.exists(sourceDir)) {
                Files.createDirectories(sourceDir);
                logger.info("Created source directory: {}", sourceDir);
            }
            
            // Copy the test.txt from resources to the source location
            try (InputStream resourceStream = getClass().getResourceAsStream("/test.txt")) {
                if (resourceStream != null) {
                    Files.copy(resourceStream, sourcePath, StandardCopyOption.REPLACE_EXISTING);
                    logger.info("Deployed test.txt from resources to: {}", sourcePath);
                } else {
                    // Fallback: create a default test.txt with "test" content
                    Files.write(sourcePath, "test".getBytes(), StandardOpenOption.CREATE);
                    logger.info("Created default test.txt at: {}", sourcePath);
                }
            }
        } else {
            logger.info("Source file already exists: {}", sourcePath);
        }
    }
    

}