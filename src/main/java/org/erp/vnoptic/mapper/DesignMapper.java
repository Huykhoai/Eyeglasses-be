package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.DesignDto;
import org.erp.vnoptic.entity.Design;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DesignMapper extends GenericMapper<Design, DesignDto> {
}
