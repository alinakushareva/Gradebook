package util;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import model.Course;
import model.Student;
import model.User;
import model.UserManager;

class FileUtilTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveUsers_contentContainsFullName() throws IOException {
        List<User> users = List.of(new Student("Alina", "Kushareva", "alina_k", "pass123"));
        Path file = tempDir.resolve("users.txt");
        FileUtil.saveUsers(users, file.toString());
        String content = Files.readString(file);
        assertTrue(content.contains("Alina Kushareva"));
    }

    @Test
    void testLoadUsers_fullName() throws IOException {
        Path file = tempDir.resolve("users.txt");
        Files.write(file, List.of("jake_s,Jake Smith,pass456,student"));
        List<User> users = FileUtil.loadUsers(file.toString());
        assertEquals("Jake Smith", users.get(0).getFullName());
    }

    @Test
    void testParseStudentCSV_fullName() throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.write(file, List.of("Alina,Kushareva,alina_k,pass123"));
        List<Student> students = FileUtil.parseStudentCSV(file.toString());
        assertEquals("Alina Kushareva", students.get(0).getFullName());
    }

    @Test
    void testReadLines_size() throws IOException {
        Path file = tempDir.resolve("test.txt");
        Files.write(file, List.of("line1", "line2"));
        List<String> lines = FileUtil.readLines(file.toString());
        assertEquals(2, lines.size());
    }

    @Test
    void testWriteLines_fileExists() throws IOException {
        Path file = tempDir.resolve("test.txt");
        FileUtil.writeLines(file.toString(), List.of("test"));
        assertTrue(Files.exists(file));
    }

    @Test
    void testSaveCourses_numberOfLines() throws IOException {
        Student s1 = new Student("First", "One", "s1", "pwd1");
        Course c1 = new Course("MATH101");
        c1.addStudent(s1);
        Course c2 = new Course("ENG202");
        Path file = tempDir.resolve("courses.txt");
        FileUtil.saveCourses(List.of(c1, c2), file.toString());
        List<String> lines = Files.readAllLines(file);
        assertEquals(2, lines.size());
    }

    @Test
    void testSaveCourses_firstLine() throws IOException {
        Student s1 = new Student("First", "One", "s1", "pwd1");
        Course c1 = new Course("MATH101");
        c1.addStudent(s1);
        Path file = tempDir.resolve("courses.txt");
        FileUtil.saveCourses(List.of(c1), file.toString());
        String line = Files.readAllLines(file).get(0);
        assertEquals("MATH101,s1", line);
    }

    @Test
    void testSaveCourses_secondLineEmptyStudents() throws IOException {
        Course c2 = new Course("ENG202");
        Path file = tempDir.resolve("courses.txt");
        FileUtil.saveCourses(List.of(c2), file.toString());
        String line = Files.readAllLines(file).get(0);
        assertEquals("ENG202,", line);
    }
    
    private UserManager setupUserManager(Path userFile, List<String> lines) throws IOException {
        Files.write(userFile, lines);
        return new UserManager(userFile.toString());
    }

    @Test
    void testLoadCourses_courseNameParsedCorrectly() throws IOException {
        Path userFile = tempDir.resolve("users.txt");
        UserManager um = setupUserManager(userFile, List.of("s1,Alice Smith,hash,student"));

        Path courseFile = tempDir.resolve("courses.txt");
        Files.write(courseFile, List.of("MATH101,s1"));

        List<Course> courses = FileUtil.loadCourses(courseFile.toString(), um);
        assertEquals("MATH101", courses.get(0).getCourseName());
    }

    @Test
    void testLoadCourses_addsOnlyStudents() throws IOException {
        Path userFile = tempDir.resolve("users.txt");
        UserManager um = setupUserManager(userFile, List.of(
            "s1,Alice Smith,hash,student",
            "t1,Tom Teacher,hash,teacher"));

        Path courseFile = tempDir.resolve("courses.txt");
        Files.write(courseFile, List.of("CS101,s1,t1"));

        List<Course> courses = FileUtil.loadCourses(courseFile.toString(), um);
        assertEquals(1, courses.get(0).getStudents().size());
    }

    @Test
    void testLoadCourses_ignoresUnknownUsernames() throws IOException {
        Path userFile = tempDir.resolve("users.txt");
        UserManager um = setupUserManager(userFile, List.of("s1,Alice Smith,hash,student"));

        Path courseFile = tempDir.resolve("courses.txt");
        Files.write(courseFile, List.of("ENG202,s1,ghost"));

        List<Course> courses = FileUtil.loadCourses(courseFile.toString(), um);
        assertEquals(1, courses.get(0).getStudents().size());
    }

    @Test
    void testLoadCourses_multipleCoursesLoaded() throws IOException {
        Path userFile = tempDir.resolve("users.txt");
        UserManager um = setupUserManager(userFile, List.of("s1,Alice Smith,hash,student"));

        Path courseFile = tempDir.resolve("courses.txt");
        Files.write(courseFile, List.of("MATH101,s1", "ENG202,s1"));

        List<Course> courses = FileUtil.loadCourses(courseFile.toString(), um);
        assertEquals(2, courses.size());
    }
    

}
