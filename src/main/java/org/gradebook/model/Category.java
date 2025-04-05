package org.gradebook.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Represents a category of assignments for weighted grading.
 * Examples could be "Homework", "Quizzes", "Exams", etc.
 */
public class Category {
    private String name;
    private double weight;
    private List<Assignment> assignments;
    private int dropLowestCount;
    
    /**
     * Constructor for Category class.
     *
     * @param name   the category name
     * @param weight the weight of this category (0.0 to 1.0)
     */
    public Category(String name, double weight) {
        this.name = name;
        this.weight = weight;
        this.assignments = new ArrayList<>();
        this.dropLowestCount = 0;
    }
    
    /**
     * Gets the name of the category.
     *
     * @return the category name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the category.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the weight of the category.
     *
     * @return the weight (0.0 to 1.0)
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * Sets the weight of the category.
     *
     * @param weight the weight to set (0.0 to 1.0)
     */
    public void setWeight(double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0");
        }
        this.weight = weight;
    }
    
    /**
     * Gets the number of lowest grades to drop.
     *
     * @return the number of grades to drop
     */
    public int getDropLowestCount() {
        return dropLowestCount;
    }
    
    /**
     * Sets the number of lowest grades to drop.
     *
     * @param dropLowestCount the number of grades to drop
     */
    public void setDropLowestCount(int dropLowestCount) {
        if (dropLowestCount < 0) {
            throw new IllegalArgumentException("Drop count cannot be negative");
        }
        this.dropLowestCount = dropLowestCount;
    }
    
    /**
     * Adds an assignment to this category.
     *
     * @param assignment the assignment to add
     */
    public void addAssignment(Assignment assignment) {
        if (!assignments.contains(assignment)) {
            assignments.add(assignment);
        }
    }
    
    /**
     * Gets all assignments in this category.
     *
     * @return the list of assignments
     */
    public List<Assignment> getAssignments() {
        return new ArrayList<>(assignments);
    }
    
    /**
     * Calculates the average for this category for a specific student,
     * taking into account the dropped lowest grades.
     *
     * @param student the student
     * @return the category average, or -1 if no grades
     */
    public double calculateCategoryAverage(Student student) {
        List<Grade> grades = new ArrayList<>();
        
        // Collect all grades for the student in this category
        for (Assignment assignment : assignments) {
            Grade grade = assignment.getGrade(student);
            if (grade != null) {
                grades.add(grade);
            }
        }
        
        // If no grades, return -1
        if (grades.isEmpty()) {
            return -1.0;
        }
        
        // Sort grades by percentage (lowest first)
        List<Grade> sortedGrades = new ArrayList<>(grades);
        Collections.sort(sortedGrades, Comparator.comparingDouble(Grade::getPercentage));
        
        // Drop the lowest grades if applicable
        int toDrop = Math.min(dropLowestCount, sortedGrades.size() - 1);
        if (toDrop > 0) {
            sortedGrades = sortedGrades.subList(toDrop, sortedGrades.size());
        }
        
        // Calculate average of remaining grades
        double sum = 0.0;
        for (Grade grade : sortedGrades) {
            sum += grade.getPercentage();
        }
        
        return sum / sortedGrades.size();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", assignments=" + assignments.size() +
                ", dropLowestCount=" + dropLowestCount +
                '}';
    }
} 