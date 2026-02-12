package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.FrameTypeDto;
import org.erp.vnoptic.entity.FrameType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FrameTypeMapper extends GenericMapper<FrameType, FrameTypeDto> {
}
