package net.bnijik.schooldbcli.mapper;

import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.stream.StreamSupport;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class GroupMapper implements SchoolModelMapper<Group, GroupDto> {
    @Override
    public abstract GroupDto modelToDto(Group model);

    @Override
    public Slice<GroupDto> modelsToDtos(Iterable<Group> models) {
        final List<GroupDto> groupDtos = StreamSupport.stream(models.spliterator(), false)
                .map(this::modelToDto)
                .toList();
        return new SliceImpl<>(groupDtos);
    }

    @Override
    public abstract Group dtoToModel(GroupDto dto);

    @Override
    public Slice<Group> dtosToModels(Iterable<GroupDto> dtos) {
        final List<Group> groupModels = StreamSupport.stream(dtos.spliterator(), false).map(this::dtoToModel).toList();
        return new SliceImpl<>(groupModels);
    }

}
