# Bore Web UI

A web interface for the Bore CLI tool built with Spring Boot.

## Prerequisites

- Java 17 or higher
- Gradle
- Bore CLI installed and accessible from command line

## Setup

1. Clone the repository
```bash
git clone <repository-url>
cd bore-web-ui
```

2. Build the project
```bash
./gradlew build
```

3. Run the application
```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`

## Features

- Web interface for Bore CLI
- Real-time output streaming
- Process management
- Clean process termination
- Status indicators

## Usage

1. Open the web interface in your browser
2. Enter the port number you want to expose
3. Click "Start Bore" to begin tunneling
4. The output will show the public URL assigned by bore.pub
5. Use "Stop Bore" to end the tunnel

## Configuration

Configuration options are available in `src/main/resources/application.properties`:

- `server.port`: Web server port (default: 8080)
- `bore.process.timeout`: Maximum time to wait for bore process (default: 30000ms)

## Development

To run the application in development mode with hot reload:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## Testing

Run the tests with:

```bash
./gradlew test
```