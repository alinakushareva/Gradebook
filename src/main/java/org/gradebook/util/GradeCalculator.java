package org.gradebook.util;

import org.gradebook.model.Category;
import org.gradebook.model.FinalGrade;
import org.gradebook.model.Grade;
import org.gradebook.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for grade calculation operations.
 */
public class GradeCalculator {
    
    /**
     * Calculates the average of a list of grades.
     *
     * @param grades the list of grades
     * @return the average percentage, or 0 if the list is empty
     */
    public static double calculateAverage(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (Grade grade : grades) {
            sum += grade.getPercentage();
        }
        
        return sum / grades.size();
    }
    
    /**
     * Calculates the median of a list of grades.
     *
     * @param grades the list of grades
     * @return the median percentage, or 0 if the list is empty
     */
    public static double calculateMedian(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        
        List<Grade> sortedGrades = new ArrayList<>(grades);
        Collections.sort(sortedGrades, Comparator.comparingDouble(Grade::getPercentage));
        
        int size = sortedGrades.size();
        if (size % 2 == 0) {
            // Average of the two middle values
            return (sortedGrades.get(size / 2 - 1).getPercentage() +
                    sortedGrades.get(size / 2).getPercentage()) / 2.0;
        } else {
            // Middle value
            return sortedGrades.get(size / 2).getPercentage();
        }
    }
    
    /**
     * Calculates the GPA from a list of final grades.
     *
     * @param finalGrades the list of final grades
     * @return the GPA, or 0 if the list is empty
     */
    public static double calculateGPA(List<FinalGrade> finalGrades) {
        if (finalGrades == null || finalGrades.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (FinalGrade grade : finalGrades) {
            sum += grade.getGpaValue();
        }
        
        return sum / finalGrades.size();
    }
    
    /**
     * Converts a percentage to a letter grade.
     *
     * @param percentage the percentage (0-100)
     * @return the corresponding final grade
     */
    public static FinalGrade getLetterGrade(double percentage) {
        return FinalGrade.fromPercentage(percentage);
    }
    
    /**
     * Calculates a weighted average based on categories.
     *
     * @param categories the list of categories
     * @param student    the student
     * @return the weighted average, or -1 if no valid categories
     */
    public static double calculateWeightedAverage(List<Category> categories, Student student) {
        if (categories == null || categories.isEmpty()) {
            return -1.0;
        }
        
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (Category category : categories) {
            double categoryAverage = category.calculateCategoryAverage(student);
            if (categoryAverage >= 0) {
                weightedSum += categoryAverage * category.getWeight();
                totalWeight += category.getWeight();
            }
        }
        
        return totalWeight > 0 ? weightedSum / totalWeight : -1.0;
    }
    
    /**
     * Drops the lowest grades from a list of grades.
     *
     * @param grades    the list of grades
     * @param dropCount the number of grades to drop
     * @return a new list with the lowest grades removed
     */
    public static List<Grade> dropLowestGrades(List<Grade> grades, int dropCount) {
        if (grades == null || grades.isEmpty() || dropCount <= 0 || dropCount >= grades.size()) {
            return new ArrayList<>(grades);
        }
        
        List<Grade> sortedGrades = new ArrayList<>(grades);
        Collections.sort(sortedGrades, Comparator.comparingDouble(Grade::getPercentage));
        
        return sortedGrades.subList(dropCount, sortedGrades.size());
    }
} 