package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.TempleDto;
import org.erp.vnoptic.service.TempleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/temple")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Temple", description = "Endpoints for user CRUD Temple")
public class TempleController {
    TempleService templeService;

    @GetMapping("/all")
    @Operation(summary = "get all temple available")
    public ApiResponse<?> getAllTemples() {
        return ApiResponse.success("success", templeService.getAllTemple());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination temple available")
    public ApiResponse<?> getPageTemples(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(templeService.getTemples(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getTempleById(@PathVariable("id") Long id) {
        return ApiResponse.success(templeService.getTempleById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createTemple(@RequestBody TempleDto templeDto) {
        return ApiResponse.success(templeService.createTemple(templeDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateTemple(@PathVariable("id") Long id, @RequestBody TempleDto templeDto) {
        return ApiResponse.success(templeService.updateTemple(id, templeDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteTemple(@PathVariable Long id) {
        return ApiResponse.success(templeService.deleteTempleById(id));
    }
}
