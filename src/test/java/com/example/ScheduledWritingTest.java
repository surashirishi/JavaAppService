package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "logging.level.com.example=INFO",
    "scheduled.writing.test=true"
})
public class ScheduledWritingTest {
    
    @Autowired
    private FileService fileService;
    
    @Test
    public void testPeriodicWritingMethod() throws IOException {
        // Clean up any existing test file
        String systemDrive = System.getenv("SystemDrive");
        if (systemDrive == null) {
            systemDrive = System.getProperty("user.home");
        }
        
        Path localDir = Paths.get(systemDrive, "local");
        Path targetFile = localDir.resolve("test.txt");
        
        // Remove the file if it exists
        if (Files.exists(targetFile)) {
            Files.delete(targetFile);
        }
        
        // Call the periodic writing method manually
        fileService.writePeriodicTestData();
        
        // Verify the file was created and contains the expected content
        assertTrue(Files.exists(targetFile), "Test file should be created");
        
        String content = Files.readString(targetFile);
        assertTrue(content.contains("test-writing:"), "Content should contain the expected format");
        assertTrue(content.contains("at 2025-"), "Content should contain the current year");
        
        // Call the method again to test the counter increment
        fileService.writePeriodicTestData();
        
        String updatedContent = Files.readString(targetFile);
        assertTrue(updatedContent.contains("test-writing:"), "Content should contain the expected format after second write");
        
        // Count the number of lines (each write adds a line)
        String[] lines = updatedContent.trim().split("\n");
        assertTrue(lines.length >= 2, "Should have at least 2 lines after 2 writes");
        
        // Verify that each line contains the correct format
        for (String line : lines) {
            assertTrue(line.matches("test-writing: \\d+ at \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"), 
                      "Line should match expected format: " + line);
        }
    }
}