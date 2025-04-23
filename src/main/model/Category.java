package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Category {
    private final String name;
    private final double weight;
    private final List<Assignment> assignments;
    private int dropLowestCount;

    public Category(String name, double weight) {
        this.name = name;
        this.weight = weight;
        this.assignments = new ArrayList<>();
        this.dropLowestCount = 0;
    }

    /**
     * Calculates the student's average for this category
     */
    public double calculateCategoryAverage(Student student) {
        List<Grade> grades = assignments.stream()
            .map(a -> a.getGrade(student))
            .sorted(Comparator.comparingDouble(Grade::getPercentage)) 
            .collect(Collectors.toList());

        // Drop lowest grades if configured
        if (dropLowestCount > 0 && !grades.isEmpty()) {
            grades = grades.subList(
                Math.min(dropLowestCount, grades.size()), 
                grades.size()
            );
        }

        return grades.stream()
            .mapToDouble(Grade::getPercentage)
            .average()
            .orElse(0.0) * weight;
    }


    // Getters and setters
    public double getWeight() { 
    	return weight; 
    }
    
    public void setDropLowestCount(int count) { 
    	dropLowestCount = count; 
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