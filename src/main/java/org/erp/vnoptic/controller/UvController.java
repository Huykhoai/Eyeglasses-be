package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.UvDto;
import org.erp.vnoptic.service.UvService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uv")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Uv", description = "Endpoints for user CRUD UV")
public class UvController {
    UvService uvService;

    @GetMapping("/all")
    @Operation(summary = "get all uv available")
    public ApiResponse<?> getAllUvs() {
        return ApiResponse.success("success", uvService.getAllUv());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination uv available")
    public ApiResponse<?> getPageUvs(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(uvService.getUvs(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getUvById(@PathVariable("id") Long id) {
        return ApiResponse.success(uvService.getUvById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createUv(@RequestBody UvDto uvDto) {
        return ApiResponse.success(uvService.createUv(uvDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateUv(@PathVariable("id") Long id, @RequestBody UvDto uvDto) {
        return ApiResponse.success(uvService.updateUv(id, uvDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteUv(@PathVariable Long id) {
        return ApiResponse.success(uvService.deleteUvById(id));
    }
}
