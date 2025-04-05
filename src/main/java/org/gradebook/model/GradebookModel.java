package org.gradebook.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.io.IOException;
import org.gradebook.util.FileUtil;

/**
 * The central model for the gradebook system.
 * Manages courses, users, and their relationships.
 * Implements Observable for MVC pattern.
 */
public class GradebookModel extends Observable {
    private Map<String, Student> students;
    private Map<String, Teacher> teachers;
    private Map<String, Course> courses;
    private UserManager userManager;
    
    private static final String COURSES_FILE = "data/courses.txt";
    private static final String ASSIGNMENTS_FILE = "data/assignments.txt";
    private static final String GRADES_FILE = "data/grades.txt";
    private static final String ENROLLMENTS_FILE = "data/enrollments.txt";
    private static final String USERS_FILE = "data/users.txt";
    private static final String TEACHER_COURSES_FILE = "data/teacher_courses.txt";
    
    /**
     * Constructor for GradebookModel class.
     */
    public GradebookModel() {
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.courses = new HashMap<>();
        this.userManager = new UserManager(USERS_FILE);
        
        // Initialize collections from user manager
        for (Student student : userManager.getAllStudents()) {
            students.put(student.getUsername(), student);
        }
        
        for (Teacher teacher : userManager.getAllTeachers()) {
            teachers.put(teacher.getUsername(), teacher);
        }
    }
    
    /**
     * Adds a student to the system.
     *
     * @param student the student to add
     */
    public void addStudent(Student student) {
        students.put(student.getUsername(), student);
        setChanged();
        notifyObservers("student_added");
    }
    
    /**
     * Adds a teacher to the system.
     *
     * @param teacher the teacher to add
     */
    public void addTeacher(Teacher teacher) {
        teachers.put(teacher.getUsername(), teacher);
        setChanged();
        notifyObservers("teacher_added");
    }
    
    /**
     * Adds a course to the system.
     *
     * @param course the course to add
     */
    public void addCourse(Course course) {
        courses.put(course.getCourseName(), course);
        setChanged();
        notifyObservers("course_added");
    }
    
    /**
     * Gets a student by username.
     *
     * @param username the username to look up
     * @return the student, or null if not found
     */
    public Student getStudentByUsername(String username) {
        return students.get(username);
    }
    
    /**
     * Gets a teacher by username.
     *
     * @param username the username to look up
     * @return the teacher, or null if not found
     */
    public Teacher getTeacherByUsername(String username) {
        return teachers.get(username);
    }
    
    /**
     * Gets a course by name.
     *
     * @param name the course name to look up
     * @return the course, or null if not found
     */
    public Course getCourseByName(String name) {
        return courses.get(name);
    }
    
    /**
     * Gets all courses in the system.
     *
     * @return a list of all courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    /**
     * Checks if a student exists in the system.
     *
     * @param username the username to check
     * @return true if the student exists, false otherwise
     */
    public boolean studentExists(String username) {
        return students.containsKey(username);
    }
    
    /**
     * Checks if a teacher exists in the system.
     *
     * @param username the username to check
     * @return true if the teacher exists, false otherwise
     */
    public boolean teacherExists(String username) {
        return teachers.containsKey(username);
    }
    
    /**
     * Gets the user manager.
     *
     * @return the user manager
     */
    public UserManager getUserManager() {
        return userManager;
    }
    
    /**
     * Gets all students in the system.
     *
     * @return a list of all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    /**
     * Gets all teachers in the system.
     *
     * @return a list of all teachers
     */
    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers.values());
    }
    
    /**
     * Registers a new user in the system.
     *
     * @param username  the username
     * @param firstName the first name
     * @param lastName  the last name
     * @param password  the plain text password
     * @param role      the role ("student" or "teacher")
     * @return the created user, or null if registration failed
     */
    public User registerUser(String username, String firstName, String lastName, String password, String role) {
        User user = userManager.registerUser(username, firstName, lastName, password, role);
        
        if (user != null) {
            if (user instanceof Student) {
                students.put(username, (Student) user);
                setChanged();
                notifyObservers("student_added");
            } else if (user instanceof Teacher) {
                teachers.put(username, (Teacher) user);
                setChanged();
                notifyObservers("teacher_added");
            }
        }
        
        return user;
    }
    
    /**
     * Attempts to log in a user with the given credentials.
     *
     * @param username the username
     * @param password the plain text password
     * @return the user if login successful, null otherwise
     */
    public User login(String username, String password) {
        return userManager.login(username, password);
    }
    
    /**
     * Creates a new course and adds it to the system.
     *
     * @param courseName the course name
     * @param teacher    the teacher for the course
     * @return the created course
     */
    public Course createCourse(String courseName, Teacher teacher) {
        if (courses.containsKey(courseName)) {
            return null; // Course already exists
        }
        
        Course course = new Course(courseName);
        courses.put(courseName, course);
        
        if (teacher != null) {
            teacher.addCourse(course);
        }
        
        setChanged();
        notifyObservers("course_added");
        
        return course;
    }
    
    /**
     * Enrolls a student in a course.
     *
     * @param student the student
     * @param course  the course
     */
    public void enrollStudent(Student student, Course course) {
        course.addStudent(student);
        setChanged();
        notifyObservers("student_enrolled");
    }

    /**
     * Saves all data to files.
     */
    public void saveAllData() {
        try {
            saveCoursesToFile();
            saveAssignmentsToFile();
            saveGradesToFile();
            saveEnrollmentsToFile();
            saveTeacherCoursesToFile();
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Loads all data from files.
     */
    public void loadAllData() {
        try {
            loadCoursesFromFile();
            loadAssignmentsFromFile();
            loadGradesFromFile();
            loadEnrollmentsFromFile();
            loadTeacherCoursesFromFile();
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    /**
     * Saves courses to file.
     */
    private void saveCoursesToFile() throws IOException {
        List<String> lines = new ArrayList<>();
        
        for (Course course : courses.values()) {
            String line = String.format("%s,%s,%d",
                    course.getCourseName(),
                    course.isUseWeightedGrading() ? "1" : "0",
                    course.getNumAssignmentsToDrop());
            lines.add(line);
        }
        
        FileUtil.writeLines(COURSES_FILE, lines);
    }

    /**
     * Loads courses from file.
     */
    private void loadCoursesFromFile() throws IOException {
        List<String> lines = FileUtil.readLines(COURSES_FILE);
        courses.clear();
        
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String courseName = parts[0];
                boolean useWeightedGrading = "1".equals(parts[1]);
                int numAssignmentsToDrop = Integer.parseInt(parts[2]);
                
                Course course = new Course(courseName);
                course.setUseWeightedGrading(useWeightedGrading);
                course.setNumAssignmentsToDrop(numAssignmentsToDrop);
                
                courses.put(courseName, course);
            }
        }
    }

    /**
     * Saves assignments to file.
     */
    private void saveAssignmentsToFile() throws IOException {
        List<String> lines = new ArrayList<>();
        
        for (Course course : courses.values()) {
            for (Assignment assignment : course.getAssignments()) {
                String line = String.format("%s,%s,%.2f,%s",
                        course.getCourseName(),
                        assignment.getTitle(),
                        assignment.getMaxPoints(),
                        assignment.getCategory() != null ? assignment.getCategory() : "");
                lines.add(line);
            }
        }
        
        FileUtil.writeLines(ASSIGNMENTS_FILE, lines);
    }

    /**
     * Loads assignments from file.
     */
    private void loadAssignmentsFromFile() throws IOException {
        List<String> lines = FileUtil.readLines(ASSIGNMENTS_FILE);
        
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                String courseName = parts[0];
                String title = parts[1];
                double maxPoints = Double.parseDouble(parts[2]);
                String category = parts[3].isEmpty() ? null : parts[3];
                
                Course course = getCourseByName(courseName);
                if (course != null) {
                    Assignment assignment = new Assignment(title, maxPoints, category);
                    course.addAssignment(assignment);
                }
            }
        }
    }

    /**
     * Saves grades to file.
     */
    private void saveGradesToFile() throws IOException {
        List<String> lines = new ArrayList<>();
        
        for (Course course : courses.values()) {
            for (Student student : course.getStudents()) {
                // Save assignment grades
                for (Assignment assignment : course.getAssignments()) {
                    Grade grade = assignment.getGrade(student);
                    if (grade != null) {
                        String line = String.format("A,%s,%s,%s,%.2f",
                                course.getCourseName(),
                                student.getUsername(),
                                assignment.getTitle(),
                                grade.getPointsReceived());
                        lines.add(line);
                    }
                }
                
                // Save final grades
                FinalGrade finalGrade = course.getFinalGrade(student);
                if (finalGrade != null) {
                    String line = String.format("F,%s,%s,%s",
                            course.getCourseName(),
                            student.getUsername(),
                            finalGrade.name());
                    lines.add(line);
                }
            }
        }
        
        FileUtil.writeLines(GRADES_FILE, lines);
    }

    /**
     * Loads grades from file.
     */
    private void loadGradesFromFile() throws IOException {
        List<String> lines = FileUtil.readLines(GRADES_FILE);
        
        for (String line : lines) {
            String[] parts = line.split(",");
            
            if (parts.length >= 4) {
                String type = parts[0];
                String courseName = parts[1];
                String username = parts[2];
                
                Course course = getCourseByName(courseName);
                Student student = getStudentByUsername(username);
                
                if (course != null && student != null) {
                    if ("A".equals(type) && parts.length >= 5) {
                        // Assignment grade
                        String assignmentTitle = parts[3];
                        double pointsReceived = Double.parseDouble(parts[4]);
                        
                        Assignment assignment = getAssignmentByTitle(course, assignmentTitle);
                        if (assignment != null) {
                            assignment.assignGrade(student, pointsReceived);
                        }
                    } else if ("F".equals(type)) {
                        // Final grade
                        String gradeName = parts[3];
                        try {
                            FinalGrade finalGrade = FinalGrade.valueOf(gradeName);
                            course.assignFinalGrade(student, finalGrade);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid final grade value: " + gradeName);
                        }
                    }
                }
            }
        }
    }

    /**
     * Saves enrollments (student-course relationships) to file.
     */
    private void saveEnrollmentsToFile() throws IOException {
        List<String> lines = new ArrayList<>();
        
        for (Course course : courses.values()) {
            for (Student student : course.getStudents()) {
                String line = String.format("%s,%s",
                        course.getCourseName(),
                        student.getUsername());
                lines.add(line);
            }
        }
        
        FileUtil.writeLines(ENROLLMENTS_FILE, lines);
    }

    /**
     * Loads enrollments (student-course relationships) from file.
     */
    private void loadEnrollmentsFromFile() throws IOException {
        List<String> lines = FileUtil.readLines(ENROLLMENTS_FILE);
        
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String courseName = parts[0];
                String username = parts[1];
                
                Course course = getCourseByName(courseName);
                Student student = (Student) userManager.findUserByUsername(username);
                
                if (course != null && student != null) {
                    course.addStudent(student);
                }
            }
        }
    }

    /**
     * Gets an assignment by its title within a course.
     *
     * @param course the course
     * @param title the assignment title
     * @return the assignment, or null if not found
     */
    private Assignment getAssignmentByTitle(Course course, String title) {
        for (Assignment assignment : course.getAssignments()) {
            if (assignment.getTitle().equals(title)) {
                return assignment;
            }
        }
        return null;
    }

    /**
     * Saves teacher to course relationships to file.
     */
    private void saveTeacherCoursesToFile() throws IOException {
        List<String> lines = new ArrayList<>();
        
        for (Teacher teacher : teachers.values()) {
            for (Course course : teacher.getTeachingCourses()) {
                String line = String.format("%s,%s",
                        teacher.getUsername(),
                        course.getCourseName());
                lines.add(line);
            }
        }
        
        FileUtil.writeLines(TEACHER_COURSES_FILE, lines);
    }

    /**
     * Loads teacher to course relationships from file.
     */
    private void loadTeacherCoursesFromFile() throws IOException {
        List<String> lines = FileUtil.readLines(TEACHER_COURSES_FILE);
        
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String teacherUsername = parts[0];
                String courseName = parts[1];
                
                Teacher teacher = getTeacherByUsername(teacherUsername);
                Course course = getCourseByName(courseName);
                
                if (teacher != null && course != null) {
                    teacher.addCourse(course);
                }
            }
        }
    }
} 