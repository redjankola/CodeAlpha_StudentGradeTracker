#GradeTracker 🎓

GradeTracker is a simple Java Swing desktop application for managing students and their grades. It allows you to register students, add grades, view all grades, and calculate averages — with data saved locally in a text file.

---

##✨ Features
📌 Register new students
➕ Add grades for each student
🔎 Search students and view details
📊 Calculate individual averages
📋 View all grades or all averages
💾 Persistent storage using a local .txt file
🎨 Modern dark-themed Swing UI

---

##📂 Project Structure
GradeTracker.java   → Main application (GUI + logic)
notat.txt           → Auto-generated data file (stored in user home directory)

---

##🚀 How to Run
1. Requirements
Java 8 or higher
Any IDE (IntelliJ IDEA, Eclipse, NetBeans) or terminal
2.Compile
javac GradeTracker.java
3. Run
java GradeTracker

---

##💡 How It Works
Each student is stored in a text file in this format:
John:8.5,9,10
Anna:7,8,9.5
The app loads data on startup and updates the file whenever:
A student is registered
A grade is added

---

##📌 Main Functionalities
Register Student

Enter a name and click Register.

Add Grade

Select a student (by name) and add a grade (4–10 scale).

Search Student

Displays:

All grades
Total sum
Average score
Show All Grades

Displays all students with their grades.

All Averages

Shows average grade per student.

---

