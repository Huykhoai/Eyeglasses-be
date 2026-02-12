package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.ColorDto;
import org.erp.vnoptic.service.ColorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/color")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Color", description = "Endpoints for user CRUD Color")
public class ColorController {
    ColorService colorService;

    @GetMapping("/all")
    @Operation(summary = "get all color available")
    public ApiResponse<?> getAllColors() {
        return ApiResponse.success("success", colorService.getAllColor());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination color available")
    public ApiResponse<?> getPageColors(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(colorService.getColors(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getColorById(@PathVariable("id") Long id) {
        return ApiResponse.success(colorService.getColorById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createColor(@RequestBody ColorDto colorDto) {
        return ApiResponse.success(colorService.createColor(colorDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateColor(@PathVariable("id") Long id, @RequestBody ColorDto colorDto) {
        return ApiResponse.success(colorService.updateColor(id, colorDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteColor(@PathVariable Long id) {
        return ApiResponse.success(colorService.deleteColorById(id));
    }
}
