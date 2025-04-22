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

    @BeforeEach
    void setup() {
        model = new GradebookModel();
        teacher = new Teacher("teacher1", "Alice", "Smith", "secure");
        controller = new ImportController(model, teacher);
        course = new Course("CSC101");
    }

    @Test
    void testValidateFile_Valid(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.write(file, List.of("s123,Alina,Kushareva,pass123"));
        assertTrue(controller.validateFile(file.toString()));
    }

    @Test
    void testValidateFile_EmptyButReadable(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("empty.csv");
        Files.createFile(file);
        assertTrue(controller.validateFile(file.toString()));
    }

    @Test
    void testValidateFile_Invalid() {
        assertFalse(controller.validateFile("/invalid/path/file.csv"));
    }

    @Test
    void testImportStudents_ValidLine(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.write(file, List.of("s123,Alina,Kushareva,pass123"));

        List<Student> students = controller.importStudents(file.toString());
        assertEquals(1, students.size());
        assertEquals("s123", students.get(0).getUsername());
        assertEquals("Alina", students.get(0).getFirstName());
    }

    @Test
    void testImportStudents_SkipsInvalidLines(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.write(file, List.of(
                "s123,Alina,Kushareva,pass123",
                "invalidLineWithTooFewFields",
                "s124,John,Doe,pass456"
        ));

        List<Student> students = controller.importStudents(file.toString());
        assertEquals(2, students.size());
        assertEquals("s124", students.get(1).getUsername());
    }

    @Test
    void testImportStudents_InvalidFilePath() {
        List<Student> students = controller.importStudents("/invalid/path/file.csv");
        assertTrue(students.isEmpty());
    }

    @Test
    void testAddStudentsToCourse() {
        Student s = new Student("Alina", "Kushareva", "alina_k", "pass");
        controller.addStudentsToCourse(List.of(s), course);
        assertTrue(course.getStudents().contains(s));
    }

    @Test
    void testHandleImport_Success(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.write(file, List.of("s123,Alina,Kushareva,pass123"));

        controller.handleImport(file.toString(), course);

        assertEquals(1, course.getStudents().size());
        assertEquals("s123", course.getStudents().get(0).getUsername());
    }

    @Test
    void testHandleImport_InvalidFilePath() {
        controller.handleImport("/invalid/path/file.csv", course);
        assertEquals(0, course.getStudents().size());
    }
}