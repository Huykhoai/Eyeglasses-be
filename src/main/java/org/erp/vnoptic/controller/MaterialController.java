package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.MaterialDto;
import org.erp.vnoptic.service.MaterialService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Material", description = "Endpoints for user CRUD Material")
public class MaterialController {
    MaterialService materialService;

    @GetMapping("/all")
    @Operation(summary = "get all material available")
    public ApiResponse<?> getAllMaterials() {
        return ApiResponse.success("success", materialService.getAllMaterial());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination material available")
    public ApiResponse<?> getPageMaterials(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(materialService.getMaterials(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getMaterialById(@PathVariable("id") Long id) {
        return ApiResponse.success(materialService.getMaterialById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createMaterial(@RequestBody MaterialDto materialDto) {
        return ApiResponse.success(materialService.createMaterial(materialDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateMaterial(@PathVariable("id") Long id, @RequestBody MaterialDto materialDto) {
        return ApiResponse.success(materialService.updateMaterial(id, materialDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteMaterial(@PathVariable Long id) {
        return ApiResponse.success(materialService.deleteMaterialById(id));
    }
}
