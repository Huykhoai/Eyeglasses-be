package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.WarrantyDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface WarrantyService {
    List<WarrantyDto> getAllWarranty();

    ConfigResponse<WarrantyDto> getWarranties(Integer page, Integer size, String query);

    WarrantyDto getWarrantyById(Long id);

    Message createWarranty(WarrantyDto warrantyDto);

    Message updateWarranty(Long id, WarrantyDto warrantyDto);

    Message deleteWarrantyById(Long id);
}
