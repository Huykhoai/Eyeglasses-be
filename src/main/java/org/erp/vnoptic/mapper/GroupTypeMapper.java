package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.GroupTypeDto;
import org.erp.vnoptic.entity.GroupType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupTypeMapper extends GenericMapper<GroupType, GroupTypeDto> {
}
