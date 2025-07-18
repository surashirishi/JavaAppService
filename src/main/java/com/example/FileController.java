package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class FileController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    @Autowired
    private FileService fileService;
    
    @GetMapping("/")
    public String home() {
        return "Java App Service is running! Use POST /copy-file to perform file operations.";
    }
    
    @PostMapping("/copy-file")
    public String copyFile() {
        try {
            logger.info("File copy operation requested");
            fileService.copyFileAndReadContent();
            return "File operation completed successfully. Check logs for details.";
        } catch (Exception e) {
            logger.error("File operation failed: {}", e.getMessage());
            return "File operation failed: " + e.getMessage();
        }
    }
}