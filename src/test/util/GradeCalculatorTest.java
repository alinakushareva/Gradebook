package util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Grade;

class GradeCalculatorTest {
	
	List<Grade> grades = new ArrayList<Grade>();


	@BeforeEach
	void setUp() {
		Grade g1 = new Grade(10.0, 25.0);
		Grade g2 = new Grade(15.0, 25.0);
		Grade g3 = new Grade(75.0, 100.0);
		
		grades.add(g1);
		grades.add(g2);
		grades.add(g3);
		
	}
	
	@Test
	void calculateAvgTest() {
		GradeCalculator gc = new GradeCalculator();
		
		assertEquals(0.58333,gc.calculateAverage(grades));
	}
}
