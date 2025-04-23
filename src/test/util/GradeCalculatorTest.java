package util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Grade;
import model.FinalGrade;
import model.Category;
import model.Student;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class GradeCalculatorTest {

    @Test
    void testCalculateAverage_nullOrEmpty() {
        assertEquals(0.0, GradeCalculator.calculateAverage(null));
        assertEquals(0.0, GradeCalculator.calculateAverage(Collections.emptyList()));
    }

    @Test
    void testCalculateAverage_values() {
        List<Grade> grades = Arrays.asList(
            new Grade(8.0, 10.0),  // 80%
            new Grade(7.0, 10.0),  // 70%
            new Grade(5.0, 10.0)   // 50%
        );
        assertEquals((80.0 + 70.0 + 50.0) / 3, GradeCalculator.calculateAverage(grades), 1e-6);
    }

    @Test
    void testCalculateMedian_nullOrEmpty() {
        assertEquals(0.0, GradeCalculator.calculateMedian(null));
        assertEquals(0.0, GradeCalculator.calculateMedian(Collections.emptyList()));
    }

    @Test
    void testCalculateMedian_oddCount() {
        List<Grade> grades = Arrays.asList(
            new Grade(9.0, 10.0),  // 90%
            new Grade(7.0, 10.0),  // 70%
            new Grade(8.0, 10.0)   // 80%
        );
        assertEquals(80.0, GradeCalculator.calculateMedian(grades), 1e-6);
    }

    @Test
    void testCalculateMedian_evenCount() {
        List<Grade> grades = Arrays.asList(
            new Grade(4.0, 10.0),  // 40%
            new Grade(6.0, 10.0),  // 60%
            new Grade(8.0, 10.0),  // 80%
            new Grade(10.0, 10.0)  // 100%
        );
        assertEquals(70.0, GradeCalculator.calculateMedian(grades), 1e-6);
    }

    @Test
    void testCalculateGPA_nullOrEmpty() {
        assertEquals(0.0, GradeCalculator.calculateGPA(null));
        assertEquals(0.0, GradeCalculator.calculateGPA(Collections.emptyList()));
    }

    @Test
    void testCalculateGPA_values() {
        List<FinalGrade> finals = Arrays.asList(
            FinalGrade.A,  // 4.0
            FinalGrade.B,  // 3.0
            FinalGrade.C   // 2.0
        );
        assertEquals((4.0 + 3.0 + 2.0) / 3, GradeCalculator.calculateGPA(finals), 1e-6);
    }

    @Test
    void testGetLetterGrade_boundaries() {
        assertEquals(FinalGrade.A, GradeCalculator.getLetterGrade(90.0));
        assertEquals(FinalGrade.A, GradeCalculator.getLetterGrade(95.5));
        assertEquals(FinalGrade.B, GradeCalculator.getLetterGrade(80.0));
        assertEquals(FinalGrade.C, GradeCalculator.getLetterGrade(70.0));
        assertEquals(FinalGrade.D, GradeCalculator.getLetterGrade(60.0));
        assertEquals(FinalGrade.E, GradeCalculator.getLetterGrade(59.99));
    }

    @Test
    void testDropLowestGrades_nullAndDropCounts() {
        // null input
        List<Grade> resultNull = GradeCalculator.dropLowestGrades(null, 1);
        assertNotNull(resultNull);
        assertTrue(resultNull.isEmpty());

        // dropCount <= 0
        List<Grade> grades = Arrays.asList(
            new Grade(5.0, 10.0),
            new Grade(10.0, 10.0)
        );
        List<Grade> fullCopy = GradeCalculator.dropLowestGrades(grades, 0);
        assertEquals(2, fullCopy.size());

        // dropCount >= size
        List<Grade> empty = GradeCalculator.dropLowestGrades(grades, 5);
        assertTrue(empty.isEmpty());
    }

    @Test
    void testDropLowestGrades_actualDrop() {
        List<Grade> grades = Arrays.asList(
            new Grade(4.0, 10.0),
            new Grade(8.0, 10.0),
            new Grade(6.0, 10.0)
        );
        List<Grade> dropped = GradeCalculator.dropLowestGrades(grades, 1);
        assertEquals(2, dropped.size());
        assertEquals(60.0, dropped.get(0).getPercentage(), 1e-6);
        assertEquals(80.0, dropped.get(1).getPercentage(), 1e-6);
    }
    
    @Test
    void testNullCategoriesOrStudent() {
        Student student = new Student("First", "Last", "u1", "pwd");
        // Null categories
        assertEquals(0.0, GradeCalculator.calculateWeightedAverage(null, student));
        // Empty categories list
        assertEquals(0.0, GradeCalculator.calculateWeightedAverage(Collections.emptyList(), student));
        // Null student
        Category cat = new Category("cat", 1.0) {
            @Override public double calculateCategoryAverage(Student s) { return 42.0; }
        };
        assertEquals(0.0, GradeCalculator.calculateWeightedAverage(Arrays.asList(cat), null));
    }

    @Test
    void testZeroTotalWeight() {
        Student student = new Student("First", "Last", "u2", "pwd");
        Category zero = new Category("zero", 0.0) {
            @Override public double calculateCategoryAverage(Student s) { return 80.0; }
        };
        // weight 0 => totalWeight == 0 => expect 0.0
        assertEquals(0.0, GradeCalculator.calculateWeightedAverage(Arrays.asList(zero), student));
    }

    @Test
    void testSingleCategory() {
        Student student = new Student("First", "Last", "u3", "pwd");
        Category cat = new Category("single", 0.5) {
            @Override public double calculateCategoryAverage(Student s) { return 60.0; }
        };
        // With single category: (60 * 0.5) / 0.5 = 60
        assertEquals(60.0, GradeCalculator.calculateWeightedAverage(Arrays.asList(cat), student), 1e-6);
    }

    @Test
    void testMultipleCategories() {
        Student student = new Student("First", "Last", "u4", "pwd");
        Category c1 = new Category("c1", 0.2) {
            @Override public double calculateCategoryAverage(Student s) { return 50.0; }
        };
        Category c2 = new Category("c2", 0.8) {
            @Override public double calculateCategoryAverage(Student s) { return 90.0; }
        };
        List<Category> list = Arrays.asList(c1, c2);
        double weightedSum = c1.calculateCategoryAverage(student) * c1.getWeight()
                           + c2.calculateCategoryAverage(student) * c2.getWeight();
        double totalWeight = c1.getWeight() + c2.getWeight();
        double expected = weightedSum / totalWeight;
        assertEquals(expected, GradeCalculator.calculateWeightedAverage(list, student), 1e-6);
    }

    @Test
    void testEqualWeights() {
        Student student = new Student("First", "Last", "u5", "pwd");
        Category high = new Category("high", 1.0) {
            @Override public double calculateCategoryAverage(Student s) { return 100.0; }
        };
        Category low = new Category("low", 1.0) {
            @Override public double calculateCategoryAverage(Student s) { return 0.0; }
        };
        // (100*1 + 0*1) / (1+1) = 50
        assertEquals(50.0, GradeCalculator.calculateWeightedAverage(Arrays.asList(high, low), student), 1e-6);
    }
    
    
}
