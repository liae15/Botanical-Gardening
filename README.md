# Botanical Gardening

Ever wondered how to care for a plant or where it comes from? Botanical Gardening is a small JavaFX app that makes it easy to look up plant information and keep track of plants you own or want.

This project began as a school assignment and is still a work in progress. It uses the [Trefle Plant API](https://trefle.io/) for plant data.

## What it does

- Searches for plants by common name
- Displays the scientific name, discovery year, and an image when available
- Keeps a list of plants you own
- Keeps a wish list of plants you want

Lists are stored in memory for the current session and reset when the app closes.

## Requirements

- Java 21
- Maven 3.9 or IntelliJ IDEA's bundled Maven
- A Trefle API token for plant searches

You can build and launch the app without an API token, but the search feature will remain unavailable.

## Set up the API token

Set your Trefle token as an environment variable named `TREFLE_API_KEY` before launching the app.

PowerShell:

```powershell
$env:TREFLE_API_KEY="your-token"
```

macOS or Linux:

```bash
export TREFLE_API_KEY="your-token"
```

## Build and run

```bash
mvn clean compile
mvn javafx:run
```

## Windows on ARM

JavaFX 21 does not provide native Windows ARM64 artifacts. If you are using a Windows ARM computer, install an x64 version of Java 21 and make sure Maven uses it.

In IntelliJ IDEA, choose the x64 Java 21 installation for both the project SDK and the Maven runner JRE. You can also select it for the current PowerShell session:

```powershell
$env:JAVA_HOME="C:\path\to\x64-jdk-21"
mvn -U clean javafx:run
```

The `-U` option tells Maven to retry dependency downloads if an earlier JavaFX download failed.
