# **Botanical Gardening**
How often should I water my plant? Where is it native to?

Botanical Gardening is a program that helps people learn about and care for the plants in their day-to-day life! Botanical can find information about almost any plant and displays it on an easy-to-read, colorful page for the conveience of my fellow plant lovers.

## **How it Works**
Botanical relies on an API to search for information on household, wild, and event extinct plants. It is then able to filter through the information, returning only the information users request for their specific needs.

This project uses the [Trefle.io Plant API](https://trefle.io/).  
To use the app, you’ll need your own free API key.

### Steps
1. Go to [https://trefle.io](https://trefle.io) and create a free account.
2. Generate a personal access token.
3. Set it as an environment variable named `TREFLE_API_KEY`.

The API key is only required for plant searches. The project can still be built and launched without one.

## Requirements

- Java 21
- Maven 3.9 or IntelliJ IDEA's bundled Maven

## Build and run

```bash
mvn clean compile
mvn javafx:run
```

### Windows on ARM

JavaFX 21 does not provide native Windows ARM64 artifacts. On a Windows ARM computer, use an x64 Java 21 installation and make sure Maven runs with that JDK. In IntelliJ IDEA, select the x64 Java 21 installation as both the project SDK and the Maven runner JRE.

From PowerShell, select the x64 JDK for the current terminal before running Maven:

```powershell
$env:JAVA_HOME="C:\path\to\x64-jdk-21"
mvn -U clean javafx:run
```

The `-U` option forces Maven to retry dependency downloads if an earlier JavaFX resolution failure was cached.
