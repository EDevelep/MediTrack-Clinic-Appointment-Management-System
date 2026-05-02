# MediTrack Clinic Appointment Management System

A comprehensive Java-based clinic appointment management system demonstrating advanced OOP concepts, design patterns, and Java features.

## 🏥 Overview

MediTrack is a complete clinic management solution that handles patient appointments, doctor schedules, billing, and provides AI-powered recommendations. This project showcases proficiency in Java development, object-oriented programming, and software design patterns.

## ✨ Features

### Core Functionality
- **Patient Management**: Complete CRUD operations for patient records
- **Doctor Management**: Doctor profiles with specializations and availability
- **Appointment Scheduling**: Smart appointment booking with conflict detection
- **Billing System**: Flexible billing with multiple strategies and tax calculations
- **Search & Filter**: Advanced search capabilities across all entities

### Advanced Features
- **AI Recommendations**: Symptom-based doctor recommendations
- **File I/O & Persistence**: CSV-based data storage and backup
- **Real-time Notifications**: Observer pattern for appointment updates
- **Analytics Dashboard**: Comprehensive statistics and insights
- **Smart Scheduling**: Automatic slot finding and availability checking

### Technical Features
- **Design Patterns**: Singleton, Factory, Observer, Template Method
- **Advanced OOP**: Inheritance, Polymorphism, Abstraction, Encapsulation
- **Java 8+ Features**: Streams, Lambdas, Functional Interfaces
- **Exception Handling**: Custom exceptions with chaining
- **Data Validation**: Comprehensive validation framework
- **Cloning Support**: Deep vs shallow copy demonstrations

## 🏗️ Architecture

### Package Structure
```
com.airtribe.meditrack/
├── entity/          # Core entities (Person, Doctor, Patient, Appointment, Bill)
├── service/         # Business logic services
├── util/            # Utility classes (Validator, DateUtil, CSVUtil, etc.)
├── factory/         # Factory pattern implementations
├── observer/        # Observer pattern for notifications
├── exception/       # Custom exception classes
├── interface/       # Interface definitions
├── constants/       # Application constants
└── test/            # Manual testing framework
```

### Key Components

#### Entities
- **Person**: Abstract base class for people
- **Doctor**: Medical professionals with specializations
- **Patient**: Medical patients with history and allergies
- **Appointment**: Scheduled visits with status tracking
- **Bill**: Financial transactions with multiple billing strategies
- **BillSummary**: Immutable billing summaries

#### Services
- **DoctorService**: Doctor management and recommendations
- **PatientService**: Patient records and medical history
- **AppointmentService**: Scheduling and appointment management
- **DataPersistenceService**: CSV-based data storage

#### Design Patterns
- **Singleton**: Application configuration and ID generation
- **Factory**: Bill creation with different strategies
- **Observer**: Real-time appointment notifications
- **Strategy**: Multiple billing approaches

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Git for version control
- An IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/MediTrack-Clinic-Appointment-Management-System.git
   cd MediTrack-Clinic-Appointment-Management-System
   ```

2. **Compile the project**
   ```bash
   javac -d bin -cp src src/com/airtribe/meditrack/**/*.java
   ```

3. **Run the application**
   ```bash
   # Normal mode
   java -cp bin com.airtribe.meditrack.Main
   
   # With data loading
   java -cp bin com.airtribe.meditrack.Main --loadData
   ```

4. **Run tests**
   ```bash
   java -cp bin com.airtribe.meditrack.test.TestRunner
   ```

## 📋 Usage

### Main Menu Options

1. **Manage Doctors**
   - Add new doctors with specializations
   - Update availability and fees
   - Search by name, specialization, or availability

2. **Manage Patients**
   - Register new patients
   - Update medical history and allergies
   - Search by various criteria

3. **Manage Appointments**
   - Schedule appointments with conflict detection
   - Update appointment status
   - Cancel and reschedule

4. **Manage Bills**
   - Generate bills with different strategies
   - Process payments
   - Create billing summaries

5. **Search**
   - Global search across all entities
   - Filter by specific criteria

6. **View Statistics**
   - System analytics and insights
   - Performance metrics

7. **AI Features**
   - Doctor recommendations by symptoms
   - Available slot finder
   - Analytics dashboard

## 🧪 Testing

The project includes a comprehensive test runner that validates:

- **Basic Functionality**: ID generation, configuration, date utilities
- **Entity Creation**: Object instantiation and validation
- **Service Operations**: CRUD operations and business logic
- **Design Patterns**: Singleton, Factory, Observer implementations
- **File I/O**: Data persistence and backup
- **Advanced Features**: AI recommendations and search
- **Error Handling**: Exception management
- **Validation**: Data integrity checks
- **Search & Filter**: Query functionality
- **Analytics**: Statistical reporting

Run tests with:
```bash
java -cp bin com.airtribe.meditrack.test.TestRunner
```

## 📊 Data Persistence

### CSV Storage
- **Doctors**: `data/doctors.csv`
- **Patients**: `data/patients.csv`
- **Appointments**: `data/appointments.csv`
- **Bills**: `data/bills.csv`

### Backup System
- Automatic timestamped backups
- Data restoration capabilities
- Configuration-driven persistence

## 🔧 Configuration

### Application Settings
Configuration is managed through the `AppConfig` singleton:

```java
AppConfig config = AppConfig.getInstance();
double taxRate = config.getDoubleProperty("tax.rate");
boolean backupEnabled = config.getBooleanProperty("backup.enabled");
```

### Key Settings
- Tax rates and service charges
- Consultation fee defaults
- Appointment duration
- Backup preferences
- Notification settings

## 🎯 Java Concepts Demonstrated

### Core OOP (35 points)
- **Encapsulation**: Private fields with validation
- **Inheritance**: Person → Doctor, Patient hierarchy
- **Polymorphism**: Method overloading and overriding
- **Abstraction**: Abstract classes and interfaces

### Advanced OOP
- **Cloning**: Deep vs shallow copy implementation
- **Immutability**: Thread-safe BillSummary class
- **Enums**: Specialization and AppointmentStatus
- **Static Initialization**: Application-wide configuration

### Collections & Generics
- **DataStore<T>**: Generic storage implementation
- **ArrayList & HashMap**: Core data structures
- **Streams & Lambdas**: Modern Java processing
- **Comparators**: Custom sorting logic

### Exception Handling
- **Custom Exceptions**: AppointmentNotFoundException, InvalidDataException
- **Exception Chaining**: Proper error propagation
- **Try-with-Resources**: Safe file operations

### File I/O & Serialization
- **CSV Processing**: Custom CSV parsing and generation
- **Data Persistence**: Automatic save/load functionality
- **Backup System**: Timestamped data backups

### Concurrency
- **AtomicInteger**: Thread-safe ID generation
- **Synchronized Methods**: Thread-safe operations
- **Observer Pattern**: Event-driven notifications

### Design Patterns
- **Singleton**: Configuration management
- **Factory**: Bill creation strategies
- **Strategy**: Multiple billing approaches
- **Observer**: Real-time notifications

### Java 8+ Features
- **Streams API**: Data filtering and processing
- **Lambda Expressions**: Functional programming
- **Optional**: Null-safe operations
- **DateTime API**: Modern date handling

## 📈 Performance Features

### Optimization Techniques
- **Lazy Loading**: Efficient data access
- **Caching**: Frequently accessed data
- **Batch Operations**: Bulk processing
- **Memory Management**: Proper object lifecycle

### Analytics
- **Real-time Statistics**: Live system metrics
- **Performance Monitoring**: Operation tracking
- **Usage Analytics**: User behavior insights

## 🔍 Search Capabilities

### Multi-criteria Search
- **Entity-specific**: Doctors, patients, appointments
- **Global Search**: Cross-entity queries
- **Filter Options**: Status, specialization, date ranges
- **AI-powered**: Symptom-based recommendations

### Advanced Filtering
- **Date Ranges**: Flexible time-based queries
- **Status Filtering**: Appointment status management
- **Specialization**: Medical expertise matching
- **Availability**: Real-time scheduling

## 📝 Documentation

- **Setup Instructions**: `docs/Setup_Instructions.md`
- **JVM Report**: `docs/JVM_Report.md`
- **Code Documentation**: Comprehensive JavaDocs
- **API Reference**: Method-level documentation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is for educational purposes as part of Java development training.

## 🎓 Learning Objectives

This project demonstrates proficiency in:

### Java Fundamentals (10 points)
- **JDK/JRE Setup**: Environment configuration
- **JVM Internals**: Understanding of Java Virtual Machine
- **Write Once, Run Anywhere**: Platform independence

### Object-Oriented Programming (35 points)
- **Encapsulation**: Data hiding and validation
- **Inheritance**: Code reuse and hierarchy
- **Polymorphism**: Method overriding and overloading
- **Abstraction**: Interface and abstract class usage

### Advanced Java Concepts (20 points)
- **Collections Framework**: Generic data structures
- **Exception Handling**: Robust error management
- **File I/O**: Data persistence and CSV processing
- **Concurrency**: Thread-safe operations

### Design Patterns (10 points)
- **Singleton**: Global access management
- **Factory**: Object creation abstraction
- **Observer**: Event notification system
- **Strategy**: Algorithm selection

### Modern Java Features (10 points)
- **Streams API**: Functional data processing
- **Lambda Expressions**: Concise functional code
- **Optional**: Null safety
- **DateTime API**: Modern date handling

### Bonus Features (20 points)
- **File I/O & Persistence**: CSV-based storage
- **Design Patterns**: Multiple pattern implementations
- **AI Features**: Rule-based recommendations
- **Streams & Lambdas**: Advanced functional programming

## 📞 Support

For questions or support regarding this educational project, please refer to the documentation or contact the development team.

---

**Note**: This is an educational project designed to demonstrate Java programming proficiency and software development best practices.
