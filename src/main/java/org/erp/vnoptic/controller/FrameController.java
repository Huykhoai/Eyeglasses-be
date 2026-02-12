package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.FrameDto;
import org.erp.vnoptic.service.FrameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/frame")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Frame", description = "Endpoints for user CRUD Frame")
public class FrameController {
    FrameService frameService;

    @GetMapping("/all")
    @Operation(summary = "get all frame available")
    public ApiResponse<?> getAllFrames() {
        return ApiResponse.success("success", frameService.getAllFrame());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination frame available")
    public ApiResponse<?> getPageFrames(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(frameService.getFrames(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getFrameById(@PathVariable("id") Long id) {
        return ApiResponse.success(frameService.getFrameById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createFrame(@RequestBody FrameDto frameDto) {
        return ApiResponse.success(frameService.createFrame(frameDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateFrame(@PathVariable("id") Long id, @RequestBody FrameDto frameDto) {
        return ApiResponse.success(frameService.updateFrame(id, frameDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteFrame(@PathVariable Long id) {
        return ApiResponse.success(frameService.deleteFrameById(id));
    }
}
