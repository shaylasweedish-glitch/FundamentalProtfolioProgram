import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final StudentManager manager = new StudentManager();

    public static void main(String[] args) {
        seedTestData(); // helpful for testing; you can remove later if desired

        System.out.println("=== Student Records Manager ===");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ", 1, 9);

            switch (choice) {
                case 1 -> addStudentFlow();
                case 2 -> listStudentsFlow();
                case 3 -> searchByIdFlow();
                case 4 -> searchByLastNameFlow();
                case 5 -> updateStudentFlow();
                case 6 -> removeStudentFlow();
                case 7 -> sortFlow();
                case 8 -> statsFlow();
                case 9 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("""
            1) Add student
            2) List all students
            3) Search by ID
            4) Search by Last Name
            5) Update student
            6) Remove student
            7) Sort students
            8) Compute statistics
            9) Quit
            """);
    }

    // ---------- Feature Flows ----------

    private static void addStudentFlow() {
        System.out.println("--- Add Student ---");
        int id = readInt("Student ID (positive integer): ", 1, Integer.MAX_VALUE);

        if (manager.findById(id) != null) {
            System.out.println("Error: A student with that ID already exists.");
            return;
        }

        String first = readNonEmptyString("First name: ");
        String last = readNonEmptyString("Last name: ");
        double gpa = readDouble("GPA (0.0 - 4.0): ", 0.0, 4.0);

        boolean ok = manager.addStudent(new Student(id, first, last, gpa));
        System.out.println(ok ? "Student added." : "Could not add student.");
    }

    private static void listStudentsFlow() {
        System.out.println("--- All Students ---");
        if (manager.getAllStudents().isEmpty()) {
            System.out.println("(No records yet)");
            return;
        }
        for (Student s : manager.getAllStudents()) {
            System.out.println(s);
        }
    }

    private static void searchByIdFlow() {
        System.out.println("--- Search by ID ---");
        int id = readInt("Enter student ID: ", 1, Integer.MAX_VALUE);
        Student s = manager.findById(id);
        System.out.println(s == null ? "Student not found." : s.toString());
    }

    private static void searchByLastNameFlow() {
        System.out.println("--- Search by Last Name ---");
        String last = readNonEmptyString("Enter last name: ");
        ArrayList<Student> matches = manager.findByLastName(last);

        if (matches.isEmpty()) {
            System.out.println("No students found with that last name.");
            return;
        }
        System.out.println("Matches:");
        for (Student s : matches) System.out.println(s);
    }

    private static void updateStudentFlow() {
        System.out.println("--- Update Student ---");
        int id = readInt("Enter student ID to update: ", 1, Integer.MAX_VALUE);
        Student s = manager.findById(id);

        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Current: " + s);

        System.out.println("Leave blank to keep the current value.");
        String newFirst = readOptionalString("New first name: ");
        String newLast = readOptionalString("New last name: ");
        Double newGpa = readOptionalDouble("New GPA (0.0 - 4.0): ", 0.0, 4.0);

        boolean ok = manager.updateStudent(
                id,
                newFirst.isBlank() ? null : newFirst,
                newLast.isBlank() ? null : newLast,
                newGpa
        );

        System.out.println(ok ? "Updated: " + manager.findById(id) : "Update failed.");
    }

    private static void removeStudentFlow() {
        System.out.println("--- Remove Student ---");
        int id = readInt("Enter student ID to remove: ", 1, Integer.MAX_VALUE);
        boolean ok = manager.removeById(id);
        System.out.println(ok ? "Student removed." : "Student not found.");
    }

    private static void sortFlow() {
        System.out.println("--- Sort Students ---");
        System.out.println("""
            1) Sort by ID
            2) Sort by Last Name (then First, then ID)
            """);
        int c = readInt("Choose sort option: ", 1, 2);
        if (c == 1) manager.sortById();
        else manager.sortByLastNameThenFirst();
        System.out.println("Sorted.");
        listStudentsFlow();
    }

    private static void statsFlow() {
        System.out.println("--- Statistics ---");
        if (manager.getAllStudents().isEmpty()) {
            System.out.println("(No records yet)");
            return;
        }

        double avg = manager.averageGpa();
        double hi = manager.highestGpa();
        double threshold = readDouble("Count students with GPA >= what threshold? ", 0.0, 4.0);

        // Recursion used meaningfully here
        int count = manager.countAboveOrEqualGpaRecursive(threshold);

        System.out.printf("Average GPA: %.2f%n", avg);
        System.out.printf("Highest GPA: %.2f%n", hi);
        System.out.printf("Count GPA >= %.2f: %d%n", threshold, count);
    }

    // ---------- Input Helpers (Validation + try/catch) ----------

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val < min || val > max) {
                    System.out.printf("Please enter an integer between %d and %d.%n", min, max);
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double val = Double.parseDouble(line);
                if (val < min || val > max) {
                    System.out.printf("Please enter a number between %.1f and %.1f.%n", min, max);
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (example: 3.5).");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("This field is required. Please enter a value.");
        }
    }

    private static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim(); // may be blank
    }

    private static Double readOptionalDouble(String prompt, double min, double max) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        if (line.isEmpty()) return null;

        try {
            double val = Double.parseDouble(line);
            if (val < min || val > max) {
                System.out.printf("Out of range (%.1f - %.1f). Keeping current GPA.%n", min, max);
                return null;
            }
            return val;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Keeping current GPA.");
            return null;
        }
    }

    // ---------- Starter Data for Testing ----------
    private static void seedTestData() {
        manager.addStudent(new Student(1001, "Ava", "Nguyen", 3.8));
        manager.addStudent(new Student(1002, "Liam", "Patel", 2.9));
        manager.addStudent(new Student(1003, "Mia", "Garcia", 3.5));
    }
}
