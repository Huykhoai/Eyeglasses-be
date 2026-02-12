package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.RefractiveIndexDto;
import org.erp.vnoptic.service.RefractiveIndexService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refractive-index")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "RefractiveIndex", description = "Endpoints for user CRUD RefractiveIndex")
public class RefractiveIndexController {
    RefractiveIndexService refractiveIndexService;

    @GetMapping("/all")
    @Operation(summary = "get all refractive index available")
    public ApiResponse<?> getAllRefractiveIndices() {
        return ApiResponse.success("success", refractiveIndexService.getAllRefractiveIndex());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination refractive index available")
    public ApiResponse<?> getPageRefractiveIndices(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(refractiveIndexService.getRefractiveIndices(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getRefractiveIndexById(@PathVariable("id") Long id) {
        return ApiResponse.success(refractiveIndexService.getRefractiveIndexById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createRefractiveIndex(@RequestBody RefractiveIndexDto refractiveIndexDto) {
        return ApiResponse.success(refractiveIndexService.createRefractiveIndex(refractiveIndexDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateRefractiveIndex(@PathVariable("id") Long id,
            @RequestBody RefractiveIndexDto refractiveIndexDto) {
        return ApiResponse.success(refractiveIndexService.updateRefractiveIndex(id, refractiveIndexDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteRefractiveIndex(@PathVariable Long id) {
        return ApiResponse.success(refractiveIndexService.deleteRefractiveIndexById(id));
    }
}
