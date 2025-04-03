package main.model;

import java.util.HashMap;
import java.util.Map;

public class Assignment {
	
	private String title;
	private double maxPoints;
	private String category;
	private Map<Student, Grade> studentGrades;
	
	public Assignment(String aName, double points, String category) {
		this.title = aName;
		this.maxPoints = points;
		this.category = category;
		
		this.studentGrades = new HashMap<Student, Grade>();
	}
	
//	public void assignGrade(Student s, double points) {
//		for(Student name : this.studentGrades.keySet()) {
//			if(name.equals(s)) {
//				this.studentGrades.put(s,points);
//			}
//		}
//	}
//	
//	public Grade getGrade(Student s) {
//		
//	}
	

	
//	public boolean isGraded(Student s) {
//		
//	}
}