package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.FrameDto;
import org.erp.vnoptic.entity.Frame;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FrameTypeMapper.class})
public interface FrameMapper extends GenericMapper<Frame, FrameDto> {
    @Override
    @Mapping(source = "frameType", target = "type", qualifiedByName = "toDto")
    FrameDto toDto(Frame entity);
}
