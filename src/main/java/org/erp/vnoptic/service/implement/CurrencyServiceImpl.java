package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.CurrencyDto;
import org.erp.vnoptic.entity.Currency;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.CurrencyMapper;
import org.erp.vnoptic.repository.CurrencyRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.CurrencyService;
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
public class CurrencyServiceImpl implements CurrencyService {
    CurrencyRepository currencyRepository;
    CurrencyMapper currencyMapper;

    @Override
    public List<CurrencyDto> getAllCurrency() {
        return currencyRepository.findAllAvailable().stream()
                .map(currencyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<CurrencyDto> getCurrencies(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Currency> currencies = currencyRepository.getPagination(query, pageable);
        List<CurrencyDto> dtos = currencies.stream().map(currencyMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) currencies.getTotalElements());
    }

    @Override
    public CurrencyDto getCurrencyById(Long id) {
        Currency currency = currencyRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Currency with id " + id + " does not exist"));
        return currencyMapper.toDto(currency);
    }

    @Override
    public Message createCurrency(CurrencyDto dto) {
        if (currencyRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Currency with cid " + dto.getCid() + " already exists");
        }
        Currency currency = Currency.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        currencyRepository.save(currency);
        return new Message(200, "Created currency with cid " + currency.getCid());
    }

    @Override
    public Message updateCurrency(Long id, CurrencyDto dto) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Currency with id " + id + " does not exist"));
        if (!currency.getCid().equalsIgnoreCase(dto.getCid())) {
            if (currencyRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Currency with cid " + dto.getCid() + " already exists");
            }
        }
        currency.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        currency.setName(dto.getName());
        currency.setDescription(dto.getDescription());
        currency.setModifiedAt(LocalDateTime.now());
        currencyRepository.save(currency);
        return new Message(200, "Updated currency with cid " + currency.getCid());
    }

    @Override
    public Message deleteCurrencyById(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Currency with id " + id + " does not exist"));
        currency.setDisContinue(true);
        currency.setModifiedAt(LocalDateTime.now());
        currencyRepository.save(currency);
        return new Message(200, "Deleted currency with cid " + currency.getCid());
    }
}
