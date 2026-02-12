package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.TaxDto;
import org.erp.vnoptic.entity.Tax;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaxMapper extends GenericMapper<Tax, TaxDto> {
}
