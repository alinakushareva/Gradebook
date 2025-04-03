package test.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.model.FinalGrade;

class FinalGradeTest {

    // --- Constructor Tests ---
    @Test
    void constructor_SetsCorrectGpaValueForA() {
        assertEquals(4.0, FinalGrade.A.getGpaValue());
    }

    @Test
    void constructor_SetsCorrectGpaValueForB() {
        assertEquals(3.0, FinalGrade.B.getGpaValue());
    }

    @Test
    void constructor_SetsCorrectGpaValueForC() {
        assertEquals(2.0, FinalGrade.C.getGpaValue());
    }

    @Test
    void constructor_SetsCorrectGpaValueForD() {
        assertEquals(1.0, FinalGrade.D.getGpaValue());
    }

    @Test
    void constructor_SetsCorrectGpaValueForE() {
        assertEquals(0.0, FinalGrade.E.getGpaValue());
    }

    // --- Enum Values Tests ---
    @Test
    void values_ContainsAllExpectedGrades() {
        FinalGrade[] grades = FinalGrade.values();
        assertEquals(5, grades.length);
    }

    @Test
    void valueOf_ReturnsCorrectEnumForA() {
        assertSame(FinalGrade.A, FinalGrade.valueOf("A"));
    }

    @Test
    void valueOf_ReturnsCorrectEnumForE() {
        assertSame(FinalGrade.E, FinalGrade.valueOf("E"));
    }
}