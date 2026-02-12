package org.erp.vnoptic.mapper;

import org.erp.vnoptic.dto.CurrencyDto;
import org.erp.vnoptic.entity.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper extends GenericMapper<Currency, CurrencyDto> {
}
