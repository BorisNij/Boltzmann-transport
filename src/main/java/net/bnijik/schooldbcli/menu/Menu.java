package net.bnijik.schooldbcli.menu;

import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.dto.StudentDto;
import net.bnijik.schooldbcli.service.course.CourseService;
import net.bnijik.schooldbcli.service.group.GroupService;
import net.bnijik.schooldbcli.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Menu implements ApplicationRunner {
    private final Scanner scanner;
    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;

    @Autowired
    public Menu(Scanner scanner,
                StudentService studentService,
                GroupService groupService,
                CourseService courseService) {
        this.scanner = scanner;
        this.studentService = studentService;
        this.groupService = groupService;
        this.courseService = courseService;
    }

    @Override
    public void run(ApplicationArguments args) {

        int userChoice = -1;
        while (userChoice != 0) {
            try {
                userChoice = getUserChoice(scanner);
                processUserChoice(userChoice, scanner);
            } catch (Exception e) {
                if (e instanceof InputMismatchException) {
                    scanner.nextLine();
                }
                System.err.println("Invalid input");
            }
        }
    }

    private void processUserChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 0:
                System.out.println("Quitting...");
                break;
            case 1:
                getGroupsByMaxStudentCount(scanner);
                break;
            case 2:
                getStudentsByCourseName(scanner);
                break;
            case 3:
                addStudent(scanner);
                break;
            case 4:
                removeStudent(scanner);
                break;
            case 5:
                enrollStudentInCourses(scanner);
                break;
            case 6:
                withdrawStudentFromCourse(scanner);
                break;
            default:
                System.err.println("Invalid input. Please enter a number between 1 and 6 (or Ctrl + C to quit).");
                sleep(500);
        }
    }

    private int getUserChoice(Scanner scanner) {
        System.out.println("\n=== Student Database Menu ===");
        System.out.println("1. Find all groups with less or equal students count");
        System.out.println("2. Find all students enrolled in a course");
        System.out.println("3. Add Student");
        System.out.println("4. Delete Student");
        System.out.println("5. Enroll Student in Courses");
        System.out.println("6. Withdraw Student from Course");
        System.out.println("\t(0 to quit)\n");

        System.out.print("Enter your choice: ");
        final int userChoice = scanner.nextInt();
        scanner.nextLine();

        return userChoice;
    }


    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
        }
    }

    private void withdrawStudentFromCourse(Scanner scanner) {
        System.out.println("\tWITHDRAWING a student from a course");
        System.out.print("Enter student ID (or any letter to abort): ");
        try {
            int studentId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter course ID to withdraw from (or any letter to abort): ");
            int courseId = scanner.nextInt();
            scanner.nextLine();

            studentService.withdrawFromCourse(studentId, courseId);
            System.out.println("Withdrawn from the course.");
        } catch (InputMismatchException ignored) {
            scanner.nextLine();
            System.out.println("\tWithdrawal aborted");
        }
    }

    private void getGroupsByMaxStudentCount(Scanner scanner) {

        System.out.print("Enter student count: ");
        int studentCount = scanner.nextInt();
        scanner.nextLine();

        Slice<GroupDto> groups = groupService.findAllByMaxStudentCount(studentCount, PageRequest.of(0, 10));

        if (!groups.isEmpty()) {
            System.out.printf("Groups with %d or less than %<d students:\n", studentCount);
            printIndexed(groups, 2);
        } else {
            System.out.printf("No groups found with %d or less than %<d students\n", studentCount);
        }
    }

    private void getStudentsByCourseName(Scanner scanner) {
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine();

        Slice<StudentDto> students = studentService.findAllByCourseName(courseName, PageRequest.of(0, 50));
        if (!students.isEmpty()) {
            System.out.printf("Students enrolled in %s course:\n", courseName);
            printIndexed(students, 3);
        } else {
            System.out.printf("No students enrolled in %s course.", courseName);
        }
    }

    private <T> void printIndexed(Iterable<T> items, int indexLength) {
        final ArrayList<T> itemList = new ArrayList<>();
        items.forEach(itemList::add);
        IntStream.range(0, itemList.size())
                .forEach(i -> System.out.printf("%1$" + indexLength + "s. %2$s\n", i + 1, itemList.get(i)));
    }

    private void addStudent(Scanner scanner) {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter group name: ");
        String groupName = scanner.nextLine();

        final GroupDto group = groupService.findByName(groupName).get();
        studentService.save(firstName, lastName, group.groupId());
    }

    private void removeStudent(Scanner scanner) {
        System.out.println("Caution! \n\tREMOVED STUDENT CANNOT BE RECOVERED");
        System.out.print("Enter student ID to delete (or any letter to abort): ");
        try {
            int studentId = scanner.nextInt();
            final Optional<StudentDto> studentById = studentService.findById(studentId);
            if (studentById.isPresent()) {
                studentService.delete(studentId);
                System.out.printf("Student %s was removed.\n", studentById.get());
            } else {
                System.out.printf("Student with ID %s does not exist\n", studentId);
                System.out.println("Nothing to do");
            }
        } catch (InputMismatchException ignored) {
            System.out.println("\tRemoval aborted");
        } finally {
            scanner.nextLine();
        }
    }

    private void enrollStudentInCourses(Scanner scanner) {
        System.out.print("Enter student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        StudentDto student = studentService.findById(studentId).get();

        System.out.print("Enter course IDs (comma-separated): ");
        String[] courseIds = scanner.nextLine().split(",");
        List<Long> courseIdList = Arrays.stream(courseIds)
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());

        studentService.enrollInCourses(studentId, courseIdList);
        List<CourseDto> coursesEnrolled = new ArrayList<>();
        for (Long id : courseIdList) {
            coursesEnrolled.add(courseService.findById(id).get());
        }
        System.out.printf("Enrolled %s in courses: %s", student, coursesEnrolled);
    }

}
