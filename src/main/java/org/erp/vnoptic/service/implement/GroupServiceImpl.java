package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.dto.GroupDto;
import org.erp.vnoptic.entity.Group;
import org.erp.vnoptic.entity.GroupType;
import org.erp.vnoptic.exception.DuplicateRecordException;
import org.erp.vnoptic.mapper.GroupMapper;
import org.erp.vnoptic.repository.GroupRepository;
import org.erp.vnoptic.repository.GroupTypeRepository;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;
import org.erp.vnoptic.service.GroupService;
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
public class GroupServiceImpl implements GroupService {
    GroupRepository groupRepository;
    GroupTypeRepository groupTypeRepository;
    GroupMapper groupMapper;

    @Override
    public List<GroupDto> getAllGroup() {
        return groupRepository.findAllAvailable().stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse<GroupDto> getGroups(Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Group> groups = groupRepository.getPagination(query, pageable);
        List<GroupDto> dtos = groups.stream().map(groupMapper::toDto).collect(Collectors.toList());
        return new ConfigResponse<>(dtos, (int) groups.getTotalElements());
    }

    @Override
    public GroupDto getGroupById(Long id) {
        Group group = groupRepository.findByIdAvailable(id)
                .orElseThrow(() -> new NoSuchElementException("Group with id " + id + " does not exist"));
        return groupMapper.toDto(group);
    }

    @Override
    public Message createGroup(GroupDto dto) {
        if (groupRepository.findByCidAvailable(dto.getCid()).isPresent()) {
            throw new DuplicateRecordException("Group with cid " + dto.getCid() + " already exists");
        }
        GroupType groupType = groupTypeRepository.findById(dto.getType().getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "GroupType with id " + dto.getType().getId() + " does not exist"));

        Group group = Group.builder()
                .cid(dto.getCid().toUpperCase(Locale.ROOT))
                .name(dto.getName())
                .description(dto.getDescription())
                .groupType(groupType)
                .disContinue(false)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        groupRepository.save(group);
        return new Message(200, "Created group with cid " + group.getCid());
    }

    @Override
    public Message updateGroup(Long id, GroupDto dto) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Group with id " + id + " does not exist"));
        if (!group.getCid().equalsIgnoreCase(dto.getCid())) {
            if (groupRepository.findByCidAvailable(dto.getCid()).isPresent()) {
                throw new DuplicateRecordException("Group with cid " + dto.getCid() + " already exists");
            }
        }

        GroupType groupType = groupTypeRepository.findById(dto.getType().getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "GroupType with id " + dto.getType().getId() + " does not exist"));

        group.setCid(dto.getCid().toUpperCase(Locale.ROOT));
        group.setName(dto.getName());
        group.setDescription(dto.getDescription());
        group.setGroupType(groupType);
        group.setModifiedAt(LocalDateTime.now());
        groupRepository.save(group);
        return new Message(200, "Updated group with cid " + group.getCid());
    }

    @Override
    public Message deleteGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Group with id " + id + " does not exist"));
        group.setDisContinue(true);
        group.setModifiedAt(LocalDateTime.now());
        groupRepository.save(group);
        return new Message(200, "Deleted group with cid " + group.getCid());
    }
}
