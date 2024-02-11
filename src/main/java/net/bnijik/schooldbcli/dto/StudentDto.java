package net.bnijik.schooldbcli.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record StudentDto(long studentId, GroupDto group, String firstName, String lastName, List<CourseDto> courses) {

    public StudentDto {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(courses);
    }

    @Override
    public String toString() {
        String groupStr = null == group ? "null" : group().toString();
        return "{\n"
                + "\t\"studentId\": " + studentId + ",\n"
                + "\t\"group\": " + groupStr + ",\n"
                + "\t\"firstName\": \"" + firstName + "\",\n"
                + "\t\"lastName\": \"" + lastName + "\",\n"
                + "\t\"courses\": " + coursesToString(courses) + "\n"
                + "}";
    }

    private String coursesToString(List<CourseDto> courses) {
        String coursesString = courses.stream()
                .map(course -> tabIndentCourses(course, 3))
                .collect(Collectors.joining(",\n", "[\n", "\n\t]"));
        return coursesString;
    }

    private String tabIndentCourses(CourseDto course, int numTabs) {
        String tabs = "\t".repeat(numTabs);
        return tabs + course.toString().replace("\n", "\n" + tabs);
    }

}
