package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.CoatingDto;
import org.erp.vnoptic.entity.Coating;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.CoatingMapper;
import org.erp.vnoptic.repository.CoatingRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.CoatingService;
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
public class CoatingServiceImpl implements CoatingService {
    CoatingRepository coatingRepository;
    CoatingMapper coatingMapper;

    @Override
    public List<CoatingDto> getAllCoating() {
        try {
            List<Coating> coatings = coatingRepository.findAllAvailable();
            return coatings.stream().map(coatingMapper::toDto).collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ConfigResponse<CoatingDto> getCoatings(Integer page, Integer size, String query) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
            Page<Coating> coatings = coatingRepository.getPagination(query, pageable);
            List<CoatingDto> dtos = coatings.stream().map(coatingMapper::toDto).collect(Collectors.toList());

            return new ConfigResponse<>(dtos, (int) coatings.getTotalElements());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CoatingDto getCoatingById(Long id) {
        try {
            Coating coating = coatingRepository.findByIdAvailable(id)
                    .orElseThrow(() -> new NoSuchElementException("Coating with id " + id + " does not exist"));

            return coatingMapper.toDto(coating);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Message createCoating(CoatingDto dto) {
        try {
            if (coatingRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Coating with cid " + dto.getCid() + " already exists");
            }

            Coating coating = Coating.builder()
                    .cid(dto.getCid().toUpperCase(Locale.ROOT))
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();

            coatingRepository.save(coating);
            return new Message(200, "Created coating with cid " + coating.getCid());
        } catch (DuplicateRecordException e) {
            throw new DuplicateRecordException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Message updateCoating(Long id, CoatingDto dto) {
        try {
            Coating coating = coatingRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Coating with id " + id + " does not exist"));

            if (!coating.getCid().equals(dto.getCid().toUpperCase())) {
                if (coatingRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                    throw new DuplicateRecordException("Coating with cid " + coating.getCid() + " already exists");
                }
            }
            coating.setCid(dto.getCid().toUpperCase(Locale.ROOT));
            coating.setName(dto.getName());
            coating.setDescription(dto.getDescription());
            coating.setModifiedAt(LocalDateTime.now());
            coatingRepository.save(coating);

            return new Message(200, "Updated coating with cid " + coating.getCid());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (DuplicateRecordException e) {
            throw new DuplicateRecordException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Message deleteCoatingById(Long id) {
        try {
            Coating coating = coatingRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Coating with id " + id + " does not exist"));

            coating.setDisContinue(Boolean.TRUE);
            coating.setModifiedAt(LocalDateTime.now());
            coatingRepository.save(coating);

            return new Message(200, "Deleted coating with cid " + coating.getCid());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
