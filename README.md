# Gradebook Application – CSC 335 Final Project

## 📚 Overview

The Gradebook project is a full-featured JavaFX desktop application that allows teachers and students to manage coursework in a clean, intuitive environment. Teachers can create and categorize assignments, grade students, apply custom rules like dropping the lowest scores, and assign final grades based on total points or weighted categories. Students can log in to view their assignments, current averages, and final grades across all enrolled courses.

### 💡 Design Overview

This project was developed with a strong emphasis on clean code, reusability, and modular architecture. We followed key object-oriented design principles and actively avoided common antipatterns:

- ❌ **Primitive Obsession**: Enums like `FinalGrade` are used instead of raw strings or integers for clarity and safety.
- ❌ **God Class**: Responsibilities are cleanly divided between models (`Course`, `Student`, `Teacher`, `Assignment`), controllers, and view classes.
- ❌ **Duplicated Code**: Reusable logic like grade calculations and drop-lowest rules are centralized in `GradeCalculator`.
- ❌ **Escaping References**: Methods like `getCourses()` and `getAssignments()` return defensive copies to maintain encapsulation.

We also implemented proven design patterns:

- ✅ **Model-View-Controller (MVC)**: The architecture cleanly separates logic and UI across `model`, `controller`, and `view` packages.
- ✅ **Observer Pattern**: A custom `Observer` and `Subject` system automatically updates UI components when model state changes.
- ✅ **Factory-Like Control**: Controllers abstract away creation and logic handling for entities like assignments and grades.

The result is a maintainable, extendable system that’s easy to understand, debug, and build upon in future iterations.

---

## ✅ Functionality

### 👩‍🎓 Students Should Be Able To:
- View their **courses**, both **current and completed**
- View **assignments** in a course, whether **graded or ungraded**
- See their **current average** in a course based on **graded assignments**
- Automatically calculate and view their **GPA**, based on final grades from completed courses

### 👨‍🏫 Teachers Should Be Able To:
- View their **courses**, both **current and completed**
- **Add and remove assignments** to/from a course
- **Add and remove students** from a course *(for simplicity, even though it's not typical)*
- **Import a list of students** to a course from a `.txt` file
- View the list of **students enrolled** in a course
- **Assign grades** to students for each assignment
- **Calculate averages and medians** for a given assignment
- **Calculate a student's current average** in the course
- **Sort students** by first name, last name, or username
- **Sort students** by their grades on a specific assignment
- **Put students into groups** of customizable size
- **Assign final grades** (`A`, `B`, `C`, `D`, `E`) to students based on their course averages
- View a list of **ungraded assignments**
- **Choose a grading mode** for calculating final grades in a course
- **Set up assignment categories**, including:
  - Assigning **weights** (e.g., Homework 40%, Exams 60%)
  - **Dropping the lowest n assignments** per category

### 📊 Supported Grading Modes (Only One Mode Can Be Active Per Course):
- **Mode 1: Total Points Grading**  
  Final Grade = Total Points Earned / Total Points Possible  
  *(No weighting – just raw totals)*

- **Mode 2: Weighted Categories**  
  Assignments are grouped into categories with assigned weights.  
  Teachers can also drop the lowest `n` assignments in each category.  
  Final grade is computed as a weighted average across categories.

---

## 🧪 Testing

This project includes comprehensive **JUnit 5** unit tests in `src/test`, with over **90% branch coverage**. Tested classes include:

- All model classes (`Course`, `Student`, `Teacher`, `Assignment`, `Grade`, `FinalGrade`, `Category`, and etc)
- All controller classes
- Utility classes like `GradeCalculator`

We’ve tested:
- Normal logic flows
- Edge cases (e.g., zero assignments, missing grades)
- Error handling (null values, out-of-range points)
- Median and average calculations
- GPA update propagation

---

## 🤝 Collaboration

This project was developed collaboratively by a team of four students over five weeks. From the beginning, we broke down the project into manageable parts and collaboratively created a shared Google Doc that outlined all planned classes, their attributes, and methods. This served as a blueprint for our implementation and helped ensure consistency across the entire application.

We maintained ongoing communication and support through a dedicated Discord server, where we shared updates, discussed blockers, and provided feedback on each other's work. We also met regularly as a team to integrate features, review code, and test functionality.

We held one formal standup meeting with our grader, where we demonstrated our progress and received valuable feedback. Throughout the project, we supported one another and collaborated closely on design decisions, debugging, and feature completion.

All contributions are version-controlled and can be viewed in our GitHub repository:  
[https://github.com/alinakushareva/Gradebook](https://github.com/alinakushareva/Gradebook)


---

## ▶️ How to Run the Project

To run the Gradebook application locally, follow these steps:

1. **Install Java JDK 17 or higher**  
   You can download it from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html).

2. **Download JavaFX SDK 23.0.1**  
   Go to [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)  
   Unzip the SDK and place it in a known location, such as:  
   `~/libraries/javafx-sdk-23.0.1`

3. **Pull the latest version of the project**
   ```bash
   git checkout main
   git pull origin main

4. **Compile the project**
	Run the following from the root directory of the project:   `javac --module-path ~/libraries/javafx-sdk-23.0.1/lib \--add-modules javafx.controls,javafx.fxml \-d bin \$(find src/main -name "*.java")`

5. **Run the application**
	   `java --module-path ~/libraries/javafx-sdk-23.0.1/lib \--add-modules javafx.controls,javafx.fxml \-cp bin view.MainApp`
	   
	   
## 🤖 Use of AI

We used AI tools exclusively to assist with **frontend layout design in the `view` package**. This included refining JavaFX scene structures, organizing UI components, and improving visual consistency. **All business logic, algorithms, model classes, controller functionality, and test code were manually implemented by the team**. The role of AI was limited to enhancing visual and usability aspects without impacting any core logic or computation.

---

## 👥 Authors

- Alina Kushareva  
- [Teammate 2 Name]  
- [Teammate 3 Name]  
- [Teammate 4 Name]

---

Thank you for reviewing our project!
