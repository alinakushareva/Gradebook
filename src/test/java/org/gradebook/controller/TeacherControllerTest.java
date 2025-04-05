package org.gradebook.controller;

import org.gradebook.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherControllerTest {
    
    private Teacher teacher;
    private GradebookModel model;
    private TeacherController controller;
    private Course course;
    private Student student;
    private Assignment assignment;
    
    @BeforeEach
    void setUp() {
        // 创建模拟对象
        teacher = new Teacher("teacheruser", "Test", "Teacher", "password");
        model = mock(GradebookModel.class);
        controller = new TeacherController(teacher, model);
        
        // 创建测试数据
        course = new Course("Test Course");
        student = new Student("testStudent", "Test", "Student", "password");
        assignment = new Assignment("Test Assignment", 100.0, "Test");
        
        // 设置课程和教师关系
        teacher.addCourse(course);
        course.addStudent(student);
        course.addAssignment(assignment);
    }
    
    @Test
    @DisplayName("Test getting teaching courses")
    void testGetTeachingCourses() {
        List<Course> courses = controller.getTeachingCourses();
        assertEquals(1, courses.size());
        assertTrue(courses.contains(course));
    }
    
    @Test
    @DisplayName("Test adding assignment")
    void testAddAssignment() {
        // 模拟model.saveAllData方法
        doNothing().when(model).saveAllData();
        
        Assignment newAssignment = controller.addAssignment(course, "New Assignment", 50.0, "Homework");
        
        // 验证返回的作业对象
        assertNotNull(newAssignment);
        assertEquals("New Assignment", newAssignment.getTitle());
        assertEquals(50.0, newAssignment.getMaxPoints());
        assertEquals("Homework", newAssignment.getCategory());
        
        // 验证作业被添加到课程中
        assertTrue(course.getAssignments().contains(newAssignment));
        
        // 验证model.saveAllData被调用
        verify(model).saveAllData();
    }
    
    @Test
    @DisplayName("Test adding assignment with invalid parameters")
    void testAddInvalidAssignment() {
        // 课程为空
        assertNull(controller.addAssignment(null, "Title", 100.0, "Cat"));
        
        // 标题为空
        assertNull(controller.addAssignment(course, "", 100.0, "Cat"));
        assertNull(controller.addAssignment(course, null, 100.0, "Cat"));
        
        // 分数非法
        assertNull(controller.addAssignment(course, "Title", 0.0, "Cat"));
        assertNull(controller.addAssignment(course, "Title", -10.0, "Cat"));
    }
    
    @Test
    @DisplayName("Test assigning grade")
    void testAssignGrade() {
        // 模拟model.saveAllData方法
        doNothing().when(model).saveAllData();
        
        // 分配成绩
        controller.assignGrade(course, student, assignment, 85.0);
        
        // 验证成绩是否被正确分配
        Grade grade = assignment.getGrade(student);
        assertNotNull(grade);
        assertEquals(85.0, grade.getPointsReceived());
        
        // 验证model.saveAllData被调用
        verify(model).saveAllData();
    }
    
    @Test
    @DisplayName("Test assigning final grade")
    void testAssignFinalGrade() {
        // 模拟model.saveAllData方法
        doNothing().when(model).saveAllData();
        
        // 分配最终成绩
        controller.assignFinalGrade(course, student, FinalGrade.A);
        
        // 验证最终成绩是否被正确分配
        FinalGrade finalGrade = course.getFinalGrade(student);
        assertEquals(FinalGrade.A, finalGrade);
        
        // 验证model.saveAllData被调用
        verify(model).saveAllData();
    }
    
    @Test
    @DisplayName("Test getting students for course")
    void testGetStudentsForCourse() {
        List<Student> students = controller.getStudentsForCourse(course);
        assertEquals(1, students.size());
        assertTrue(students.contains(student));
    }
    
    @Test
    @DisplayName("Test getting assignments for course")
    void testGetAssignmentsForCourse() {
        List<Assignment> assignments = controller.getAssignmentsForCourse(course);
        assertEquals(1, assignments.size());
        assertTrue(assignments.contains(assignment));
    }
    
    @Test
    @DisplayName("Test calculating class average")
    void testCalculateClassAverage() {
        // 先分配一个成绩
        assignment.assignGrade(student, 85.0);
        
        // 计算班级平均分
        double average = controller.calculateClassAverage(course);
        assertEquals(85.0, average);
    }
    
    @Test
    @DisplayName("Test creating course")
    void testCreateCourse() {
        // 模拟model.createCourse和model.saveAllData方法
        Course newCourse = new Course("New Course");
        when(model.createCourse("New Course", teacher)).thenReturn(newCourse);
        doNothing().when(model).saveAllData();
        
        // 创建课程
        Course result = controller.createCourse("New Course");
        
        // 验证返回的课程对象
        assertNotNull(result);
        assertEquals("New Course", result.getCourseName());
        
        // 验证model方法被调用
        verify(model).createCourse("New Course", teacher);
        verify(model).saveAllData();
    }
    
    @Test
    @DisplayName("Test updating assignment")
    void testUpdateAssignment() {
        // 测试更新作业
        boolean result = controller.updateAssignment(course, assignment, "Updated Title", 120.0, "New Category");
        
        // 验证更新结果
        assertTrue(result);
        assertEquals("Updated Title", assignment.getTitle());
        assertEquals(120.0, assignment.getMaxPoints());
        assertEquals("New Category", assignment.getCategory());
    }
} 