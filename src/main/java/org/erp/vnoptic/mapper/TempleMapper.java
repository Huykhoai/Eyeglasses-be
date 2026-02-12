package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.TempleDto;
import org.erp.vnoptic.entity.Temple;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TempleMapper extends GenericMapper<Temple, TempleDto> {
}
