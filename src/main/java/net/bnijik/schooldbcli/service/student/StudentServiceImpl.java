package net.bnijik.schooldbcli.service.student;

import net.bnijik.schooldbcli.dao.student.StudentDao;
import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.dto.StudentDto;
import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.entity.Student;
import net.bnijik.schooldbcli.mapper.GroupMapper;
import net.bnijik.schooldbcli.mapper.StudentMapper;
import net.bnijik.schooldbcli.service.SchoolAdminServiceImpl;
import net.bnijik.schooldbcli.service.group.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StudentServiceImpl extends SchoolAdminServiceImpl<StudentDto, Student> implements StudentService {

    private final StudentMapper studentMapper;
    private final StudentDao studentDao;
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @Autowired
    public StudentServiceImpl(StudentMapper studentMapper,
                              StudentDao studentDao,
                              GroupService groupService,
                              GroupMapper groupMapper) {
        super(studentMapper, studentDao);
        this.studentMapper = studentMapper;
        this.studentDao = studentDao;
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @Override
    public Slice<StudentDto> findAllByCourseName(String courseName, Pageable pageable) {
        final Slice<Student> students = studentDao.findAllByCourseName(courseName, pageable);
        return studentMapper.modelsToDtos(students);
    }

    @Override
    public boolean enrollInCourses(long studentId, List<Long> courseIds) {
        return studentDao.enrollInCourses(studentId, courseIds);
    }

    @Override
    public boolean withdrawFromCourse(long studentId, long courseId) {
        return studentDao.withdrawFromCourse(studentId, courseId);
    }

    @Override
    public long save(String firstName, String lastName, long groupId) {
        final GroupDto groupDto = groupService.findById(groupId).orElse(null);
        final Group group = groupMapper.dtoToModel(groupDto);
        return super.save(studentMapper.modelToDto(new Student(0L,
                                                               group,
                                                               firstName,
                                                               lastName,
                                                               Collections.emptySet())));
    }
}
