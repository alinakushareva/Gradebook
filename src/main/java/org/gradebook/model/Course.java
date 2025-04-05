package org.gradebook.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a course in the gradebook system.
 * Contains information about students, assignments, categories, and grading settings.
 */
public class Course {
    private String courseName;
    private List<Student> students;
    private List<Assignment> assignments;
    private List<Category> categories;
    private Map<Student, FinalGrade> finalGrades;
    private boolean useWeightedGrading;
    private int numAssignmentsToDrop;
    
    /**
     * Constructor for Course class.
     *
     * @param courseName the name of the course
     */
    public Course(String courseName) {
        this.courseName = courseName;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.finalGrades = new HashMap<>();
        this.useWeightedGrading = false;
        this.numAssignmentsToDrop = 0;
    }
    
    /**
     * Gets the name of the course.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }
    
    /**
     * Sets the name of the course.
     *
     * @param courseName the course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    /**
     * Adds a student to the course.
     *
     * @param student the student to add
     */
    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
            student.addCourse(this);
        }
    }
    
    /**
     * Removes a student from the course.
     *
     * @param student the student to remove
     */
    public void removeStudent(Student student) {
        students.remove(student);
        finalGrades.remove(student);
    }
    
    /**
     * Gets a list of all students in the course.
     *
     * @return the list of students
     */
    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }
    
    /**
     * Adds an assignment to the course.
     *
     * @param assignment the assignment to add
     */
    public void addAssignment(Assignment assignment) {
        if (!assignments.contains(assignment)) {
            assignments.add(assignment);
            
            // If the assignment has a category, add it to that category
            if (assignment.getCategory() != null) {
                Category category = getCategoryByName(assignment.getCategory());
                if (category != null) {
                    category.addAssignment(assignment);
                }
            }
        }
    }
    
    /**
     * Gets a category by its name.
     *
     * @param categoryName the name of the category
     * @return the category, or null if not found
     */
    private Category getCategoryByName(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null;
    }
    
    /**
     * Gets a list of all assignments in the course.
     *
     * @return the list of assignments
     */
    public List<Assignment> getAssignments() {
        return new ArrayList<>(assignments);
    }
    
    /**
     * Assigns a final grade to a student.
     *
     * @param student the student
     * @param grade   the final grade
     */
    public void assignFinalGrade(Student student, FinalGrade grade) {
        if (students.contains(student)) {
            finalGrades.put(student, grade);
            student.assignFinalGrade(this, grade);
        }
    }
    
    /**
     * Gets the final grade for a student.
     *
     * @param student the student
     * @return the final grade, or null if not assigned
     */
    public FinalGrade getFinalGrade(Student student) {
        return finalGrades.get(student);
    }
    
    /**
     * Gets a list of all ungraded assignments.
     *
     * @return the list of ungraded assignments
     */
    public List<Assignment> getUngradedAssignments() {
        List<Assignment> ungraded = new ArrayList<>();
        for (Assignment assignment : assignments) {
            for (Student student : students) {
                if (!assignment.isGraded(student)) {
                    ungraded.add(assignment);
                    break;
                }
            }
        }
        return ungraded;
    }
    
    /**
     * Adds a category to the course.
     *
     * @param category the category to add
     */
    public void addCategory(Category category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }
    
    /**
     * Gets a list of all categories in the course.
     *
     * @return the list of categories
     */
    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }
    
    /**
     * Sets whether to use weighted grading.
     *
     * @param useWeightedGrading true to use weighted grading, false otherwise
     */
    public void setUseWeightedGrading(boolean useWeightedGrading) {
        this.useWeightedGrading = useWeightedGrading;
    }
    
    /**
     * Checks if the course uses weighted grading.
     *
     * @return true if weighted grading is enabled, false otherwise
     */
    public boolean isUseWeightedGrading() {
        return useWeightedGrading;
    }
    
    /**
     * Sets the number of assignments to drop.
     *
     * @param numAssignmentsToDrop the number of assignments to drop
     */
    public void setNumAssignmentsToDrop(int numAssignmentsToDrop) {
        this.numAssignmentsToDrop = numAssignmentsToDrop;
    }
    
    /**
     * Gets the number of assignments to drop.
     *
     * @return the number of assignments to drop
     */
    public int getNumAssignmentsToDrop() {
        return numAssignmentsToDrop;
    }
    
    /**
     * Calculates the average grade for the entire class.
     *
     * @return the class average
     */
    public double calculateClassAverage() {
        if (students.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        int count = 0;
        
        for (Student student : students) {
            double studentAverage = calculateStudentAverage(student);
            if (studentAverage >= 0) {
                sum += studentAverage;
                count++;
            }
        }
        
        return count > 0 ? sum / count : 0.0;
    }
    
    /**
     * Calculates the median grade for a specific assignment.
     *
     * @param assignment the assignment
     * @return the median grade, or -1 if no grades
     */
    public double calculateMedianForAssignment(Assignment assignment) {
        if (!assignments.contains(assignment)) {
            return -1.0;
        }
        
        List<Double> grades = new ArrayList<>();
        for (Student student : students) {
            Grade grade = assignment.getGrade(student);
            if (grade != null) {
                grades.add(grade.getPercentage());
            }
        }
        
        if (grades.isEmpty()) {
            return -1.0;
        }
        
        Collections.sort(grades);
        int middle = grades.size() / 2;
        
        if (grades.size() % 2 == 0) {
            return (grades.get(middle - 1) + grades.get(middle)) / 2.0;
        } else {
            return grades.get(middle);
        }
    }
    
    /**
     * Calculates the average grade for a specific student.
     *
     * @param student the student
     * @return the student average, or -1 if no grades
     */
    public double calculateStudentAverage(Student student) {
        if (!students.contains(student)) {
            return -1.0;
        }
        
        if (useWeightedGrading && !categories.isEmpty()) {
            // Weighted grading
            double totalWeight = 0.0;
            double weightedSum = 0.0;
            
            for (Category category : categories) {
                double categoryAverage = category.calculateCategoryAverage(student);
                if (categoryAverage >= 0) {
                    weightedSum += categoryAverage * category.getWeight();
                    totalWeight += category.getWeight();
                }
            }
            
            return totalWeight > 0 ? weightedSum / totalWeight : -1.0;
        } else {
            // Simple grading
            double sum = 0.0;
            int count = 0;
            
            for (Assignment assignment : assignments) {
                Grade grade = assignment.getGrade(student);
                if (grade != null) {
                    sum += grade.getPercentage();
                    count++;
                }
            }
            
            return count > 0 ? sum / count : -1.0;
        }
    }
    
    /**
     * Sorts students by name.
     *
     * @return the sorted list of students
     */
    public List<Student> sortStudentsByName() {
        List<Student> sortedStudents = new ArrayList<>(students);
        Collections.sort(sortedStudents, (s1, s2) -> s1.getFullName().compareTo(s2.getFullName()));
        return sortedStudents;
    }
    
    /**
     * Sorts students by grade on a specific assignment.
     *
     * @param assignment the assignment
     * @return the sorted list of students
     */
    public List<Student> sortStudentsByGrade(Assignment assignment) {
        List<Student> sortedStudents = new ArrayList<>(students);
        
        Collections.sort(sortedStudents, (s1, s2) -> {
            Grade g1 = assignment.getGrade(s1);
            Grade g2 = assignment.getGrade(s2);
            
            if (g1 == null && g2 == null) return 0;
            if (g1 == null) return 1;
            if (g2 == null) return -1;
            
            return Double.compare(g2.getPercentage(), g1.getPercentage());
        });
        
        return sortedStudents;
    }
    
    /**
     * Groups students into specified lists.
     *
     * @param groups the list of student groups
     */
    public void groupStudents(List<List<Student>> groups) {
        if (groups == null || groups.isEmpty()) {
            return;
        }
        
        // Clear previous groups
        for (List<Student> group : groups) {
            group.clear();
        }
        
        // Create a copy of the students list
        List<Student> studentsCopy = new ArrayList<>(students);
        Collections.shuffle(studentsCopy);
        
        // Distribute students evenly among groups
        int groupSize = (int) Math.ceil((double) studentsCopy.size() / groups.size());
        
        for (int i = 0; i < studentsCopy.size(); i++) {
            int groupIndex = i / groupSize;
            if (groupIndex < groups.size()) {
                groups.get(groupIndex).add(studentsCopy.get(i));
            }
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseName, course.courseName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseName);
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "courseName='" + courseName + '\'' +
                ", students=" + students.size() +
                ", assignments=" + assignments.size() +
                ", categories=" + categories.size() +
                ", useWeightedGrading=" + useWeightedGrading +
                ", numAssignmentsToDrop=" + numAssignmentsToDrop +
                '}';
    }
} 