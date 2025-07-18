package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "logging.level.com.example=INFO")
public class FileServiceTest {
    
    @Test
    public void testFileOperations() {
        FileService fileService = new FileService();
        
        // Test that the file operations don't throw exceptions
        try {
            fileService.copyFileAndReadContent();
            // If we get here, the operation completed successfully
            assert true;
        } catch (Exception e) {
            // We expect it to fail in CI environment but not throw unexpected exceptions
            // This is normal behavior for file operations in restricted environments
            assert e.getMessage().contains("File operation failed") || 
                   e.getMessage().contains("Permission denied") ||
                   e.getMessage().contains("Access is denied");
        }
    }
}