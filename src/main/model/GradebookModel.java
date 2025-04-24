package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradebookModel implements Subject {
    private final Map<String, Student> students;
    private final Map<String, Teacher> teachers;
    private final Map<String, Course> courses;
    private final List<Observer> observers = new ArrayList<>();

    public GradebookModel() {
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.courses = new HashMap<>();
    }

    // Student operations
    public void addStudent(Student s) {
        students.put(s.getUsername(), s);
    }

    public Student getStudentByUsername(String username) {
        return students.get(username);
    }

    public boolean studentExists(String username) {
        return students.containsKey(username);
    }

    // Teacher operations
    public void addTeacher(Teacher t) {
        teachers.put(t.getUsername(), t);
    }

    public Teacher getTeacherByUsername(String username) {
        return teachers.get(username);
    }

    public boolean teacherExists(String username) {
        return teachers.containsKey(username);
    }

    // Course operations
    public void addCourse(Course c) {
        courses.put(c.getCourseName(), c);
        notifyObservers();
    }

    public Course getCourseByName(String name) {
        return courses.get(name);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    public double calculateAssignmentMedian(Course course, Assignment assignment) {
        List<Double> scores = new ArrayList<>();

        for (Student student : course.getStudents()) {
            Grade grade = assignment.getGrade(student);
            if (grade != null) {
                scores.add(grade.getPointsReceived());
            }
        }

        if (scores.isEmpty()) return 0.0;

        scores.sort(Double::compareTo);

        int size = scores.size();
        if (size % 2 == 1) {
            return scores.get(size / 2);
        } else {
            return (scores.get(size / 2 - 1) + scores.get(size / 2)) / 2.0;
        }
    }
    
    public List<User> getAllUsers() {
        List<User> all = new ArrayList<>();
        all.addAll(students.values());
        all.addAll(teachers.values());
        return all;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}