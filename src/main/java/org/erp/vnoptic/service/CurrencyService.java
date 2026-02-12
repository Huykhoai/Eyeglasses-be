package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.CurrencyDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface CurrencyService {
    List<CurrencyDto> getAllCurrency();

    ConfigResponse<CurrencyDto> getCurrencies(Integer page, Integer size, String query);

    CurrencyDto getCurrencyById(Long id);

    Message createCurrency(CurrencyDto currencyDto);

    Message updateCurrency(Long id, CurrencyDto currencyDto);

    Message deleteCurrencyById(Long id);
}
