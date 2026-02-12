package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.CountryDto;
import org.erp.vnoptic.entity.Country;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.CountryMapper;
import org.erp.vnoptic.repository.CountryRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.CountryService;
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
public class CountryServiceImpl implements CountryService {
    CountryRepository countryRepository;
    CountryMapper countryMapper;

    @Override
    public List<CountryDto> getAllCountry() {
        return countryRepository.findAllAvailable().stream()
                .map(countryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<CountryDto> getCountries(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Country> countries = countryRepository.getPagination(query, pageable);
        List<CountryDto> dtos = countries.stream().map(countryMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) countries.getTotalElements());
    }

    @Override
    public CountryDto getCountryById(Long id) {
        Country country = countryRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Country with id " + id + " does not exist"));
        return countryMapper.toDto(country);
    }

    @Override
    public Message createCountry(CountryDto dto) {
        if (countryRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Country with cid " + dto.getCid() + " already exists");
        }
        Country country = Country.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        countryRepository.save(country);
        return new Message(200, "Created country with cid " + country.getCid());
    }

    @Override
    public Message updateCountry(Long id, CountryDto dto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Country with id " + id + " does not exist"));
        if (!country.getCid().equalsIgnoreCase(dto.getCid())) {
            if (countryRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Country with cid " + dto.getCid() + " already exists");
            }
        }
        country.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        country.setName(dto.getName());
        country.setDescription(dto.getDescription());
        country.setModifiedAt(LocalDateTime.now());
        countryRepository.save(country);
        return new Message(200, "Updated country with cid " + country.getCid());
    }

    @Override
    public Message deleteCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Country with id " + id + " does not exist"));
        country.setDisContinue(true);
        country.setModifiedAt(LocalDateTime.now());
        countryRepository.save(country);
        return new Message(200, "Deleted country with cid " + country.getCid());
    }
}
