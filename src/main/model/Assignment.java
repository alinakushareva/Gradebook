package main.model;

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
	
	public void assignGrade(Student s, double points) {
		
	}
	
	public Grade getGrade(Student s) {
		
	}
	
	public double getAverage() {
		
	}
	
	public boolean isGraded(Student s) {
		
	}
}
