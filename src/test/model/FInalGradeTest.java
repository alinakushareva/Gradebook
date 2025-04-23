package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


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
    
 // --- Letter Grade Logic Tests ---
    @Test
    void getLetterGrade_ReturnsA_For90AndAbove() {
        assertEquals(FinalGrade.A, FinalGrade.getLetterGrade(90.0));
        assertEquals(FinalGrade.A, FinalGrade.getLetterGrade(95.5));
        assertEquals(FinalGrade.A, FinalGrade.getLetterGrade(100.0));
    }

    @Test
    void getLetterGrade_ReturnsB_For80To89_99() {
        assertEquals(FinalGrade.B, FinalGrade.getLetterGrade(80.0));
        assertEquals(FinalGrade.B, FinalGrade.getLetterGrade(88.9));
    }

    @Test
    void getLetterGrade_ReturnsC_For70To79_99() {
        assertEquals(FinalGrade.C, FinalGrade.getLetterGrade(70.0));
        assertEquals(FinalGrade.C, FinalGrade.getLetterGrade(75.0));
    }

    @Test
    void getLetterGrade_ReturnsD_For60To69_99() {
        assertEquals(FinalGrade.D, FinalGrade.getLetterGrade(60.0));
        assertEquals(FinalGrade.D, FinalGrade.getLetterGrade(65.4));
    }

    @Test
    void getLetterGrade_ReturnsE_ForBelow60() {
        assertEquals(FinalGrade.E, FinalGrade.getLetterGrade(59.9));
        assertEquals(FinalGrade.E, FinalGrade.getLetterGrade(0.0));
    }

}