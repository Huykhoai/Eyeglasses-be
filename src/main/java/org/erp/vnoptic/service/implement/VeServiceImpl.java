package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.VeDto;
import org.erp.vnoptic.entity.Ve;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.VeMapper;
import org.erp.vnoptic.repository.VeRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.VeService;
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
public class VeServiceImpl implements VeService {
    VeRepository veRepository;
    VeMapper veMapper;

    @Override
    public List<VeDto> getAllVe() {
        return veRepository.findAllAvailable().stream()
                .map(veMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<VeDto> getVes(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Ve> ves = veRepository.getPagination(query, pageable);
        List<VeDto> dtos = ves.stream().map(veMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) ves.getTotalElements());
    }

    @Override
    public VeDto getVeById(Long id) {
        Ve ve = veRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Ve with id " + id + " does not exist"));
        return veMapper.toDto(ve);
    }

    @Override
    public Message createVe(VeDto dto) {
        if (veRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Ve with cid " + dto.getCid() + " already exists");
        }
        Ve ve = Ve.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        veRepository.save(ve);
        return new Message(200, "Created ve with cid " + ve.getCid());
    }

    @Override
    public Message updateVe(Long id, VeDto dto) {
        Ve ve = veRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ve with id " + id + " does not exist"));
        if (!ve.getCid().equalsIgnoreCase(dto.getCid())) {
            if (veRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Ve with cid " + dto.getCid() + " already exists");
            }
        }
        ve.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        ve.setName(dto.getName());
        ve.setDescription(dto.getDescription());
        ve.setModifiedAt(LocalDateTime.now());
        veRepository.save(ve);
        return new Message(200, "Updated ve with cid " + ve.getCid());
    }

    @Override
    public Message deleteVeById(Long id) {
        Ve ve = veRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ve with id " + id + " does not exist"));
        ve.setDisContinue(true);
        ve.setModifiedAt(LocalDateTime.now());
        veRepository.save(ve);
        return new Message(200, "Deleted ve with cid " + ve.getCid());
    }
}
