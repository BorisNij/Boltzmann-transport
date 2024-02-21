package net.bnijik.schooldbcli.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
@Entity
@Table(name = "groups")
public class Group {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @OrderBy("studentId")
    private List<Student> students = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_seq_generator")
    @SequenceGenerator(name = "group_seq_generator", sequenceName = "groups_group_id_seq", allocationSize = 1)
    @Column(name = "group_id", updatable = false, nullable = false)
    private long groupId;
    @NonNull
    @Column(name = "group_name", unique = true, nullable = false)
    private String groupName;

    public void students(final List<Student> students) {
        this.students.clear();
        students.stream()
                .map(s -> new Student(s.studentId(), s.group(), s.firstName(), s.lastName(), new HashSet<>()))
                .forEach(this.students::add);
    }

    public List<Student> students() {
        return Collections.unmodifiableList(students);
    }

    public void addStudent(Student student) {
        students.add(student);
        student.group(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.group(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Group) obj;
        return this.groupId == that.groupId;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Group[" + "groupId=" + groupId + ", " + "groupName=" + groupName + ']';
    }

}
