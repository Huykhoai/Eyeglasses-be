package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.TempleDto;
import org.erp.vnoptic.entity.Temple;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.TempleMapper;
import org.erp.vnoptic.repository.TempleRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.TempleService;
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
public class TempleServiceImpl implements TempleService {
    TempleRepository templeRepository;
    TempleMapper templeMapper;

    @Override
    public List<TempleDto> getAllTemple() {
        return templeRepository.findAllAvailable().stream()
                .map(templeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<TempleDto> getTemples(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Temple> temples = templeRepository.getPagination(query, pageable);
        List<TempleDto> dtos = temples.stream().map(templeMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) temples.getTotalElements());
    }

    @Override
    public TempleDto getTempleById(Long id) {
        Temple temple = templeRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Temple with id " + id + " does not exist"));
        return templeMapper.toDto(temple);
    }

    @Override
    public Message createTemple(TempleDto dto) {
        if (templeRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Temple with cid " + dto.getCid() + " already exists");
        }
        Temple temple = Temple.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        templeRepository.save(temple);
        return new Message(200, "Created temple with cid " + temple.getCid());
    }

    @Override
    public Message updateTemple(Long id, TempleDto dto) {
        Temple temple = templeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Temple with id " + id + " does not exist"));
        if (!temple.getCid().equalsIgnoreCase(dto.getCid())) {
            if (templeRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Temple with cid " + dto.getCid() + " already exists");
            }
        }
        temple.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        temple.setName(dto.getName());
        temple.setDescription(dto.getDescription());
        temple.setModifiedAt(LocalDateTime.now());
        templeRepository.save(temple);
        return new Message(200, "Updated temple with cid " + temple.getCid());
    }

    @Override
    public Message deleteTempleById(Long id) {
        Temple temple = templeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Temple with id " + id + " does not exist"));
        temple.setDisContinue(true);
        temple.setModifiedAt(LocalDateTime.now());
        templeRepository.save(temple);
        return new Message(200, "Deleted temple with cid " + temple.getCid());
    }
}
