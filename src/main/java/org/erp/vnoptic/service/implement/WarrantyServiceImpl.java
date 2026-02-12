package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.WarrantyDto;
import org.erp.vnoptic.entity.Warranty;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.WarrantyMapper;
import org.erp.vnoptic.repository.WarrantyRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.WarrantyService;
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
public class WarrantyServiceImpl implements WarrantyService {
    WarrantyRepository warrantyRepository;
    WarrantyMapper warrantyMapper;

    @Override
    public List<WarrantyDto> getAllWarranty() {
        return warrantyRepository.findAllAvailable().stream()
                .map(warrantyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<WarrantyDto> getWarranties(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Warranty> warranties = warrantyRepository.getPagination(query, pageable);
        List<WarrantyDto> dtos = warranties.stream().map(warrantyMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) warranties.getTotalElements());
    }

    @Override
    public WarrantyDto getWarrantyById(Long id) {
        Warranty warranty = warrantyRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Warranty with id " + id + " does not exist"));
        return warrantyMapper.toDto(warranty);
    }

    @Override
    public Message createWarranty(WarrantyDto dto) {
        if (warrantyRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Warranty with cid " + dto.getCid() + " already exists");
        }
        Warranty warranty = Warranty.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .value(dto.getValue())
                .type(dto.getType())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        warrantyRepository.save(warranty);
        return new Message(200, "Created warranty with cid " + warranty.getCid());
    }

    @Override
    public Message updateWarranty(Long id, WarrantyDto dto) {
        Warranty warranty = warrantyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Warranty with id " + id + " does not exist"));
        if (!warranty.getCid().equalsIgnoreCase(dto.getCid())) {
            if (warrantyRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Warranty with cid " + dto.getCid() + " already exists");
            }
        }
        warranty.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        warranty.setName(dto.getName());
        warranty.setValue(dto.getValue());
        warranty.setType(dto.getType());
        warranty.setDescription(dto.getDescription());
        warranty.setModifiedAt(LocalDateTime.now());
        warrantyRepository.save(warranty);
        return new Message(200, "Updated warranty with cid " + warranty.getCid());
    }

    @Override
    public Message deleteWarrantyById(Long id) {
        Warranty warranty = warrantyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Warranty with id " + id + " does not exist"));
        warranty.setDisContinue(true);
        warranty.setModifiedAt(LocalDateTime.now());
        warrantyRepository.save(warranty);
        return new Message(200, "Deleted warranty with cid " + warranty.getCid());
    }
}
