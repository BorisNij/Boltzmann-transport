package net.bnijik.schooldbcli.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
@Builder
@Entity
@Table(name = "students",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"first_name", "last_name"})}
)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "student_id", updatable = false, nullable = false)
    private long studentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
    @NonNull
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @NonNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Student) obj;
        return this.studentId == that.studentId;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Student[" +
                "studentId=" + studentId + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ']';
    }

}