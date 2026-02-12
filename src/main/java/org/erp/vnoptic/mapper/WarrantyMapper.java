package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.WarrantyDto;
import org.erp.vnoptic.entity.Warranty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarrantyMapper extends GenericMapper<Warranty, WarrantyDto> {
}
