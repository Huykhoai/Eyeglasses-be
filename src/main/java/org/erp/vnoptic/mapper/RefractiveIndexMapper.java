package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.RefractiveIndexDto;
import org.erp.vnoptic.entity.RefractiveIndex;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefractiveIndexMapper extends GenericMapper<RefractiveIndex, RefractiveIndexDto> {
}
