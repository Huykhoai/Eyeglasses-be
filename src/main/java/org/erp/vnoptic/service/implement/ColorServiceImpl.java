package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.ColorDto;
import org.erp.vnoptic.entity.Color;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.ColorMapper;
import org.erp.vnoptic.repository.ColorRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.ColorService;
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
public class ColorServiceImpl implements ColorService {
    ColorRepository colorRepository;
    ColorMapper colorMapper;

    @Override
    public List<ColorDto> getAllColor() {
        return colorRepository.findAllAvailable().stream()
                .map(colorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<ColorDto> getColors(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Color> colors = colorRepository.getPagination(query, pageable);
        List<ColorDto> dtos = colors.stream().map(colorMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) colors.getTotalElements());
    }

    @Override
    public ColorDto getColorById(Long id) {
        Color color = colorRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Color with id " + id + " does not exist"));
        return colorMapper.toDto(color);
    }

    @Override
    public Message createColor(ColorDto dto) {
        if (colorRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Color with cid " + dto.getCid() + " already exists");
        }
        Color color = Color.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        colorRepository.save(color);
        return new Message(200, "Created color with cid " + color.getCid());
    }

    @Override
    public Message updateColor(Long id, ColorDto dto) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Color with id " + id + " does not exist"));
        if (!color.getCid().equalsIgnoreCase(dto.getCid())) {
            if (colorRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Color with cid " + dto.getCid() + " already exists");
            }
        }
        color.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        color.setName(dto.getName());
        color.setDescription(dto.getDescription());
        color.setModifiedAt(LocalDateTime.now());
        colorRepository.save(color);
        return new Message(200, "Updated color with cid " + color.getCid());
    }

    @Override
    public Message deleteColorById(Long id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Color with id " + id + " does not exist"));
        color.setDisContinue(true);
        color.setModifiedAt(LocalDateTime.now());
        colorRepository.save(color);
        return new Message(200, "Deleted color with cid " + color.getCid());
    }
}
