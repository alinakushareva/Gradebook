/**
 * Project Name: Gradebook
 * File Name: Category.java
 * Course: CSC 335 Spring 2025
 * Purpose: Represents a category of assignments (e.g., Homework, Exams) in a course.
 *          Supports category weighting and dropping lowest grades for students.
 */
package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Category {
    private final String name;
    private final double weight; 
    private final List<Assignment> assignments;
    private int dropLowestCount;

    /**
     * Constructs a category with a given name and weight.
     * @param name the name of the category (e.g., "Homework")
     * @param weight the weight of this category as a decimal (e.g., 0.25 for 25%)
     */
    public Category(String name, double weight) {
        this.name = name;
        this.weight = weight;
        this.assignments = new ArrayList<>();
        this.dropLowestCount = 0;
    }

    /**
     * Calculates the weighted average percentage for a student in this category.
     * Drops the lowest grades based on dropLowestCount.
     * @param student the student whose average to calculate
     * @return weighted average percentage
     */

    public double calculateCategoryAverage(Student student) {
        List<Grade> grades = assignments.stream()
            .map(a -> a.getGrade(student)) // Get grades for this student
            .filter(Objects::nonNull) // Remove nulls (ungraded)
            .sorted(Comparator.comparingDouble(Grade::getPercentage)) // Sort from lowest to highest
            .collect(Collectors.toList());

        if (!grades.isEmpty() && dropLowestCount > 0) {
            // Drop the lowest N grades
            int toDrop = Math.min(dropLowestCount, grades.size());
            grades = grades.subList(toDrop, grades.size());
        }

        // Compute average and apply category weight
        double averagePercentage = grades.stream()
            .mapToDouble(Grade::getPercentage)
            .average()
            .orElse(0.0);

        return averagePercentage * weight; // Assumes weight is in decimal (e.g., 0.25 for 25%)
    }

    /**
     * Checks if a given assignment's grade is dropped for a student.
     * @param assignment the assignment to check
     * @param student the student being evaluated
     * @return true if the grade is considered dropped, false otherwise
     */
    public boolean isDropped(Assignment assignment, Student student) {
        List<Grade> grades = assignments.stream()
            .map(a -> a.getGrade(student))
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingDouble(Grade::getPercentage))
            .collect(Collectors.toList());

        int toDrop = Math.min(dropLowestCount, grades.size());
        List<Grade> dropped = grades.subList(0, toDrop); // Get lowest N grades
        Grade target = assignment.getGrade(student);
        // Check if this assignment's grade was dropped
        return dropped.contains(target);
    }

    /**
     * Gets the weight of the category.
     * @return weight as a decimal
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * Sets how many lowest grades to drop for this category and notifies the course.
     * @param count number of lowest grades to drop
     * @param course the course this category belongs to
     */
    public void setDropLowestCount(int count, Course course) {
        this.dropLowestCount = count;
        course.notifyObservers(); // Trigger UI updates or recalculations
    }

    /**
     * Adds an assignment to this category.
     * @param assignment the assignment to include
     */
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    /**
     * Gets all assignments in this category.
     * @return list of Assignment objects
     */
    public List<Assignment> getAssignments() {
        return new ArrayList<>(assignments);
    }

    /**
     * Gets the name of the category.
     * @return category name (e.g., "Exams")
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the number of lowest grades to drop.
     * @return integer count
     */
    public int getDropLowestCount() {
        return dropLowestCount;
    }
}