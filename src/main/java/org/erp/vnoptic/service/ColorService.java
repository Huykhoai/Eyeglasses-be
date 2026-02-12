package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.ColorDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface ColorService {
    List<ColorDto> getAllColor();

    ConfigResponse<ColorDto> getColors(Integer page, Integer size, String query);

    ColorDto getColorById(Long id);

    Message createColor(ColorDto colorDto);

    Message updateColor(Long id, ColorDto colorDto);

    Message deleteColorById(Long id);
}
