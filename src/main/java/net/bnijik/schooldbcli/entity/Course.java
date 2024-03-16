package net.bnijik.schooldbcli.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
@Entity
@Table(name = "courses")
public class Course implements Comparable<Course> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq_generator")
    @SequenceGenerator(name = "course_seq_generator", sequenceName = "courses_course_id_seq", allocationSize = 1)
    @Column(name = "course_id", updatable = false, nullable = false)
    private long courseId;
    @NonNull
    @Column(name = "course_name", unique = true, nullable = false)
    private String courseName;
    @Column(name = "course_description")
    private String courseDescription;
    @ManyToMany(mappedBy = "courses")
    @OrderBy("studentId")
    private Set<Student> students = new LinkedHashSet<>();

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


    @Override
    public int compareTo(@NonNull Course o) {
        return Long.compare(this.courseId, o.courseId);
    }
}
