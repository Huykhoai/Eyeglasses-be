package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.TaxDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface TaxService {
    List<TaxDto> getAllTax();

    ConfigResponse<TaxDto> getTaxes(Integer page, Integer size, String query);

    TaxDto getTaxById(Long id);

    Message createTax(TaxDto taxDto);

    Message updateTax(Long id, TaxDto taxDto);

    Message deleteTaxById(Long id);
}
