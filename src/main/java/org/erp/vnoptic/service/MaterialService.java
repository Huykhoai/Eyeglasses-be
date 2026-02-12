package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.MaterialDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface MaterialService {
    List<MaterialDto> getAllMaterial();

    ConfigResponse<MaterialDto> getMaterials(Integer page, Integer size, String query);

    MaterialDto getMaterialById(Long id);

    Message createMaterial(MaterialDto materialDto);

    Message updateMaterial(Long id, MaterialDto materialDto);

    Message deleteMaterialById(Long id);
}
