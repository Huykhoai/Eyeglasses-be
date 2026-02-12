package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.GroupDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface GroupService {
    List<GroupDto> getAllGroup();

    ConfigResponse<GroupDto> getGroups(Integer page, Integer size, String query);

    GroupDto getGroupById(Long id);

    Message createGroup(GroupDto groupDto);

    Message updateGroup(Long id, GroupDto groupDto);

    Message deleteGroupById(Long id);
}
