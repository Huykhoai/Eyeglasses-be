package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.TempleDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface TempleService {
    List<TempleDto> getAllTemple();

    ConfigResponse<TempleDto> getTemples(Integer page, Integer size, String query);

    TempleDto getTempleById(Long id);

    Message createTemple(TempleDto templeDto);

    Message updateTemple(Long id, TempleDto templeDto);

    Message deleteTempleById(Long id);
}
