# MediTrack Clinic Appointment Management System - Setup Instructions

## Prerequisites

- Java Development Kit (JDK) version 8 or higher
- Git for version control
- An IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extensions)

## Java Installation and Configuration

### Step 1: Download and Install JDK

1. Visit the official Oracle JDK download page or OpenJDK
2. Download the appropriate JDK version for your operating system
3. Run the installer and follow the installation wizard

### Step 2: Set Environment Variables

#### Windows:
1. Open System Properties → Advanced → Environment Variables
2. Add new system variable `JAVA_HOME` pointing to your JDK installation directory
3. Add `%JAVA_HOME%\bin` to your PATH variable

#### macOS/Linux:
```bash
# Add to ~/.bashrc or ~/.zshrc
export JAVA_HOME=/path/to/your/jdk
export PATH=$JAVA_HOME/bin:$PATH

# Reload the configuration
source ~/.bashrc  # or source ~/.zshrc
```

### Step 3: Verify Installation

Open terminal/command prompt and run:
```bash
java -version
javac -version
```

Expected output should show Java version information.

## Project Setup

### Step 1: Clone the Repository
```bash
git clone https://github.com/your-username/MediTrack-Clinic-Appointment-Management-System.git
cd MediTrack-Clinic-Appointment-Management-System
```

### Step 2: Compile the Project
```bash
# Navigate to project root
cd MediTrack-Clinic-Appointment-Management-System

# Compile all Java files
javac -d bin -cp src src/com/airtribe/meditrack/**/*.java
```

### Step 3: Run the Application
```bash
# Run the main application
java -cp bin com.airtribe.meditrack.Main

# Run with data loading
java -cp bin com.airtribe.meditrack.Main --loadData
```

## IDE Configuration

### IntelliJ IDEA:
1. File → New → Project from Existing Sources
2. Select the project directory
3. Set JDK in Project Structure → Project Settings → Project
4. Mark `src` directory as Sources Root

### Eclipse:
1. File → Import → Existing Maven Projects
2. Select the project directory
3. Configure build path to include JDK

### VS Code:
1. Install Java Extension Pack
2. Open project folder
3. Configure Java settings in `.vscode/settings.json`

## Project Structure

```
MediTrack-Clinic-Appointment-Management-System/
├── src/
│   └── com/
│       └── airtribe/
│           └── meditrack/
│               ├── entity/          # Core entity classes
│               ├── service/         # Business logic
│               ├── util/            # Utility classes
│               ├── exception/       # Custom exceptions
│               ├── interface/       # Interface definitions
│               ├── constants/       # Application constants
│               └── test/            # Test classes
├── docs/                    # Documentation files
├── data/                    # Data storage files
└── README.md               # Project documentation
```

## Common Issues and Solutions

### Issue: "javac: command not found"
**Solution**: Ensure JDK is installed and PATH is configured correctly.

### Issue: "NoClassDefFoundError"
**Solution**: Check classpath and ensure all compiled classes are in the correct directory.

### Issue: "Access denied"
**Solution**: Ensure proper file permissions on the project directory.

## Testing the Installation

1. Compile the project using the commands above
2. Run the TestRunner class:
```bash
java -cp bin com.airtribe.meditrack.test.TestRunner
```

This will execute all manual tests to verify the installation is working correctly.
