# FundamentalProtfolioProgram
Student Records Manager is a Java console application that demonstrates core computer science concepts including object-oriented programming, ArrayList data structures, recursion, input validation, and algorithmic problem solving. The program allows users to add, view, search, update, remove, and sort student records through a menu-based interface.

# Student Records Manager (Java Console App)

This project is a menu-based Java console application that stores and manages student records.
It demonstrates core programming skills including problem-solving, algorithms, OOP design, input validation, and recursion.

## How to Run
1. Compile:
   - `javac Main.java Student.java StudentManager.java`
2. Run:
   - `java Main`

## Features Implemented
- Add a student (rejects duplicate IDs)
- List all students
- Search by student ID
- Search by last name
- Update student information (name and/or GPA)
- Remove student
- Sort students by ID or by last name
- Compute statistics (average GPA, highest GPA, count above threshold)

## Where Recursion Is Used (and Why)
Recursion is used in `StudentManager.countAboveOrEqualGpaRecursive(threshold)` to count how many students have GPA >= a threshold.
This demonstrates recursion with a clear base case (end of list) and a recursive step (count the rest and add 1 if current student qualifies).

## Testing
See the 8+ test cases in the checklist below (use the seeded records to start quickly).
