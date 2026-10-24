# Student Management System

A desktop-based Student Management System built using **Java Swing**. The application provides a clean graphical user interface to manage student records efficiently, with features for adding, viewing, searching, updating, and deleting student information.

## Features

- **Dashboard:** Displays student statistics, including total students, male and female students, and departments.
- **Add Student:** Register students with their personal and academic details.
- **View Students:** Display student records in a structured table.
- **Search Students:** Search records instantly using the search field.
- **Update Student:** Edit existing student information.
- **Delete Student:** Remove student records with confirmation.
- **Input Validation:** Validates required fields, age, email format, and duplicate roll numbers.
- **Responsive Layout:** Adapts the interface to different window sizes.
- **Sample Data:** Includes sample student records for demonstration.

## Technologies Used

- **Programming Language:** Java
- **GUI Framework:** Java Swing
- **Table Component:** JTable
- **Layout Management:** BorderLayout, GridLayout, BoxLayout
- **Data Storage:** Java ArrayList (in-memory storage)
- **IDE:** IntelliJ IDEA
- **JDK:** Java 17 or newer

## Project Structure

```text
StudentManagementSystem/
├── src/
│   └── Main.java
└── README.md
```

## Prerequisites

Before running the project, make sure you have:

- JDK 17 or newer installed
- IntelliJ IDEA installed
- Basic knowledge of Java and object-oriented programming

No external libraries or database setup are required.

## Installation and Setup

1. Clone or download the project.
2. Open IntelliJ IDEA.
3. Select **Open** and choose the project folder.
4. Ensure that a compatible JDK is configured in Project Structure.
5. Open `Main.java` inside the `src` folder.
6. Click the green **Run** button next to the `main()` method.

Alternatively, compile and run from a terminal:

```bash
javac src/Main.java
java -cp src Main
```

Run these commands from the project root.

## How to Use

### 1. Add a Student
Enter the student's name, roll number, age, gender, department, section, email, phone number, and address. Click **Add** to register the student.

### 2. View Student Records
All student records are displayed in the table on the dashboard. Click a table row to load that student's details into the form.

### 3. Search Students
Enter a name, roll number, department, or other displayed field into the search box. The table filters results as you type.

### 4. Update Student Details
Select a student, modify the details in the form, and click **Update**.

### 5. Delete a Student
Select a student and click **Delete**. Confirm the action to remove the record.

### 6. Clear the Form
Click **Clear** to reset the form and deselect the current student.

## Data Storage

This version uses an `ArrayList<Student>` to store records in memory.

**Important:** Student records are not permanently saved. All records, including changes and additions, are lost when the application closes.

A future version can integrate MySQL with JDBC to provide permanent database storage.

## Validation and Error Handling

The application checks that:

- Student name and roll number are provided.
- Roll numbers are unique.
- Age is a valid number between 1 and 120.
- Email addresses follow a basic email format.
- A student is selected before updating or deleting a record.

## Future Enhancements

- MySQL database integration using JDBC
- Login and authentication
- Permanent storage of student records
- Export records to CSV or Excel
- Advanced search and filtering
- Attendance and marks management
- Student profile photos
- Report generation

## Learning Outcomes

This project demonstrates practical use of:

- Java classes and objects
- Encapsulation of student data
- Java Swing GUI development
- Event handling with action listeners
- JTable and DefaultTableModel
- ArrayList and collection operations
- Input validation and exception handling
- Layout managers and responsive UI design

## Author

**Student Management System Project**

Developed as a Java desktop application for learning GUI development and student record management.

## License

This project is intended for educational and learning purposes. You may modify and extend it for academic use.
