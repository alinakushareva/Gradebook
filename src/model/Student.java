package model;

import java.util.HashMap;
import java.util.Map;

/*
 * This class represents a student and their grades.
 */
public class Student extends User{
	/* 
	 * Student inherits everything from User
	 * but can also include more specific Student-related
	 * data and methods which extends the User interface
	 */
	
	private Map<Assignment, Grade> grades; //Tacks grades per assignment
	private Map<Course, FinalGrade> finalGrades; //Final letter grades per course
	
	private Assignment assignment;
	private Grade grade;
	private Course course;
	private FinalGrade finalG;
	
	private final String role;
	
	public Student(String firstName, String lastName, String userName, String password) {
		role = "Student";
		
		// since Student extends User, calls to the User constructor to create the User object
		super(userName, firstName, lastName, password, role);
		
		assignment = new Assignment();
		grade = new Grade();
		course = new Course();
		finalG = new FinalGrade();
		
		// initializes grades and finalGrades
		this.grades = new HashMap<Assignment, Grade>();
		this.finalGrades = new HashMap<Course, FinalGrade>();
		
	}
	
	public void addGrade(Assignment a, Grade g) {
		/*
		 * Assignment class attributes:
		 * 	string title
		 * 	double maxPoints
		 * 	String category - tied to grading category (if applicable)
		 * 	Map<Student, Grade> studentGrades;
		 * 
		 * Assignment Class Methods:
		 * 	void assignGrade(Student s, double points)
		 * 	Grade getGrad(Student s)
		 * 	double getAvg()
		 * 	boolean isGraded(Student s)
		 * 
		 * 
		 * Grade class attributes:
		 * 	double pointsReceived
		 * 	double maxPoints
		 * 
		 * Grade class methods:
		 * 	Grade(double pointsReceived, double maxPoints)
		 * 	double getPercentage()
		 * 	double getPointsReceived()
		 * 	double getMaxPoints()
		 */
		
		
	}
	
	public Grade getGrade(Assignment a) {
		/*
		 * Assignment class attributes:
		 * 	string title
		 * 	double maxPoints
		 * 	String category - tied to grading category (if applicable)
		 * 	Map<Student, Grade> studentGrades;
		 * 
		 * Assignment Class Methods:
		 * 	void assignGrade(Student s, double points)
		 * 	Grade getGrad(Student s)
		 * 	double getAvg()
		 * 	boolean isGraded(Student s)
		 */
		
	}
	
	public double getAvgForCourse(Course c) {
		/*
		 * Course class attributes:
		 * 	String courseName
		 * 	List<Student> students
		 * 	List<Assignment> assignments
		 * 	List<Category> categories - optional, for weighted grading
		 * 	Map<Student, FinalGrade> finalGrades
		 * 	boolean useWeightedGrading
		 * 	int numAssignmentsToDrop
		 * 
		 * Course class methods:
		 * 	void addStudent(Student student)
		 * 	void removeStudent(Student student)
		 * 	void addAssignment(Assignment assignment)
		 * 	void assignFinalGrade(Student s, FinalGrade grade)
		 * 	List<Assignment> getUngradedAssignments()
		 * 	double calculateMedianForAssignment(Assignment a)
		 * 	double calculateClassAvg()
		 * 	double calculateStudentAvg(Student s)
		 * 	List<Student> sortStudentsByName()
		 * 	List<Student> sortStudentsByGrade(Assignment a)
		 * 	void groupStudents(List<List<Student>> groups) (optional)
		 */
		
	}
	
	public double calculateGPA() {
		
	}
	
	public void assignFinalGrade(Course c, FinalGrade g) {
		/*
		 * FinalGrade class:
		 * 	Values - A(4.0), B(3.0), C(2.0), D(1.0), E(0.0)
		 * 	
		 * FinalGrade class attributes:
		 * 	double gpaValue - used to calculate GPA
		 * 
		 * FinalGrade class methods:
		 * 	double getGpaValue()

		 * Course class attributes:
		 * 	String courseName
		 * 	List<Student> students
		 * 	List<Assignment> assignments
		 * 	List<Category> categories - optional, for weighted grading
		 * 	Map<Student, FinalGrade> finalGrades
		 * 	boolean useWeightedGrading
		 * 	int numAssignmentsToDrop
		 * 
		 * Course class methods:
		 * 	void addStudent(Student student)
		 * 	void removeStudent(Student student)
		 * 	void addAssignment(Assignment assignment)
		 * 	void assignFinalGrade(Student s, FinalGrade grade)
		 * 	List<Assignment> getUngradedAssignments()
		 * 	double calculateMedianForAssignment(Assignment a)
		 * 	double calculateClassAvg()
		 * 	double calculateStudentAvg(Student s)
		 * 	List<Student> sortStudentsByName()
		 * 	List<Student> sortStudentsByGrade(Assignment a)
		 * 	void groupStudents(List<List<Student>> groups) (optional)
		 */
	}
	
	public FinalGrade getFinalGrade(Course c) {
		/*
		 * Course class attributes:
		 * 	String courseName
		 * 	List<Student> students
		 * 	List<Assignment> assignments
		 * 	List<Category> categories - optional, for weighted grading
		 * 	Map<Student, FinalGrade> finalGrades
		 * 	boolean useWeightedGrading
		 * 	int numAssignmentsToDrop
		 * 
		 * Course class methods:
		 * 	void addStudent(Student student)
		 * 	void removeStudent(Student student)
		 * 	void addAssignment(Assignment assignment)
		 * 	void assignFinalGrade(Student s, FinalGrade grade)
		 * 	List<Assignment> getUngradedAssignments()
		 * 	double calculateMedianForAssignment(Assignment a)
		 * 	double calculateClassAvg()
		 * 	double calculateStudentAvg(Student s)
		 * 	List<Student> sortStudentsByName()
		 * 	List<Student> sortStudentsByGrade(Assignment a)
		 * 	void groupStudents(List<List<Student>> groups) (optional)
		*/
	
	}
}
