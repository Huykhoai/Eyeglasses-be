package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.FrameTypeDto;
import org.erp.vnoptic.service.FrameTypeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/frame-type")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "FrameType", description = "Endpoints for user CRUD FrameType")
public class FrameTypeController {
    FrameTypeService frameTypeService;

    @GetMapping("/all")
    @Operation(summary = "get all frame type available")
    public ApiResponse<?> getAllFrameTypes() {
        return ApiResponse.success("success", frameTypeService.getAllFrameType());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination frame type available")
    public ApiResponse<?> getPageFrameTypes(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(frameTypeService.getFrameTypes(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getFrameTypeById(@PathVariable("id") Long id) {
        return ApiResponse.success(frameTypeService.getFrameTypeById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createFrameType(@RequestBody FrameTypeDto frameTypeDto) {
        return ApiResponse.success(frameTypeService.createFrameType(frameTypeDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateFrameType(@PathVariable("id") Long id, @RequestBody FrameTypeDto frameTypeDto) {
        return ApiResponse.success(frameTypeService.updateFrameType(id, frameTypeDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteFrameType(@PathVariable Long id) {
        return ApiResponse.success(frameTypeService.deleteFrameTypeById(id));
    }
}
