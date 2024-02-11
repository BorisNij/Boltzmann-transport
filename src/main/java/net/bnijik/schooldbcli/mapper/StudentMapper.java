package net.bnijik.schooldbcli.mapper;

import net.bnijik.schooldbcli.dto.StudentDto;
import net.bnijik.schooldbcli.model.Student;
import net.bnijik.schooldbcli.service.course.CourseService;
import net.bnijik.schooldbcli.service.group.GroupService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class StudentMapper implements SchoolModelMapper<Student, StudentDto> {
    @Value("${school.max-courses-per-student}")
    protected int maxCoursesPerStud;

    @Autowired
    protected GroupService groupService;

    @Autowired
    protected CourseService courseService;

    @Mapping(target = "group", expression = "java(groupService.findById(model.groupId()).orElse(null))")
    @Mapping(target = "courses", expression = "java(courseService.findAllForStudent(model.studentId(), net.bnijik.schooldbcli.dao.Page.of(1, maxCoursesPerStud)))")
    @Override
    public abstract StudentDto modelToDto(Student model);

    @Override
    public abstract List<StudentDto> modelsToDtos(Collection<Student> models);

    @Mapping(target = "groupId", expression = "java(dto.group().groupId())")
    @Override
    public abstract Student dtoToModel(StudentDto dto);

    @Override
    public abstract List<Student> dtosToModels(Collection<StudentDto> dtos);
}
