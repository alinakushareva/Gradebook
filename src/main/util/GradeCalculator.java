/**
 * Project Name: Gradebook
 * File Name: GradeCalculator.java
 * Course: CSC 335 Spring 2025
 * Purpose: Utility class providing static methods to calculate averages, medians, GPA,
 *          letter grades, weighted averages, and dropping lowest grades as specified
 *          in the project requirements.
 */
package util;

import model.Grade;
import model.FinalGrade;
import model.Category;
import model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GradeCalculator {
	
	/**
     * Calculates the average percentage across a list of grades.
     * @param grades List of Grade objects
     * @return average percentage, or 0.0 if empty
     */
    public static double calculateAverage(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Grade grade : grades) {
        	// Add each grade's percentage to the sum

            sum += grade.getPercentage();
        }
        // Return average by dividing total by number of grades
        return sum / grades.size();
    }

    /**
     * Calculates the median percentage from a list of grades.
     * @param grades List of Grade objects
     * @return median percentage, or 0.0 if empty
     */

    public static double calculateMedian(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        List<Double> values = new ArrayList<>();
        for (Grade grade : grades) {
        	// Extract percentage values
            values.add(grade.getPercentage());
        }
        Collections.sort(values); // Sort in ascending order
        int size = values.size();
        
        // Return middle value or average of two middles
        if (size % 2 == 1) {
            return values.get(size / 2);
        } else {
            double lower = values.get(size / 2 - 1);
            double upper = values.get(size / 2);
            return (lower + upper) / 2.0;
        }
    }

    /**
     * Calculates GPA based on a list of final letter grades.
     * @param finalGrades List of FinalGrade enums
     * @return GPA value out of 4.0
     */

    public static double calculateGPA(List<FinalGrade> finalGrades) {
        if (finalGrades == null || finalGrades.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (FinalGrade grade : finalGrades) {
        	// Add corresponding GPA value
            total += grade.getGpaValue();
        }
        return total / finalGrades.size(); // Return average GPA
    }

    /**
     * Converts a numeric percentage to a letter grade.
     * @param percentage score between 0–100
     * @return FinalGrade enum (A–E)
     */
    public static FinalGrade getLetterGrade(double percentage) {
        if (percentage >= 90.0) {
            return FinalGrade.A;
        } else if (percentage >= 80.0) {
            return FinalGrade.B;
        } else if (percentage >= 70.0) {
            return FinalGrade.C;
        } else if (percentage >= 60.0) {
            return FinalGrade.D;
        } else {
            return FinalGrade.E;
        }
    }

    /**
     * Computes a weighted average across all categories for a student.
     * @param categories List of assignment categories
     * @param student Student whose grades are used
     * @return weighted percentage average
     */
    public static double calculateWeightedAverage(List<Category> categories, Student student) {
        if (categories == null || categories.isEmpty() || student == null) {
            return 0.0;
        }
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        for (Category category : categories) {
            double weight = category.getWeight();
            // Get average per category
            double avg = category.calculateCategoryAverage(student);
            weightedSum += avg * weight; // Multiply by weight and accumulate
            totalWeight += weight; // Track total weights used
        }
        // Return weighted average if weight is non-zero
        if (totalWeight > 0) {
            return weightedSum / totalWeight;
        } else {
            return 0.0;
        }
    }

    /**
     * Drops the lowest-scoring grades from a list based on the drop count.
     * @param grades List of Grade objects
     * @param dropCount Number of lowest grades to drop
     * @return List of remaining grades after drops
     */
    public static List<Grade> dropLowestGrades(List<Grade> grades, int dropCount) {
        if (grades == null) {
            return new ArrayList<>();
        }
        if (dropCount <= 0) {
        	// Return all grades if nothing to drop
            return new ArrayList<>(grades);
        }
        List<Grade> sorted = new ArrayList<>(grades);
        // Sort grades in ascending order of percentage
        Collections.sort(sorted, new Comparator<Grade>() {
            @Override
            public int compare(Grade g1, Grade g2) {
                return Double.compare(g1.getPercentage(), g2.getPercentage());
            }
        });
        // If dropping all or more, return empty list
        if (dropCount >= sorted.size()) {
            return new ArrayList<>();
        }
        // Return list after dropping the lowest scores
        return new ArrayList<>(sorted.subList(dropCount, sorted.size()));
    }
}
