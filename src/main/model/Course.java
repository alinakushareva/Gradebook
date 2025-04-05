package main.model;

import java.util.*;

public class Course {
    private String courseName;
    private List<Student> students;
    private List<Assignment> assignments;
    private Map<Student, FinalGrade> finalGrades;
    private boolean useWeightedGrading;
    private int numAssignmentsToDrop;
    /*
     * Constructs a Course with a given name.
     * @param courseName Name of the course
     */
    public Course(String courseName) {
        this.courseName = courseName;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.finalGrades = new HashMap<>();
        this.useWeightedGrading = false;
        this.numAssignmentsToDrop = 0;
    }

    
    /*
     * Adds a student to the course.
     * @param student Student to enroll
     */
    public void addStudent(Student student) {
        students.add(student);
        student.addCourse(this);
    }

    /*
     * Removes a student from the course.
     * @param student Student to remove
     */
    public void removeStudent(Student student) {
        students.remove(student);
    }

    /*
     * Adds an assignment to the course.
     * @param assignment Assignment to add
     */
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    /*
     * @return All assignments in the course */
    public List<Assignment> getAssignments() {
        return assignments;
    }
    
    /*
     * Assigns a final grade to a student.
     * @param s  Student
     * @param grade Final grade
     */
    public void assignFinalGrade(Student s, FinalGrade grade) {
        finalGrades.put(s, grade);
        s.assignFinalGrade(this, grade);
    }

    /*
     * Returns a list of assignments that are not fully graded for all students.
     * @return List of ungraded assignments
     */
    public List<Assignment> getUngradedAssignments() {
        List<Assignment> ungraded = new ArrayList<>();
        for (Assignment a : assignments) {
            for (Student s : students) {
                if (!a.isGraded(s)) {
                    ungraded.add(a);
                    break;
                }
            }
        }
        return ungraded;
    }

    /*
     * Calculates the median percentage score for an assignment.
     * @param a Assignment to check
     * @return Median score (0 if no grades)
     */
    public double calculateMedianForAssignment(Assignment a) {
        List<Double> scores = new ArrayList<>();
        for (Student s : students) {
            Grade g = a.getGrade(s);
            if (g != null) {
                scores.add(g.getPercentage());
            }
        }
        Collections.sort(scores);
        int size = scores.size();
        if (size == 0) return 0;
        if (size % 2 == 1) return scores.get(size / 2);
        return (scores.get(size / 2 - 1) + scores.get(size / 2)) / 2.0;
    }

    /*
     * Calculates a student’s average score across all assignments in this course.
     * @param s Student
     * @return Average score percentage
     */
    public double calculateStudentAverage(Student s) {
        double total = 0;
        int count = 0;
        for (Assignment a : assignments) {
            Grade g = a.getGrade(s);
            if (g != null) {
                total += g.getPercentage();
                count++;
            }
        }
        return count > 0 ? total / count : 0;
    }

    /*
     * Sorts students alphabetically by full name.
     * @return Sorted list of students
     */
    public List<Student> sortStudentsByName() {
        students.sort(Comparator.comparing(Student::getFullName));
        return students;
    }


    /*
     * Sorts students by their grade on a specific assignment.
     * @param a Assignment
     * @return Sorted list of students by grade (descending)
     */
    public List<Student> sortStudentsByGrade(Assignment a) {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((s1, s2) -> {
            Grade g1= a.getGrade(s1);
            Grade g2= a.getGrade(s2);
            double p1 = g1 != null ? g1.getPercentage() : 0;
            double p2 = g2 != null ? g2.getPercentage() : 0;
            return Double.compare(p2, p1);
        });
        return sorted;
    }

    /*
     * Calculates the class average GPA based on final grades.
     * @return GPA average
     */
    public double calculateClassAverage() {
        double total = 0;
        int count = 0;
        for (Student s : students) {
            total += s.calculateGPA();
            count++;
        }
        return count > 0 ? total / count : 0;
    }
}
