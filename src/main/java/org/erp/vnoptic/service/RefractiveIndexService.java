package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.RefractiveIndexDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface RefractiveIndexService {
    List<RefractiveIndexDto> getAllRefractiveIndex();

    ConfigResponse<RefractiveIndexDto> getRefractiveIndices(Integer page, Integer size, String query);

    RefractiveIndexDto getRefractiveIndexById(Long id);

    Message createRefractiveIndex(RefractiveIndexDto refractiveIndexDto);

    Message updateRefractiveIndex(Long id, RefractiveIndexDto refractiveIndexDto);

    Message deleteRefractiveIndexById(Long id);
}
