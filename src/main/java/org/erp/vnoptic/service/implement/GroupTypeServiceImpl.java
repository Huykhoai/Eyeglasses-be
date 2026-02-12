package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.GroupTypeDto;
import org.erp.vnoptic.entity.GroupType;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.GroupTypeMapper;
import org.erp.vnoptic.repository.GroupTypeRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.GroupTypeService;
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
public class GroupTypeServiceImpl implements GroupTypeService {
    GroupTypeRepository groupTypeRepository;
    GroupTypeMapper groupTypeMapper;

    @Override
    public List<GroupTypeDto> getAllGroupType() {
        return groupTypeRepository.findAllAvailable().stream()
                .map(groupTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<GroupTypeDto> getGroupTypes(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<GroupType> groupTypes = groupTypeRepository.getPagination(query, pageable);
        List<GroupTypeDto> dtos = groupTypes.stream().map(groupTypeMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) groupTypes.getTotalElements());
    }

    @Override
    public GroupTypeDto getGroupTypeById(Long id) {
        GroupType groupType = groupTypeRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("GroupType with id " + id + " does not exist"));
        return groupTypeMapper.toDto(groupType);
    }

    @Override
    public Message createGroupType(GroupTypeDto dto) {
        if (groupTypeRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("GroupType with cid " + dto.getCid() + " already exists");
        }
        GroupType groupType = GroupType.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        groupTypeRepository.save(groupType);
        return new Message(200, "Created group type with cid " + groupType.getCid());
    }

    @Override
    public Message updateGroupType(Long id, GroupTypeDto dto) {
        GroupType groupType = groupTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("GroupType with id " + id + " does not exist"));
        if (!groupType.getCid().equalsIgnoreCase(dto.getCid())) {
            if (groupTypeRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("GroupType with cid " + dto.getCid() + " already exists");
            }
        }
        groupType.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        groupType.setName(dto.getName());
        groupType.setDescription(dto.getDescription());
        groupType.setModifiedAt(LocalDateTime.now());
        groupTypeRepository.save(groupType);
        return new Message(200, "Updated group type with cid " + groupType.getCid());
    }

    @Override
    public Message deleteGroupTypeById(Long id) {
        GroupType groupType = groupTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("GroupType with id " + id + " does not exist"));
        groupType.setDisContinue(true);
        groupType.setModifiedAt(LocalDateTime.now());
        groupTypeRepository.save(groupType);
        return new Message(200, "Deleted group type with cid " + groupType.getCid());
    }
}
