package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.DesignDto;
import org.erp.vnoptic.entity.Design;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.DesignMapper;
import org.erp.vnoptic.repository.DesignRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.DesignService;
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
public class DesignServiceImpl implements DesignService {
    DesignRepository designRepository;
    DesignMapper designMapper;

    @Override
    public List<DesignDto> getAllDesign() {
        return designRepository.findAllAvailable().stream()
                .map(designMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<DesignDto> getDesigns(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Design> designs = designRepository.getPagination(query, pageable);
        List<DesignDto> dtos = designs.stream().map(designMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) designs.getTotalElements());
    }

    @Override
    public DesignDto getDesignById(Long id) {
        Design design = designRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Design with id " + id + " does not exist"));
        return designMapper.toDto(design);
    }

    @Override
    public Message createDesign(DesignDto dto) {
        if (designRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Design with cid " + dto.getCid() + " already exists");
        }
        Design design = Design.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        designRepository.save(design);
        return new Message(200, "Created design with cid " + design.getCid());
    }

    @Override
    public Message updateDesign(Long id, DesignDto dto) {
        Design design = designRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Design with id " + id + " does not exist"));
        if (!design.getCid().equalsIgnoreCase(dto.getCid())) {
            if (designRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Design with cid " + dto.getCid() + " already exists");
            }
        }
        design.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        design.setName(dto.getName());
        design.setDescription(dto.getDescription());
        design.setModifiedAt(LocalDateTime.now());
        designRepository.save(design);
        return new Message(200, "Updated design with cid " + design.getCid());
    }

    @Override
    public Message deleteDesignById(Long id) {
        Design design = designRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Design with id " + id + " does not exist"));
        design.setDisContinue(true);
        design.setModifiedAt(LocalDateTime.now());
        designRepository.save(design);
        return new Message(200, "Deleted design with cid " + design.getCid());
    }
}
