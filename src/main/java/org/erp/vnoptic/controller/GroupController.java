package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.GroupDto;
import org.erp.vnoptic.service.GroupService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Group", description = "Endpoints for user CRUD Group")
public class GroupController {
    GroupService groupService;

    @GetMapping("/all")
    @Operation(summary = "get all group available")
    public ApiResponse<?> getAllGroups() {
        return ApiResponse.success("success", groupService.getAllGroup());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination group available")
    public ApiResponse<?> getPageGroups(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(groupService.getGroups(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getGroupById(@PathVariable("id") Long id) {
        return ApiResponse.success(groupService.getGroupById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createGroup(@RequestBody GroupDto groupDto) {
        return ApiResponse.success(groupService.createGroup(groupDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateGroup(@PathVariable("id") Long id, @RequestBody GroupDto groupDto) {
        return ApiResponse.success(groupService.updateGroup(id, groupDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteGroup(@PathVariable Long id) {
        return ApiResponse.success(groupService.deleteGroupById(id));
    }
}
