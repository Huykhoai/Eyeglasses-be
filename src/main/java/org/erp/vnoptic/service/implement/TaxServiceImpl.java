package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.TaxDto;
import org.erp.vnoptic.entity.Tax;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.TaxMapper;
import org.erp.vnoptic.repository.TaxRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.TaxService;
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
public class TaxServiceImpl implements TaxService {
    TaxRepository taxRepository;
    TaxMapper taxMapper;

    @Override
    public List<TaxDto> getAllTax() {
        return taxRepository.findAllAvailable().stream()
                .map(taxMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<TaxDto> getTaxes(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Tax> taxes = taxRepository.getPagination(query, pageable);
        List<TaxDto> dtos = taxes.stream().map(taxMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) taxes.getTotalElements());
    }

    @Override
    public TaxDto getTaxById(Long id) {
        Tax tax = taxRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Tax with id " + id + " does not exist"));
        return taxMapper.toDto(tax);
    }

    @Override
    public Message createTax(TaxDto dto) {
        if (taxRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Tax with cid " + dto.getCid() + " already exists");
        }
        Tax tax = Tax.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .taxRate(dto.getTaxRate())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        taxRepository.save(tax);
        return new Message(200, "Created tax with cid " + tax.getCid());
    }

    @Override
    public Message updateTax(Long id, TaxDto dto) {
        Tax tax = taxRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tax with id " + id + " does not exist"));
        if (!tax.getCid().equalsIgnoreCase(dto.getCid())) {
            if (taxRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Tax with cid " + dto.getCid() + " already exists");
            }
        }
        tax.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        tax.setName(dto.getName());
        tax.setTaxRate(dto.getTaxRate());
        tax.setModifiedAt(LocalDateTime.now());
        taxRepository.save(tax);
        return new Message(200, "Updated tax with cid " + tax.getCid());
    }

    @Override
    public Message deleteTaxById(Long id) {
        Tax tax = taxRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tax with id " + id + " does not exist"));
        tax.setDisContinue(true);
        tax.setModifiedAt(LocalDateTime.now());
        taxRepository.save(tax);
        return new Message(200, "Deleted tax with cid " + tax.getCid());
    }
}
