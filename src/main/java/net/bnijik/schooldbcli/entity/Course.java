package net.bnijik.schooldbcli.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "course_id", updatable = false, nullable = false)
    private long courseId;
    @NonNull
    @Column(name = "course_name", unique = true, nullable = false)
    private String courseName;
    @Column(name = "course_description")
    private String courseDescription;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Course) obj;
        return this.courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Course[" +
                "courseId=" + courseId + ", " +
                "courseName=" + courseName;
    }

}
