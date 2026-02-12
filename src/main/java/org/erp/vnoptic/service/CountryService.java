package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.CountryDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface CountryService {
    List<CountryDto> getAllCountry();

    ConfigResponse<CountryDto> getCountries(Integer page, Integer size, String query);

    CountryDto getCountryById(Long id);

    Message createCountry(CountryDto countryDto);

    Message updateCountry(Long id, CountryDto countryDto);

    Message deleteCountryById(Long id);
}
