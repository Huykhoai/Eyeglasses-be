package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.WarrantyDto;
import org.erp.vnoptic.service.WarrantyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warranty")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Warranty", description = "Endpoints for user CRUD Warranty")
public class WarrantyController {
    WarrantyService warrantyService;

    @GetMapping("/all")
    @Operation(summary = "get all warranty available")
    public ApiResponse<?> getAllWarranties() {
        return ApiResponse.success("success", warrantyService.getAllWarranty());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination warranty available")
    public ApiResponse<?> getPageWarranties(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(warrantyService.getWarranties(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getWarrantyById(@PathVariable("id") Long id) {
        return ApiResponse.success(warrantyService.getWarrantyById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createWarranty(@RequestBody WarrantyDto warrantyDto) {
        return ApiResponse.success(warrantyService.createWarranty(warrantyDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateWarranty(@PathVariable("id") Long id, @RequestBody WarrantyDto warrantyDto) {
        return ApiResponse.success(warrantyService.updateWarranty(id, warrantyDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteWarranty(@PathVariable Long id) {
        return ApiResponse.success(warrantyService.deleteWarrantyById(id));
    }
}
