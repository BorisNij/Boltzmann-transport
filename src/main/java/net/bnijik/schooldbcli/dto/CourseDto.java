package net.bnijik.schooldbcli.dto;

import java.util.Objects;

public record CourseDto(
        long courseId,
        String courseName,
        String courseDescription) {

    public CourseDto {
        Objects.requireNonNull(courseName);
        Objects.requireNonNullElse(courseDescription, "");
    }

    @Override
    public String toString() {
        return "{\n"
                + "\t\"courseId\": " + courseId + ",\n"
                + "\t\"courseName\": \"" + courseName + "\",\n"
                + "\t\"courseDescription\": \"" + courseDescription + "\"\n"
                + "}";
    }
}
