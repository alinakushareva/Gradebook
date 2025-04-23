package model;

import java.util.*;
import java.util.stream.Collectors;

public class Course {
	private final String courseName;
    private final List<Student> students;
    private final List<Assignment> assignments;
    private final List<Category> categories;
    private final Map<Student, FinalGrade> finalGrades;
    private boolean useWeightedGrading;
    private int numAssignmentsToDrop;
    
  public Course(String courseName) {
	  this.courseName = courseName;
      this.students = new ArrayList<>();
      this.assignments = new ArrayList<>();
      this.categories = new ArrayList<>();
      this.finalGrades = new HashMap<>();
      this.useWeightedGrading = false;
      this.numAssignmentsToDrop = 0;
    }

  public void addStudent(Student student) {
      if (!students.contains(student)) {
          students.add(student);
          student.addCourse(this);
      }
  }

  public void removeStudent(Student student) {
      students.remove(student);
      student.getCourses().remove(this);
  }

  public void addAssignment(Assignment assignment) {
      if (!assignments.contains(assignment)) {
          assignments.add(assignment);
      }
  }
  
  public void removeAssignment(Assignment assignment) {
	    assignments.remove(assignment);
	}


  // Grading calculations
  public double calculateClassAverage() {
      return students.stream()
          .mapToDouble(this::calculateStudentAverage)
          .average()
          .orElse(0.0);
  }

  public double calculateStudentAverage(Student student) {
      if (useWeightedGrading) {
          return calculateWeightedAverage(student);
      } else {
          return calculateTotalPointsAverage(student);
      }
  }

  private double calculateTotalPointsAverage(Student student) {
	    double totalEarned = assignments.stream()
	        .map(a -> a.getGrade(student))
	        .filter(g -> g != null)
	        .mapToDouble(Grade::getPointsReceived)
	        .sum();

	    double totalPossible = assignments.stream()
	        .map(a -> a.getGrade(student))
	        .filter(g -> g != null)
	        .mapToDouble(Grade::getMaxPoints)
	        .sum();

	    return totalPossible > 0 ? (totalEarned / totalPossible) * 100 : 0.0;
	}


  private double calculateWeightedAverage(Student student) {
      return categories.stream()
          .mapToDouble(c -> c.calculateCategoryAverage(student) * c.getWeight())
          .sum();
  }

  // Sorting/grouping
  public List<Student> sortStudentsByName() {
	    return students.stream()
	        .sorted(Comparator.comparing((Student s) -> s.getLastName())
	            .thenComparing(s -> s.getFirstName()))
	        .collect(Collectors.toList());
	}

  public List<Student> sortStudentsByAssignmentGrade(Assignment assignment) {
      return students.stream()
          .sorted((s1, s2) -> Double.compare(
              assignment.getGrade(s2).getPercentage(),
              assignment.getGrade(s1).getPercentage()
          ))
          .collect(Collectors.toList());
  }

  // Getters and configuration
  public String getCourseName() {
      return courseName;
  }

  public List<Student> getStudents() {
      return new ArrayList<>(students);
  }

  public List<Assignment> getAssignments() {
      return new ArrayList<>(assignments);
  }

  public void setGradingMode(boolean useWeightedGrading) {
      this.useWeightedGrading = useWeightedGrading;
  }

  public void setAssignmentsToDrop(int numToDrop) {
      this.numAssignmentsToDrop = Math.max(numToDrop, 0);
  }

  // Additional methods
  public List<Assignment> getUngradedAssignments() {
      return assignments.stream()
          .filter(a -> !a.isFullyGraded(students))
          .collect(Collectors.toList());
  }

  public void assignFinalGrade(Student student, FinalGrade grade) {
      finalGrades.put(student, grade);
  }

  public FinalGrade getFinalGrade(Student student) {
      return finalGrades.getOrDefault(student, null);
  }
  
  public boolean isCompleted(Student student) {
      return student.getFinalGrade(this) != null;
  }
}