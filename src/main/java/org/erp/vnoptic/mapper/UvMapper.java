package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.UvDto;
import org.erp.vnoptic.entity.Uv;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UvMapper extends GenericMapper<Uv, UvDto> {
}
