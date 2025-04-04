# Gradebook Management System

A comprehensive system for managing course grades in an educational setting, designed for both teachers and students.

## Features

- **User Management**: Separate interfaces for students and teachers
- **Course Management**: Create and manage courses, enroll students
- **Assignment Management**: Create assignments, categories, and grade weighting
- **Grade Tracking**: Record and view grades, calculate averages and GPA
- **Analytics**: Class statistics, sorting students by performance, grouping students

## Architecture

The system follows the Model-View-Controller (MVC) architecture pattern:

- **Model**: Core classes like Student, Teacher, Course, Assignment, Grade
- **View**: JavaFX UI components for different user roles and functions
- **Controller**: Business logic for handling user interactions and data operations

## Technical Details

- **Language**: Java
- **UI Framework**: JavaFX
- **Testing**: JUnit 5
- **Build System**: Maven

## Security Features

- Password hashing for secure storage
- Input validation for usernames and passwords
- Defensive copying for protecting internal data

## Getting Started

### Prerequisites

- Java 11 or later
- Maven

### Building the Project

```bash
mvn clean package
```

### Running the Application

```bash
mvn javafx:run
```

### Running Tests

```bash
mvn test
```

## Project Structure

- `src/main/java/org/gradebook/model` - Core domain models
- `src/main/java/org/gradebook/view` - UI components
- `src/main/java/org/gradebook/controller` - Application controllers
- `src/main/java/org/gradebook/util` - Utility classes
- `src/test/java` - Unit tests 