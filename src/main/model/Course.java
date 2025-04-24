/**
 * Project Name: Gradebook
 * File Name: Course.java
 * Course: CSC 335 Spring 2025
 * Purpose: Represents a course with students, assignments, categories, and grading logic.
 *          Supports total-points and weighted grading modes, grade assignment, and observer notifications.
 */
package model;

import java.util.*;
import java.util.stream.Collectors;

import util.GradeCalculator;

public class Course implements Subject {
	private final String courseName;
    private final List<Student> students;
    private final List<Assignment> assignments;
    private final List<Category> categories;
    private final Map<Student, FinalGrade> finalGrades;
    private boolean useWeightedGrading;
    private int numAssignmentsToDrop;
    private final List<Observer> observers = new ArrayList<>();
    
  /**
   * Constructs a course with a name and initializes internal structures.
   * @param courseName name of the course
   */  
  public Course(String courseName) {
	  this.courseName = courseName;
      this.students = new ArrayList<>();
      this.assignments = new ArrayList<>();
      this.categories = new ArrayList<>();
      this.finalGrades = new HashMap<>();
      this.useWeightedGrading = false;
      this.numAssignmentsToDrop = 0;
    }

  /**
   * Enrolls a student in this course and notifies observers.
   *
   * @param student the student to add
   */

  public void addStudent(Student student) {
      if (!students.contains(student)) {
          students.add(student);
          student.addCourse(this); // link both ways
          notifyObservers(); // notify UI
      }
  }

  /**
   * Removes a student from this course and notifies observers.
   *
   * @param student the student to remove
   */
  public void removeStudent(Student student) {
      students.remove(student);
      student.getCourses().remove(this); // remove bidirectional link
      notifyObservers();
  }

  /**
   * Adds an assignment to the course and notifies observers.
   *
   * @param assignment the assignment to add
   */
  public void addAssignment(Assignment assignment) {
      if (!assignments.contains(assignment)) {
          assignments.add(assignment);
          notifyObservers();
      }
  }
  
  /**
   * Removes an assignment from the course and notifies observers.
   *
   * @param assignment the assignment to remove
   */

  public void removeAssignment(Assignment assignment) {
	    assignments.remove(assignment);
	    notifyObservers();
	}

  /**
   * Calculates the average grade across all students in the course.
   *
   * @return the class average as a percentage
   */  
  public double calculateClassAverage() {
      return students.stream()
          .mapToDouble(this::calculateStudentAverage)
          .average()
          .orElse(0.0);
  }

  /**
   * Calculates the average grade for a specific student.
   *
   * @param student the student whose average is being calculated
   * @return the average as a percentage
   */
  public double calculateStudentAverage(Student student) {
      if (useWeightedGrading) {
          return calculateWeightedAverage(student);
      } else {
          return calculateTotalPointsAverage(student);
      }
  }

  /**
   * Calculates the total points-based average for a student, applying drop rules.
   *
   * @param student the student whose total points average is calculated
   * @return the average as a percentage
   */
  private double calculateTotalPointsAverage(Student student) {
	    double totalEarned = 0;
	    double totalPossible = 0;

	    for (Category category : categories) {
	        List<Assignment> assignmentsInCat = category.getAssignments();
	        List<Grade> studentGrades = new ArrayList<>();

	        // Collect grades for this student in the current category
	        for (Assignment a : assignmentsInCat) {
	            Grade g = a.getGrade(student);
	            if (g != null) {
	                studentGrades.add(g);
	            }
	        }
	        // Drop lowest grades according to category rule
	        List<Grade> keptGrades = GradeCalculator.dropLowestGrades(studentGrades, category.getDropLowestCount());

	        // Accumulate earned and possible points
	        for (Grade g : keptGrades) {
	            totalEarned += g.getPointsReceived();
	            totalPossible += g.getMaxPoints();
	        }
	    }

	    return totalPossible > 0 ? (totalEarned / totalPossible) * 100.0 : 0.0;
	}



  /**
   * Calculates the weighted average for a student based on category weights.
   *
   * @param student the student whose weighted average is calculated
   * @return the weighted average percentage
   */
  private double calculateWeightedAverage(Student student) {
	    // Total weight of all categories must be non-zero
	    double totalWeight = categories.stream()
	        .mapToDouble(Category::getWeight)
	        .sum();

	    if (totalWeight == 0) return 0.0;

	    // Sum up each weighted category average
	    return categories.stream()
	        .mapToDouble(cat -> cat.calculateCategoryAverage(student))
	        .sum();
	}


  /**
   * Sorts students by last name, then by first name.
   *
   * @return list of sorted students
   */
  public List<Student> sortStudentsByName() {
	    // Comparator sorts by last name first, then first name
	    return students.stream()
	        .sorted(Comparator.comparing((Student s) -> s.getLastName())
	            .thenComparing(s -> s.getFirstName()))
	        .collect(Collectors.toList());
	}

  /**
   * Sorts students based on their grade in a specific assignment.
   *
   * @param assignment the assignment to sort by
   * @return list of students sorted by grade descending
   */

  public List<Student> sortStudentsByAssignmentGrade(Assignment assignment) {
	  // Higher grades appear first
      return students.stream()
          .sorted((s1, s2) -> Double.compare(
              assignment.getGrade(s2).getPercentage(),
              assignment.getGrade(s1).getPercentage()
          ))
          .collect(Collectors.toList());
  }

  /**
   * Gets the name of this course.
   *
   * @return course name
   */
  public String getCourseName() {
      return courseName;
  }

  /**
   * Gets a copy of the list of enrolled students.
   *
   * @return list of students
   */
  public List<Student> getStudents() {
      return new ArrayList<>(students);
  }

  /**
   * Gets a copy of the assignment list.
   *
   * @return list of assignments
   */
  public List<Assignment> getAssignments() {
      return new ArrayList<>(assignments);
  }

  /**
   * Sets the grading mode (true = weighted, false = total points).
   *
   * @param useWeightedGrading true to use category weighting
   */
  public void setGradingMode(boolean useWeightedGrading) {
      this.useWeightedGrading = useWeightedGrading;
  }

  /**
   * Sets how many lowest assignments to drop.
   *
   * @param numToDrop the number of assignments to drop
   */
  public void setAssignmentsToDrop(int numToDrop) {
      this.numAssignmentsToDrop = Math.max(numToDrop, 0);
  }

  /**
   * Gets all assignments that are not fully graded.
   *
   * @return list of ungraded assignments
   */
  public List<Assignment> getUngradedAssignments() {
	  // Filter assignments where not all students have grades
      return assignments.stream()
          .filter(a -> !a.isFullyGraded(students))
          .collect(Collectors.toList());
  }

  /**
   * Assigns a final letter grade to a student.
   *
   * @param student the student to assign the grade to
   * @param grade the final grade
   */
  public void assignFinalGrade(Student student, FinalGrade grade) {
	    finalGrades.put(student, grade);
	    student.assignFinalGrade(this, grade);  // Sync with student's record
	    notifyObservers(); // Update UI or dependent components
	}

  /**
   * Gets the final grade of a student.
   *
   * @param student the student to query
   * @return final grade or null
   */
  public FinalGrade getFinalGrade(Student student) {
      return finalGrades.getOrDefault(student, null);
  }
  
  /**
   * Checks if a student has completed the course.
   *
   * @param student the student to check
   * @return true if the student has a final grade
   */
  public boolean isCompleted(Student student) {
      return student.getFinalGrade(this) != null;
  }
  
  @Override
  public void addObserver(Observer o) {
      if (!observers.contains(o)) {
          observers.add(o);
      }
  }

  @Override
  public void removeObserver(Observer o) {
      observers.remove(o);
  }

  @Override
  public void notifyObservers() {
      for (Observer o : observers) {
          o.update();
      }
  }
  
  /**
   * Adds a category if total weight does not exceed 100%.
   *
   * @param category the category to add
   * @return true if added, false if weight exceeded
   */
	public boolean addCategory(Category category) {
	   double currentTotal = categories.stream()
	           .mapToDouble(Category::getWeight)
	           .sum();
	
	   if (currentTotal + category.getWeight() <= 1.0) {
	       categories.add(category);
	       notifyObservers(); // Update views on change
	       return true;
	   } else {
	       return false;
	   }
	}
	
	/**
     * Removes a category by name.
     *
     * @param name the name of the category
     * @return true if removed
     */
	public boolean removeCategory(String name) {
	   boolean removed = categories.removeIf(c -> c.getName().equalsIgnoreCase(name));
	   if (removed) {
	       notifyObservers(); // Update views on change
	   }
	   return removed;
	}
	
	/**
     * Gets the list of all categories in the course.
     *
     * @return list of categories
     */
	public List<Category> getCategories() {
	   return new ArrayList<>(categories);
	}
	
	/**
     * Returns how many assignments to drop.
     *
     * @return number of assignments to drop
     */
	public int getAssignmentsToDrop() {
	   return numAssignmentsToDrop;
	}
}