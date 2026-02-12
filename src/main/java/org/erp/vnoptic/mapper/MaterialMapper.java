package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.MaterialDto;
import org.erp.vnoptic.entity.Material;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaterialMapper extends GenericMapper<Material, MaterialDto> {
}
