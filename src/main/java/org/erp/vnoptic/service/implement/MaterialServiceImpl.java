package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.MaterialDto;
import org.erp.vnoptic.entity.Material;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.MaterialMapper;
import org.erp.vnoptic.repository.MaterialRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.MaterialService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialServiceImpl implements MaterialService {
    MaterialRepository materialRepository;
    MaterialMapper materialMapper;

    @Override
    public List<MaterialDto> getAllMaterial() {
        return materialRepository.findAllAvailable().stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<MaterialDto> getMaterials(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Material> materials = materialRepository.getPagination(query, pageable);
        List<MaterialDto> dtos = materials.stream().map(materialMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) materials.getTotalElements());
    }

    @Override
    public MaterialDto getMaterialById(Long id) {
        Material material = materialRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Material with id " + id + " does not exist"));
        return materialMapper.toDto(material);
    }

    @Override
    public Message createMaterial(MaterialDto dto) {
        if (materialRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Material with cid " + dto.getCid() + " already exists");
        }
        Material material = Material.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        materialRepository.save(material);
        return new Message(200, "Created material with cid " + material.getCid());
    }

    @Override
    public Message updateMaterial(Long id, MaterialDto dto) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Material with id " + id + " does not exist"));
        if (!material.getCid().equalsIgnoreCase(dto.getCid())) {
            if (materialRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Material with cid " + dto.getCid() + " already exists");
            }
        }
        material.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        material.setName(dto.getName());
        material.setDescription(dto.getDescription());
        material.setModifiedAt(LocalDateTime.now());
        materialRepository.save(material);
        return new Message(200, "Updated material with cid " + material.getCid());
    }

    @Override
    public Message deleteMaterialById(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Material with id " + id + " does not exist"));
        material.setDisContinue(true);
        material.setModifiedAt(LocalDateTime.now());
        materialRepository.save(material);
        return new Message(200, "Deleted material with cid " + material.getCid());
    }
}
