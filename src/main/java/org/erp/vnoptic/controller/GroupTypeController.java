package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.GroupTypeDto;
import org.erp.vnoptic.service.GroupTypeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group-type")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "GroupType", description = "Endpoints for user CRUD GroupType")
public class GroupTypeController {
    GroupTypeService groupTypeService;

    @GetMapping("/all")
    @Operation(summary = "get all group type available")
    public ApiResponse<?> getAllGroupTypes() {
        return ApiResponse.success("success", groupTypeService.getAllGroupType());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination group type available")
    public ApiResponse<?> getPageGroupTypes(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(groupTypeService.getGroupTypes(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getGroupTypeById(@PathVariable("id") Long id) {
        return ApiResponse.success(groupTypeService.getGroupTypeById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createGroupType(@RequestBody GroupTypeDto groupTypeDto) {
        return ApiResponse.success(groupTypeService.createGroupType(groupTypeDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateGroupType(@PathVariable("id") Long id, @RequestBody GroupTypeDto groupTypeDto) {
        return ApiResponse.success(groupTypeService.updateGroupType(id, groupTypeDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteGroupType(@PathVariable Long id) {
        return ApiResponse.success(groupTypeService.deleteGroupTypeById(id));
    }
}
