package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.FrameTypeDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface FrameTypeService {
    List<FrameTypeDto> getAllFrameType();

    ConfigResponse<FrameTypeDto> getFrameTypes(Integer page, Integer size, String query);

    FrameTypeDto getFrameTypeById(Long id);

    Message createFrameType(FrameTypeDto frameTypeDto);

    Message updateFrameType(Long id, FrameTypeDto frameTypeDto);

    Message deleteFrameTypeById(Long id);
}
