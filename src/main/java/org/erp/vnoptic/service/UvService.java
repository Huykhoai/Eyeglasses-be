package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.UvDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface UvService {
    List<UvDto> getAllUv();

    ConfigResponse<UvDto> getUvs(Integer page, Integer size, String query);

    UvDto getUvById(Long id);

    Message createUv(UvDto uvDto);

    Message updateUv(Long id, UvDto uvDto);

    Message deleteUvById(Long id);
}
