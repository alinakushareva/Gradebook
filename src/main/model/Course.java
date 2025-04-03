package model;

import java.util.*;

public class Course {
    private String courseName;
    private List<Student> students;
    private List<Assignment> assignments;
    private Map<Student, FinalGrade> finalGrades;
    private boolean useWeightedGrading;
    private int numAssignmentsToDrop;
  public Course(String courseName) {
        this.courseName = courseName;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.finalGrades = new HashMap<>();
        this.useWeightedGrading = false;
        this.numAssignmentsToDrop = 0;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }
    public void assignFinalGrade(Student s, FinalGrade grade){}


//    public List<Assignment> getUngradedAssignments(){}
//
//
//    public double calculateMedianForAssignment(Assignment a){}
//
//
//    public double calculateStudentAverage(Student s){}
//
//
//    public List<Student> sortStudentsByName(){}
//
//
//    public List<Student> sortStudentsByGrade(Assignment a){}

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