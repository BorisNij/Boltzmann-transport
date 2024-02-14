package net.bnijik.schooldbcli.mapper;

import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CourseMapper implements SchoolModelMapper<Course, CourseDto> {
    @Override
    public abstract CourseDto modelToDto(Course model);

    @Override
    public abstract List<CourseDto> modelsToDtos(Collection<Course> models);

    @Override
    public abstract Course dtoToModel(CourseDto dto);

    @Override
    public abstract List<Course> dtosToModels(Collection<CourseDto> dtos);

}
