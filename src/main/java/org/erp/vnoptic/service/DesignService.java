package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.DesignDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface DesignService {
    List<DesignDto> getAllDesign();

    ConfigResponse<DesignDto> getDesigns(Integer page, Integer size, String query);

    DesignDto getDesignById(Long id);

    Message createDesign(DesignDto designDto);

    Message updateDesign(Long id, DesignDto designDto);

    Message deleteDesignById(Long id);
}
