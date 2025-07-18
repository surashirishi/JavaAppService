# JavaAppService

A simple Java application for Azure App Service that copies a file to a local directory and outputs its contents to logs.

## Features

- **File Copy Operation**: Copies "test.txt" file with content "test" to `%SystemDrive%\local` directory
- **Content Logging**: Reads and outputs the file contents to application logs
- **REST API**: Exposes HTTP endpoints for triggering file operations
- **App Service Ready**: Configured for Windows OS App Service deployment

## Requirements

- Java 17 or higher
- Maven 3.6+
- Windows OS (for production App Service deployment)

## Endpoints

- `GET /` - Health check endpoint
- `POST /copy-file` - Triggers file copy and read operations

## Usage

### Local Development

1. **Build the application**:
   ```bash
   mvn clean package
   ```

2. **Run the application**:
   ```bash
   java -jar target/javaappservice-1.0-SNAPSHOT.jar
   ```

3. **Test the endpoints**:
   ```bash
   # Health check
   curl http://localhost:8080/
   
   # Execute file operations
   curl -X POST http://localhost:8080/copy-file
   ```

### App Service Deployment

1. **Build the application**:
   ```bash
   mvn clean package
   ```

2. **Deploy to App Service**:
   - Upload the JAR file: `target/javaappservice-1.0-SNAPSHOT.jar`
   - Include the `web.config` file from `src/main/resources/`
   - Set Java runtime to Java 17
   - Configure startup command: `java -jar javaappservice-1.0-SNAPSHOT.jar`

## File Operations

The application performs the following operations when `/copy-file` is called:

1. **Directory Creation**: Creates `%SystemDrive%\local` directory if it doesn't exist
2. **File Writing**: Writes "test" content to `%SystemDrive%\local\test.txt`
3. **File Reading**: Reads the file content back and logs it
4. **Logging**: All operations are logged with INFO level

## Logging

The application logs all file operations with detailed information:
- Directory creation status
- File write operations
- File read operations
- Error handling

## Configuration

- **Port**: 8080 (default, can be overridden with `server.port` property)
- **Logging**: INFO level for application logs
- **Environment**: Automatically detects Windows `%SystemDrive%` environment variable

## App Service Configuration

The application includes:
- `web.config` for Windows App Service
- `application.properties` for Spring Boot configuration
- Maven Spring Boot plugin for creating executable JAR

## Testing

Run the test suite:
```bash
mvn test
```

The tests verify:
- Application startup
- File operations functionality
- Error handling