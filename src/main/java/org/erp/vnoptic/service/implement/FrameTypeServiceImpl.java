package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.FrameTypeDto;
import org.erp.vnoptic.entity.FrameType;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.FrameTypeMapper;
import org.erp.vnoptic.repository.FrameTypeRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.FrameTypeService;
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
public class FrameTypeServiceImpl implements FrameTypeService {
    FrameTypeRepository frameTypeRepository;
    FrameTypeMapper frameTypeMapper;

    @Override
    public List<FrameTypeDto> getAllFrameType() {
        return frameTypeRepository.findAllAvailable().stream()
                .map(frameTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<FrameTypeDto> getFrameTypes(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<FrameType> frameTypes = frameTypeRepository.getPagination(query, pageable);
        List<FrameTypeDto> dtos = frameTypes.stream().map(frameTypeMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) frameTypes.getTotalElements());
    }

    @Override
    public FrameTypeDto getFrameTypeById(Long id) {
        FrameType frameType = frameTypeRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("FrameType with id " + id + " does not exist"));
        return frameTypeMapper.toDto(frameType);
    }

    @Override
    public Message createFrameType(FrameTypeDto dto) {
        if (frameTypeRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("FrameType with cid " + dto.getCid() + " already exists");
        }
        FrameType frameType = FrameType.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        frameTypeRepository.save(frameType);
        return new Message(200, "Created frame type with cid " + frameType.getCid());
    }

    @Override
    public Message updateFrameType(Long id, FrameTypeDto dto) {
        FrameType frameType = frameTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("FrameType with id " + id + " does not exist"));
        if (!frameType.getCid().equalsIgnoreCase(dto.getCid())) {
            if (frameTypeRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("FrameType with cid " + dto.getCid() + " already exists");
            }
        }
        frameType.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        frameType.setName(dto.getName());
        frameType.setDescription(dto.getDescription());
        frameType.setModifiedAt(LocalDateTime.now());
        frameTypeRepository.save(frameType);
        return new Message(200, "Updated frame type with cid " + frameType.getCid());
    }

    @Override
    public Message deleteFrameTypeById(Long id) {
        FrameType frameType = frameTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("FrameType with id " + id + " does not exist"));
        frameType.setDisContinue(true);
        frameType.setModifiedAt(LocalDateTime.now());
        frameTypeRepository.save(frameType);
        return new Message(200, "Deleted frame type with cid " + frameType.getCid());
    }
}
