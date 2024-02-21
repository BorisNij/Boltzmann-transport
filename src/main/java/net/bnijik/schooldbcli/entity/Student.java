package net.bnijik.schooldbcli.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
@Builder
@Entity
@Table(name = "students")
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
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @OrderBy("courseId")
    @Builder.Default
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.students().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.students().remove(this);
    }


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