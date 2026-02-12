package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.ColorDto;
import org.erp.vnoptic.entity.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper extends GenericMapper<Color, ColorDto> {
}
