package org.gradebook.controller;

import org.gradebook.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentControllerTest {
    
    private Student student;
    private GradebookModel model;
    private StudentController controller;
    private Course course1, course2;
    private Assignment assignment1, assignment2;
    
    @BeforeEach
    void setUp() {
        // 创建模拟对象
        student = new Student("studentuser", "Test", "Student", "password");
        model = mock(GradebookModel.class);
        controller = new StudentController(student, model);
        
        // 创建测试数据
        course1 = new Course("Course 1");
        course2 = new Course("Course 2");
        
        assignment1 = new Assignment("Assignment 1", 100.0, "Homework");
        assignment2 = new Assignment("Assignment 2", 50.0, "Quiz");
        
        // 设置课程和学生关系
        course1.addStudent(student);
        course1.addAssignment(assignment1);
        
        course2.addStudent(student);
        course2.addAssignment(assignment2);
        
        // 设置学生课程
        student.addCourse(course1);
        student.addCourse(course2);
        
        // 设置一些成绩
        assignment1.assignGrade(student, 90.0);
        assignment2.assignGrade(student, 40.0);
    }
    
    @Test
    @DisplayName("Test getting enrolled courses")
    void testGetEnrolledCourses() {
        List<Course> courses = controller.getEnrolledCourses();
        assertEquals(2, courses.size());
        assertTrue(courses.contains(course1));
        assertTrue(courses.contains(course2));
    }
    
    @Test
    @DisplayName("Test getting assignments for course")
    void testGetAssignmentsForCourse() {
        List<Assignment> assignments = controller.getAssignmentsForCourse(course1);
        assertEquals(1, assignments.size());
        assertTrue(assignments.contains(assignment1));
    }
    
    @Test
    @DisplayName("Test getting grade for assignment")
    void testGetGrade() {
        Grade grade = controller.getGrade(course1, assignment1);
        assertNotNull(grade);
        assertEquals(90.0, grade.getPointsReceived());
        assertEquals(100.0, grade.getMaxPoints());
        assertEquals(90.0, grade.getPercentage());
    }
    
    @Test
    @DisplayName("Test calculating GPA")
    void testCalculateGPA() {
        // 设置最终成绩
        course1.assignFinalGrade(student, FinalGrade.A);  // 4.0
        course2.assignFinalGrade(student, FinalGrade.B);  // 3.0
        
        // 计算GPA
        double gpa = controller.calculateGPA();
        assertEquals(3.5, gpa);  // (4.0 + 3.0) / 2 = 3.5
    }
    
    @Test
    @DisplayName("Test getting course average")
    void testGetCourseAverage() {
        double average = controller.calculateAverage(course1);
        assertEquals(90.0, average);
    }
    
    @Test
    @DisplayName("Test getting final grade")
    void testGetFinalGrade() {
        // 没有在StudentController中找到获取最终成绩的方法，
        // 我们可以通过Student对象直接获取
        assertNull(student.getFinalGrade(course1));
        
        // 设置最终成绩
        course1.assignFinalGrade(student, FinalGrade.A);
        
        // 获取最终成绩
        assertEquals(FinalGrade.A, student.getFinalGrade(course1));
    }
    
    @Test
    @DisplayName("Test logout")
    void testLogout() {
        // 创建并设置主控制器
        MainController mainController = mock(MainController.class);
        controller.setMainController(mainController);
        
        // 执行登出
        controller.logout();
        
        // 验证主控制器的登出方法被调用
        verify(mainController).logout();
    }
} 