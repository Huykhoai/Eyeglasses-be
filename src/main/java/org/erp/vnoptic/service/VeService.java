package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.VeDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface VeService {
    List<VeDto> getAllVe();

    ConfigResponse<VeDto> getVes(Integer page, Integer size, String query);

    VeDto getVeById(Long id);

    Message createVe(VeDto veDto);

    Message updateVe(Long id, VeDto veDto);

    Message deleteVeById(Long id);
}
