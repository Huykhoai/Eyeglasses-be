package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.GroupDto;
import org.erp.vnoptic.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { GroupTypeMapper.class })
public interface GroupMapper extends GenericMapper<Group, GroupDto> {
    @Override
    @Mapping(source = "groupType", target = "type", qualifiedByName = "toDto")
    GroupDto toDto(Group entity);
}
