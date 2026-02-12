package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.GroupTypeDto;
import org.erp.vnoptic.responese.ConfigResponse;
import org.erp.vnoptic.responese.Message;

import java.util.List;

public interface GroupTypeService {
    List<GroupTypeDto> getAllGroupType();

    ConfigResponse<GroupTypeDto> getGroupTypes(Integer page, Integer size, String query);

    GroupTypeDto getGroupTypeById(Long id);

    Message createGroupType(GroupTypeDto groupTypeDto);

    Message updateGroupType(Long id, GroupTypeDto groupTypeDto);

    Message deleteGroupTypeById(Long id);
}
