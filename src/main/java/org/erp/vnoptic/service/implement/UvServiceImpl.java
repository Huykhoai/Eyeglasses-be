package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.UvDto;
import org.erp.vnoptic.entity.Uv;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.UvMapper;
import org.erp.vnoptic.repository.UvRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.UvService;
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
public class UvServiceImpl implements UvService {
    UvRepository uvRepository;
    UvMapper uvMapper;

    @Override
    public List<UvDto> getAllUv() {
        return uvRepository.findAllAvailable().stream()
                .map(uvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<UvDto> getUvs(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Uv> uvs = uvRepository.getPagination(query, pageable);
        List<UvDto> dtos = uvs.stream().map(uvMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) uvs.getTotalElements());
    }

    @Override
    public UvDto getUvById(Long id) {
        Uv uv = uvRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Uv with id " + id + " does not exist"));
        return uvMapper.toDto(uv);
    }

    @Override
    public Message createUv(UvDto dto) {
        if (uvRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Uv with cid " + dto.getCid() + " already exists");
        }
        Uv uv = Uv.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        uvRepository.save(uv);
        return new Message(200, "Created uv with cid " + uv.getCid());
    }

    @Override
    public Message updateUv(Long id, UvDto dto) {
        Uv uv = uvRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Uv with id " + id + " does not exist"));
        if (!uv.getCid().equalsIgnoreCase(dto.getCid())) {
            if (uvRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Uv with cid " + dto.getCid() + " already exists");
            }
        }
        uv.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        uv.setName(dto.getName());
        uv.setDescription(dto.getDescription());
        uv.setModifiedAt(LocalDateTime.now());
        uvRepository.save(uv);
        return new Message(200, "Updated uv with cid " + uv.getCid());
    }

    @Override
    public Message deleteUvById(Long id) {
        Uv uv = uvRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Uv with id " + id + " does not exist"));
        uv.setDisContinue(true);
        uv.setModifiedAt(LocalDateTime.now());
        uvRepository.save(uv);
        return new Message(200, "Deleted uv with cid " + uv.getCid());
    }
}
