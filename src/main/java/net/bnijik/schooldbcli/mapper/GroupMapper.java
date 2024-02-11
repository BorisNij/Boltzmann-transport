package net.bnijik.schooldbcli.mapper;

import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class GroupMapper implements SchoolModelMapper<Group, GroupDto> {
    @Override
    public abstract GroupDto modelToDto(Group model);

    @Override
    public abstract List<GroupDto> modelsToDtos(Collection<Group> models);

    @Override
    public abstract Group dtoToModel(GroupDto dto);

    @Override
    public abstract List<Group> dtosToModels(Collection<GroupDto> dtos);

}
