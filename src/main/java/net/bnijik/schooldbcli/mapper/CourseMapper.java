package net.bnijik.schooldbcli.mapper;

import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.stream.StreamSupport;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CourseMapper implements SchoolModelMapper<Course, CourseDto> {
    @Override
    public abstract CourseDto modelToDto(Course model);

    @Override
    public Slice<CourseDto> modelsToDtos(Iterable<Course> models) {
        List<CourseDto> courseDtos = StreamSupport.stream(models.spliterator(), false)
                .map(this::modelToDto)
                .toList();
        return new SliceImpl<>(courseDtos);
    }

    @Override
    public abstract Course dtoToModel(CourseDto dto);

    @Override
    public Slice<Course> dtosToModels(Iterable<CourseDto> dtos) {
        final List<Course> coursesModels = StreamSupport.stream(dtos.spliterator(), false)
                .map(this::dtoToModel)
                .toList();
        return new SliceImpl<>(coursesModels);
    }

}
