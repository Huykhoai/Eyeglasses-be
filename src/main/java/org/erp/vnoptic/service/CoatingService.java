package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.CoatingDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface CoatingService {
    List<CoatingDto> getAllCoating();
    ConfigResponse<CoatingDto> getCoatings(Integer page, Integer size, String query);
    CoatingDto getCoatingById(Long id);
    Message createCoating(CoatingDto coatingDto);
    Message updateCoating(Long id, CoatingDto coatingDto);
    Message deleteCoatingById(Long id);
}
