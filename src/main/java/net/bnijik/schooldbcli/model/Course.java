package net.bnijik.schooldbcli.model;

import java.util.Objects;

public record Course(long courseId, String courseName, String courseDescription) {
    public Course {
        Objects.requireNonNull(courseName);
        Objects.requireNonNullElse(courseDescription, "");
    }
}
