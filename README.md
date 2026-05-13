# 🎓 Grade Tracker (Java Swing App)

A desktop application built with **Java Swing** that allows users to manage students and their grades.  
It supports registering students, adding grades, searching students, and calculating averages — all with persistent file storage.

---

## ✨ Features

- 👨‍🎓 Register new students
- ➕ Add grades (4–10 scale)
- 🔍 Search student by name
- 📊 View all grades and averages
- 📈 Calculate individual and overall averages
- 💾 File-based data storage (persistent data)
- 🎨 Modern styled Java Swing GUI

---

## 🖥️ Interface Overview

The application is divided into three main sections:

### 🟦 Top Panel
- Register new students
- Add grades for existing students
- Search students

### 🟩 Center Panel
- Displays student information
- Shows grades, sum, and average

### 🟨 Bottom Panel
- View all averages
- View all stored grades

---

### 💾 Data Format
- StudentName:8,9,10
- John:7,8,9
- Anna:10,9

---

## 🚀 How to Run

### 1. Compile the program
Make sure you have Java installed (JDK 8+).

```bash id="grade_compile"
javac GradeTracker.java
