package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Category {
    private final String name;
    private final double weight; // e.g., 0.25 for 25%
    private final List<Assignment> assignments;
    private int dropLowestCount;

    public Category(String name, double weight) {
        this.name = name;
        this.weight = weight;
        this.assignments = new ArrayList<>();
        this.dropLowestCount = 0;
    }

    /**
     * Calculates the student's average for this category,
     * after dropping the lowest grades based on dropLowestCount.
     */
    public double calculateCategoryAverage(Student student) {
        List<Grade> grades = assignments.stream()
            .map(a -> a.getGrade(student))
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingDouble(Grade::getPercentage))
            .collect(Collectors.toList());

        if (!grades.isEmpty() && dropLowestCount > 0) {
            int toDrop = Math.min(dropLowestCount, grades.size());
            grades = grades.subList(toDrop, grades.size());
        }

        double averagePercentage = grades.stream()
            .mapToDouble(Grade::getPercentage)
            .average()
            .orElse(0.0);

        return averagePercentage * weight; // Assumes weight is in decimal (e.g., 0.25 for 25%)
    }

    /**
     * Checks if a given assignment's grade is considered "dropped" for a student.
     */
    public boolean isDropped(Assignment assignment, Student student) {
        List<Grade> grades = assignments.stream()
            .map(a -> a.getGrade(student))
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingDouble(Grade::getPercentage))
            .collect(Collectors.toList());

        int toDrop = Math.min(dropLowestCount, grades.size());
        List<Grade> dropped = grades.subList(0, toDrop);
        Grade target = assignment.getGrade(student);
        return dropped.contains(target);
    }

    // Getters and setters
    public double getWeight() {
        return weight;
    }

    public void setDropLowestCount(int count, Course course) {
        this.dropLowestCount = count;
        course.notifyObservers(); // Trigger UI updates or recalculations
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public List<Assignment> getAssignments() {
        return new ArrayList<>(assignments);
    }

    public String getName() {
        return name;
    }

    public int getDropLowestCount() {
        return dropLowestCount;
    }
}
