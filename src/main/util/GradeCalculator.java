package util;

import model.Grade;
import model.FinalGrade;
import model.Category;
import model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class providing static methods to calculate averages, medians, GPA,
 * letter grades, weighted averages, and dropping lowest grades as specified in the project spec.
 */
public class GradeCalculator {
    /**
     * Calculates the average percentage across a list of grades.
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
     * Calculates the median percentage of a list of grades.
     */
    public static double calculateMedian(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        List<Double> values = new ArrayList<>();
        for (Grade grade : grades) {
            values.add(grade.getPercentage());
        }
        Collections.sort(values);
        int size = values.size();
        if (size % 2 == 1) {
            return values.get(size / 2);
        } else {
            double lower = values.get(size / 2 - 1);
            double upper = values.get(size / 2);
            return (lower + upper) / 2.0;
        }
    }

    /**
     * Calculates GPA based on a list of final grades.
     */
    public static double calculateGPA(List<FinalGrade> finalGrades) {
        if (finalGrades == null || finalGrades.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (FinalGrade grade : finalGrades) {
            total += grade.getGpaValue();
        }
        return total / finalGrades.size();
    }

    /**
     * Maps a percentage to a letter grade enum.
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
     * Calculates a weighted average across categories for a given student.
     */
    public static double calculateWeightedAverage(List<Category> categories, Student student) {
        if (categories == null || categories.isEmpty() || student == null) {
            return 0.0;
        }
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        for (Category category : categories) {
            double weight = category.getWeight();
            double avg = category.calculateCategoryAverage(student);
            weightedSum += avg * weight;
            totalWeight += weight;
        }
        if (totalWeight > 0) {
            return weightedSum / totalWeight;
        } else {
            return 0.0;
        }
    }

    /**
     * Returns a list of grades after dropping the specified number of lowest grades.
     */
    public static List<Grade> dropLowestGrades(List<Grade> grades, int dropCount) {
        if (grades == null) {
            return new ArrayList<>();
        }
        if (dropCount <= 0) {
            return new ArrayList<>(grades);
        }
        List<Grade> sorted = new ArrayList<>(grades);
        Collections.sort(sorted, new Comparator<Grade>() {
            @Override
            public int compare(Grade g1, Grade g2) {
                return Double.compare(g1.getPercentage(), g2.getPercentage());
            }
        });
        if (dropCount >= sorted.size()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(sorted.subList(dropCount, sorted.size()));
    }
}
