package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.VeDto;
import org.erp.vnoptic.service.VeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ve")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Ve", description = "Endpoints for user CRUD VE")
public class VeController {
    VeService veService;

    @GetMapping("/all")
    @Operation(summary = "get all ve available")
    public ApiResponse<?> getAllVes() {
        return ApiResponse.success("success", veService.getAllVe());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination ve available")
    public ApiResponse<?> getPageVes(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(veService.getVes(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getVeById(@PathVariable("id") Long id) {
        return ApiResponse.success(veService.getVeById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createVe(@RequestBody VeDto veDto) {
        return ApiResponse.success(veService.createVe(veDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateVe(@PathVariable("id") Long id, @RequestBody VeDto veDto) {
        return ApiResponse.success(veService.updateVe(id, veDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteVe(@PathVariable Long id) {
        return ApiResponse.success(veService.deleteVeById(id));
    }
}
