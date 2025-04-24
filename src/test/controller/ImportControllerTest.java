package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImportControllerTest {

    private GradebookModel model;
    private Teacher teacher;
    private ImportController controller;
    private Course course;
    private Student student1;
    private Student student2;

    @BeforeEach
    void setup() {
        model = new GradebookModel();
        teacher = new Teacher("teacher1", "Alice", "Smith", "secure");
        model.addTeacher(teacher);
        controller = new ImportController(model, teacher);
        course = new Course("CSC101");

        student1 = new Student("Alina", "Kushareva", "s123", "pass123");
        student2 = new Student("John", "Doe", "s124", "pass456");

        model.addStudent(student1);
        model.addStudent(student2);
    }

    @Test
    void testValidateFile_Valid(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("students.txt");
        Files.write(file, List.of("s123"));
        assertTrue(controller.validateFile(file.toString()));
    }

    @Test
    void testValidateFile_EmptyButReadable(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.createFile(file);
        assertTrue(controller.validateFile(file.toString()));
    }

    @Test
    void testValidateFile_InvalidPath() {
        assertFalse(controller.validateFile("/nonexistent/path/students.txt"));
    }

    @Test
    void testImportStudents_WithValidUsernames(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("students.txt");
        Files.write(file, List.of("s123", "s124"));

        List<Student> imported = controller.importStudents(file.toString());

        assertEquals(2, imported.size());
        assertTrue(imported.contains(student1));
        assertTrue(imported.contains(student2));
    }

    @Test
    void testImportStudents_IgnoresUnknownUsernames(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("students.txt");
        Files.write(file, List.of("unknown", "s123"));

        List<Student> imported = controller.importStudents(file.toString());

        assertEquals(1, imported.size());
        assertEquals("s123", imported.get(0).getUsername());
    }

    @Test
    void testImportStudents_InvalidPathReturnsEmptyList() {
        List<Student> imported = controller.importStudents("/invalid/file.txt");
        assertTrue(imported.isEmpty());
    }

    @Test
    void testAddStudentsToCourse() {
        controller.addStudentsToCourse(List.of(student1, student2), course);
        assertEquals(2, course.getStudents().size());
        assertTrue(course.getStudents().contains(student1));
        assertTrue(course.getStudents().contains(student2));
    }

    @Test
    void testAddStudentsToCourse_DoesNotAddDuplicates() {
        course.addStudent(student1); // already enrolled
        controller.addStudentsToCourse(List.of(student1, student2), course);
        assertEquals(2, course.getStudents().size()); // not duplicated
    }

    @Test
    void testHandleImport_ValidFile_EnrollsStudents(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("students.txt");
        Files.write(file, List.of("s123", "s124"));

        controller.handleImport(file.toString(), course);

        assertEquals(2, course.getStudents().size());
        assertTrue(course.getStudents().contains(student1));
        assertTrue(course.getStudents().contains(student2));
    }

    @Test
    void testHandleImport_InvalidFile_DoesNothing() {
        controller.handleImport("/invalid/file.txt", course);
        assertTrue(course.getStudents().isEmpty());
    }

    @Test
    void testHandleImport_ValidFileButNoMatches(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("students.txt");
        Files.write(file, List.of("ghost1", "ghost2"));

        controller.handleImport(file.toString(), course);
        assertTrue(course.getStudents().isEmpty());
    }
}
