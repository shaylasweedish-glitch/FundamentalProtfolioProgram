import java.util.ArrayList;
import java.util.Comparator;

public class StudentManager {
    private final ArrayList<Student> students = new ArrayList<>();

    public boolean addStudent(Student s) {
        if (findById(s.getId()) != null) return false; // prevent duplicate IDs
        students.add(s);
        return true;
    }

    public ArrayList<Student> getAllStudents() {
        return students;
    }

    public Student findById(int id) {
        for (Student s : students) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    public ArrayList<Student> findByLastName(String lastName) {
        ArrayList<Student> matches = new ArrayList<>();
        for (Student s : students) {
            if (s.getLastName().equalsIgnoreCase(lastName.trim())) {
                matches.add(s);
            }
        }
        return matches;
    }

    public boolean removeById(int id) {
        Student s = findById(id);
        if (s == null) return false;
        students.remove(s);
        return true;
    }

    public boolean updateStudent(int id, String newFirst, String newLast, Double newGpa) {
        Student s = findById(id);
        if (s == null) return false;

        if (newFirst != null) s.setFirstName(newFirst);
        if (newLast != null) s.setLastName(newLast);
        if (newGpa != null) s.setGpa(newGpa);

        return true;
    }

    public void sortById() {
        students.sort(Comparator.comparingInt(Student::getId));
    }

    public void sortByLastNameThenFirst() {
        students.sort(
            Comparator.comparing(Student::getLastName, String.CASE_INSENSITIVE_ORDER)
                      .thenComparing(Student::getFirstName, String.CASE_INSENSITIVE_ORDER)
                      .thenComparingInt(Student::getId)
        );
    }

    public double averageGpa() {
        if (students.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Student s : students) sum += s.getGpa();
        return sum / students.size();
    }

    public double highestGpa() {
        if (students.isEmpty()) return 0.0;
        double best = students.get(0).getGpa();
        for (Student s : students) {
            if (s.getGpa() > best) best = s.getGpa();
        }
        return best;
    }

    /**
     * Recursion demo (purposeful): counts students with GPA >= threshold.
     * Base case: index reaches the end of the list.
     * Recursive case: count the rest of the list and add 1 if this student qualifies.
     */
    public int countAboveOrEqualGpaRecursive(double threshold) {
        return countAboveOrEqualGpaRecursive(threshold, 0);
    }

    private int countAboveOrEqualGpaRecursive(double threshold, int index) {
        if (index >= students.size()) return 0; // base case
        int add = (students.get(index).getGpa() >= threshold) ? 1 : 0;
        return add + countAboveOrEqualGpaRecursive(threshold, index + 1); // recursive step
    }
}
