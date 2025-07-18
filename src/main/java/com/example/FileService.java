package com.example;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private int writeCounter = 0;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Copies a test.txt file with content "test" to %SystemDrive%\local directory
     * and reads back the content to log it.
     */
    public void copyFileAndReadContent() {
        try {
            // Get the system drive (typically C: on Windows)
            String systemDrive = System.getenv("SystemDrive");
            if (systemDrive == null) {
                // Fallback for non-Windows systems during development
                systemDrive = System.getProperty("user.home");
                logger.warn("SystemDrive environment variable not found. Using user.home: {}", systemDrive);
            }
            
            // Create the local directory path
            Path localDir = Paths.get(systemDrive, "local");
            Path targetFile = localDir.resolve("test.txt");
            
            logger.info("Target directory: {}", localDir);
            logger.info("Target file: {}", targetFile);
            
            // Create the directory if it doesn't exist
            if (!Files.exists(localDir)) {
                Files.createDirectories(localDir);
                logger.info("Created directory: {}", localDir);
            }
            
            // Write "test" content to the file
            String content = "test";
            Files.write(targetFile, content.getBytes(), StandardOpenOption.CREATE);
            logger.info("Successfully wrote content '{}' to file: {}", content, targetFile);
            
            // Read the content back and log it
            if (Files.exists(targetFile)) {
                String readContent = Files.readString(targetFile);
                logger.info("Content read from {}: '{}'", targetFile, readContent);
            } else {
                logger.error("File not found after writing: {}", targetFile);
            }
            
        } catch (IOException e) {
            logger.error("Error during file operations: {}", e.getMessage(), e);
            throw new RuntimeException("File operation failed", e);
        }
    }
    
    /**
     * Scheduled method that writes to test.txt every 10 minutes.
     * Writes "test-writing: {n} at {書き込み時間}" where n is the write count.
     */
    @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds (10 * 60 * 1000 = 600,000)
    public void writePeriodicTestData() {
        try {
            writeCounter++;
            String currentTime = LocalDateTime.now().format(timeFormatter);
            String content = String.format("test-writing: %d at %s", writeCounter, currentTime);
            
            // Get the system drive or user home directory
            String systemDrive = System.getenv("SystemDrive");
            if (systemDrive == null) {
                systemDrive = System.getProperty("user.home");
            }
            
            // Create the local directory path
            Path localDir = Paths.get(systemDrive, "local");
            Path targetFile = localDir.resolve("test.txt");
            
            // Create the directory if it doesn't exist
            if (!Files.exists(localDir)) {
                Files.createDirectories(localDir);
            }
            
            // Append the content to the file
            Files.write(targetFile, (content + System.lineSeparator()).getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            
            logger.info("Periodic write #{}: '{}' to file: {}", writeCounter, content, targetFile);
            
        } catch (IOException e) {
            logger.error("Error during periodic file writing: {}", e.getMessage(), e);
        }
    }
}