package net.bnijik.schooldbcli.service.student;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.dao.student.StudentDao;
import net.bnijik.schooldbcli.dto.StudentDto;
import net.bnijik.schooldbcli.entity.Student;
import net.bnijik.schooldbcli.mapper.StudentMapper;
import net.bnijik.schooldbcli.service.SchoolAdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl extends SchoolAdminServiceImpl<StudentDto, Student> implements StudentService {

    private final StudentMapper studentMapper;
    private final StudentDao studentDao;

    @Autowired
    public StudentServiceImpl(StudentMapper studentMapper, StudentDao studentDao) {
        super(studentMapper, studentDao);
        this.studentMapper = studentMapper;
        this.studentDao = studentDao;
    }

    @Override
    public List<StudentDto> findAllByCourseName(String courseName, Page page) {
        final List<Student> students = studentDao.findAllByCourseName(courseName, page);
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
        return super.save(studentMapper.modelToDto(new Student(0L, groupId, firstName, lastName)));
    }
}
