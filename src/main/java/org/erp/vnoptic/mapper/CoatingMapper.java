package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.CoatingDto;
import org.erp.vnoptic.entity.Coating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoatingMapper extends GenericMapper<Coating, CoatingDto>{
}
