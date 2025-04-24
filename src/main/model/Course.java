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
          notifyObservers();
      }
  }

  public void removeStudent(Student student) {
      students.remove(student);
      student.getCourses().remove(this);
      notifyObservers();
  }

  public void addAssignment(Assignment assignment) {
      if (!assignments.contains(assignment)) {
          assignments.add(assignment);
          notifyObservers();
      }
  }
  
  public void removeAssignment(Assignment assignment) {
	    assignments.remove(assignment);
	    notifyObservers();
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
	    double totalEarned = 0;
	    double totalPossible = 0;

	    for (Category category : categories) {
	        List<Assignment> assignmentsInCat = category.getAssignments();
	        List<Grade> studentGrades = new ArrayList<>();

	        for (Assignment a : assignmentsInCat) {
	            Grade g = a.getGrade(student);
	            if (g != null) {
	                studentGrades.add(g);
	            }
	        }
	        List<Grade> keptGrades = GradeCalculator.dropLowestGrades(studentGrades, category.getDropLowestCount());

	        for (Grade g : keptGrades) {
	            totalEarned += g.getPointsReceived();
	            totalPossible += g.getMaxPoints();
	        }
	    }

	    return totalPossible > 0 ? (totalEarned / totalPossible) * 100.0 : 0.0;
	}



  private double calculateWeightedAverage(Student student) {
	    double totalWeight = categories.stream()
	        .mapToDouble(Category::getWeight)
	        .sum();

	    if (totalWeight == 0) return 0.0;

	    return categories.stream()
	        .mapToDouble(cat -> cat.calculateCategoryAverage(student))
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
	    student.assignFinalGrade(this, grade); 
	    notifyObservers();
	}


  public FinalGrade getFinalGrade(Student student) {
      return finalGrades.getOrDefault(student, null);
  }
  
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
  
	//Category Management
	
	public boolean addCategory(Category category) {
	   double currentTotal = categories.stream()
	           .mapToDouble(Category::getWeight)
	           .sum();
	
	   if (currentTotal + category.getWeight() <= 1.0) {
	       categories.add(category);
	       notifyObservers();
	       return true;
	   } else {
	       return false;
	   }
	}
	
	public boolean removeCategory(String name) {
	   boolean removed = categories.removeIf(c -> c.getName().equalsIgnoreCase(name));
	   if (removed) {
	       notifyObservers();
	   }
	   return removed;
	}
	
	public List<Category> getCategories() {
	   return new ArrayList<>(categories);
	}
	
	public int getAssignmentsToDrop() {
	   return numAssignmentsToDrop;
	}
}