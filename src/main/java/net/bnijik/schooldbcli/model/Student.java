package net.bnijik.schooldbcli.model;

import java.util.Objects;

public record Student(long studentId, long groupId, String firstName, String lastName) {
    public Student {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
    }
}
