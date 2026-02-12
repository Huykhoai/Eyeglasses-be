package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.DesignDto;
import org.erp.vnoptic.service.DesignService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/design")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Design", description = "Endpoints for user CRUD Design")
public class DesignController {
    DesignService designService;

    @GetMapping("/all")
    @Operation(summary = "get all design available")
    public ApiResponse<?> getAllDesigns() {
        return ApiResponse.success("success", designService.getAllDesign());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination design available")
    public ApiResponse<?> getPageDesigns(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(designService.getDesigns(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getDesignById(@PathVariable("id") Long id) {
        return ApiResponse.success(designService.getDesignById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createDesign(@RequestBody DesignDto designDto) {
        return ApiResponse.success(designService.createDesign(designDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateDesign(@PathVariable("id") Long id, @RequestBody DesignDto designDto) {
        return ApiResponse.success(designService.updateDesign(id, designDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteDesign(@PathVariable Long id) {
        return ApiResponse.success(designService.deleteDesignById(id));
    }
}
