package util;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import model.Student;
import model.User;

class FileUtilTest {

    @TempDir
    Path tempDir;

    @Test
    void saveUsers_WritesFullName() throws IOException {
        List<User> users = List.of(new Student("Alina", "Kushareva", "alina_k", "pass123"));
        Path file = tempDir.resolve("users.txt");
        FileUtil.saveUsers(users, file.toString());
        
        String content = Files.readString(file);
        assertTrue(content.contains("Alina Kushareva"));
    }

    @Test
    void loadUsers_ParsesFullName() throws IOException {
        Path file = tempDir.resolve("users.txt");
        Files.write(file, List.of("jake_s,Jake Smith,pass456,student"));
        
        List<User> users = FileUtil.loadUsers(file.toString());
        assertEquals("Jake Smith", users.get(0).getFullName());
    }

    @Test
    void parseStudentCSV_CreatesStudentWithFullName() throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.write(file, List.of("Alina,Kushareva,alina_k,pass123"));
        
        List<Student> students = FileUtil.parseStudentCSV(file.toString());
        assertEquals("Alina Kushareva", students.get(0).getFullName());
    }

    @Test
    void readLines_ReturnsCorrectNumberOfLines() throws IOException {
        Path file = tempDir.resolve("test.txt");
        Files.write(file, List.of("line1", "line2"));
        
        List<String> lines = FileUtil.readLines(file.toString());
        assertEquals(2, lines.size());
    }

    @Test
    void writeLines_CreatesFileWithExactContent() throws IOException {
        Path file = tempDir.resolve("test.txt");
        FileUtil.writeLines(file.toString(), List.of("test"));
        
        assertTrue(Files.exists(file));
    }
}