package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.CoatingDto;
import org.erp.vnoptic.service.CoatingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coating")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Coating", description = "Endpoints for user CRUD Coating")
public class CoatingController {
    CoatingService coatingService;

    @GetMapping("/all")
    @Operation(summary = "get all coating available")
    public ApiResponse<?>  getAllCoatings() {
        return ApiResponse.success("success", coatingService.getAllCoating());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination coating available")
    public ApiResponse<?> getPageCoatings(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20")  int size,
            @RequestParam(value = "search", required = false) String query
    ) {
        return ApiResponse.success(coatingService.getCoatings(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getCoatingById(
            @PathVariable("id") Long id
    ){
        return ApiResponse.success(coatingService.getCoatingById(id));
    }
    @PostMapping("/create")
    public ApiResponse<?> createCoating(
            @RequestBody CoatingDto coatingDto
    ){
        return ApiResponse.success(coatingService.createCoating(coatingDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateCoating(
            @PathVariable("id") Long id,
            @RequestBody CoatingDto coatingDto
    ){
        return ApiResponse.success(coatingService.updateCoating(id, coatingDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteCoating(
            @PathVariable Long id
    ){
        return  ApiResponse.success(coatingService.deleteCoatingById(id));
    }
}
