package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.FrameDto;
import org.erp.vnoptic.entity.Frame;
import org.erp.vnoptic.entity.FrameType;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.FrameMapper;
import org.erp.vnoptic.repository.FrameRepository;
import org.erp.vnoptic.repository.FrameTypeRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.FrameService;
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
public class FrameServiceImpl implements FrameService {
    FrameRepository frameRepository;
    FrameMapper frameMapper;
    FrameTypeRepository frameTypeRepository;
    @Override
    public List<FrameDto> getAllFrame() {
        return frameRepository.findAllAvailable().stream()
                .map(frameMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<FrameDto> getFrames(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Frame> frames = frameRepository.getPagination(query, pageable);
        List<FrameDto> dtos = frames.stream().map(frameMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) frames.getTotalElements());
    }

    @Override
    public FrameDto getFrameById(Long id) {
        Frame frame = frameRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Frame with id " + id + " does not exist"));
        return frameMapper.toDto(frame);
    }

    @Override
    public Message createFrame(FrameDto dto) {
        if (frameRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Frame with cid " + dto.getCid() + " already exists");
        }

        FrameType frameType = frameTypeRepository.findByIdAvailable(dto.getType().getId())
                .orElseThrow(() -> new NoSuchElementException("Type with id " + dto.getType().getId() + " does not exist"));

        Frame frame = Frame.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .frameType(frameType)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        frameRepository.save(frame);
        return new Message(200, "Created frame with cid " + frame.getCid());
    }

    @Override
    public Message updateFrame(Long id, FrameDto dto) {
        Frame frame = frameRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Frame with id " + id + " does not exist"));
        if (!frame.getCid().equalsIgnoreCase(dto.getCid())) {
            if (frameRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Frame with cid " + dto.getCid() + " already exists");
            }
        }
        FrameType frameType = frameTypeRepository.findByIdAvailable(dto.getType().getId())
                        .orElseThrow(() -> new NoSuchElementException("Type with id " + dto.getType().getId() + " does not exist"));

        frame.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        frame.setName(dto.getName());
        frame.setDescription(dto.getDescription());
        frame.setFrameType(frameType);
        frame.setModifiedAt(LocalDateTime.now());
        frameRepository.save(frame);
        return new Message(200, "Updated frame with cid " + frame.getCid());
    }

    @Override
    public Message deleteFrameById(Long id) {
        Frame frame = frameRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Frame with id " + id + " does not exist"));
        frame.setDisContinue(true);
        frame.setModifiedAt(LocalDateTime.now());
        frameRepository.save(frame);
        return new Message(200, "Deleted frame with cid " + frame.getCid());
    }
}
