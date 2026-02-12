package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.CountryDto;
import org.erp.vnoptic.entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper extends GenericMapper<Country, CountryDto> {
}
