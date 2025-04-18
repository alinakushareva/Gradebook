package main.util;

import java.util.List;

import model.Category;
import model.FinalGrade;
import model.Grade;
import model.Student;

public class GradeCalculator {
	
	public static double calculateAverage(List<Grade> grades) {
		double sum = 0;
		double grade = 0;
		
		for(Grade g : grades) {
			grade = g.getPointsReceived() / g.getMaxPoints();
			sum += grade;
		}
		return sum / (double) grades.size();
	}
	
	public static double calculateMedian(List<Grade> grades) {
		return 0.0;
	}
	
	public static double calculateGPA(List<FinalGrade> finalGrades) {
		
		double sum = 0;
		
		for(FinalGrade f : finalGrades) {
			sum += f.getGpaValue();
		}
		return sum / (double) finalGrades.size();
	}
	
	public static FinalGrade getLetterGrade(double percentage) {
		if(percentage >= 90.0) {
			return FinalGrade.A;
		}
		else if(percentage >= 80.0) {
			return FinalGrade.B;
		}
		else if(percentage >= 70.0) {
			return FinalGrade.C;
		}
		else if(percentage >= 60.0) {
			return FinalGrade.D;
		}
		return FinalGrade.E;
	}
	
	public static double calculateWeightedAverage(List<Category> categories, Student student) {
		
		return 0.0;
	}
	
	public static List<Grade> dropLowestGrades(List<Grade> grades, int dropCount){
		return null;
	}
}
