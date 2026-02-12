package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.RefractiveIndexDto;
import org.erp.vnoptic.entity.RefractiveIndex;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.RefractiveIndexMapper;
import org.erp.vnoptic.repository.RefractiveIndexRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.RefractiveIndexService;
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
public class RefractiveIndexServiceImpl implements RefractiveIndexService {
    RefractiveIndexRepository refractiveIndexRepository;
    RefractiveIndexMapper refractiveIndexMapper;

    @Override
    public List<RefractiveIndexDto> getAllRefractiveIndex() {
        return refractiveIndexRepository.findAllAvailable().stream()
                .map(refractiveIndexMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<RefractiveIndexDto> getRefractiveIndices(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<RefractiveIndex> indices = refractiveIndexRepository.getPagination(query, pageable);
        List<RefractiveIndexDto> dtos = indices.stream().map(refractiveIndexMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) indices.getTotalElements());
    }

    @Override
    public RefractiveIndexDto getRefractiveIndexById(Long id) {
        RefractiveIndex index = refractiveIndexRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("RefractiveIndex with id " + id + " does not exist"));
        return refractiveIndexMapper.toDto(index);
    }

    @Override
    public Message createRefractiveIndex(RefractiveIndexDto dto) {
        if (refractiveIndexRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("RefractiveIndex with cid " + dto.getCid() + " already exists");
        }
        RefractiveIndex index = RefractiveIndex.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        refractiveIndexRepository.save(index);
        return new Message(200, "Created refractive index with cid " + index.getCid());
    }

    @Override
    public Message updateRefractiveIndex(Long id, RefractiveIndexDto dto) {
        RefractiveIndex index = refractiveIndexRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("RefractiveIndex with id " + id + " does not exist"));
        if (!index.getCid().equalsIgnoreCase(dto.getCid())) {
            if (refractiveIndexRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("RefractiveIndex with cid " + dto.getCid() + " already exists");
            }
        }
        index.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        index.setName(dto.getName());
        index.setDescription(dto.getDescription());
        index.setModifiedAt(LocalDateTime.now());
        refractiveIndexRepository.save(index);
        return new Message(200, "Updated refractive index with cid " + index.getCid());
    }

    @Override
    public Message deleteRefractiveIndexById(Long id) {
        RefractiveIndex index = refractiveIndexRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("RefractiveIndex with id " + id + " does not exist"));
        index.setDisContinue(true);
        index.setModifiedAt(LocalDateTime.now());
        refractiveIndexRepository.save(index);
        return new Message(200, "Deleted refractive index with cid " + index.getCid());
    }
}
