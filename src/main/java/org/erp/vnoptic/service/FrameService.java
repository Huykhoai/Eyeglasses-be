package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.FrameDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface FrameService {
    List<FrameDto> getAllFrame();

    ConfigResponse<FrameDto> getFrames(Integer page, Integer size, String query);

    FrameDto getFrameById(Long id);

    Message createFrame(FrameDto frameDto);

    Message updateFrame(Long id, FrameDto frameDto);

    Message deleteFrameById(Long id);
}
