package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.VeDto;
import org.erp.vnoptic.entity.Ve;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VeMapper extends GenericMapper<Ve, VeDto> {
}
