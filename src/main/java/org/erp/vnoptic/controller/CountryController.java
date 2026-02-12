package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.CountryDto;
import org.erp.vnoptic.service.CountryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/country")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Country", description = "Endpoints for user CRUD Country")
public class CountryController {
    CountryService countryService;

    @GetMapping("/all")
    @Operation(summary = "get all country available")
    public ApiResponse<?> getAllCountries() {
        return ApiResponse.success("success", countryService.getAllCountry());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination country available")
    public ApiResponse<?> getPageCountries(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(countryService.getCountries(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getCountryById(@PathVariable("id") Long id) {
        return ApiResponse.success(countryService.getCountryById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createCountry(@RequestBody CountryDto countryDto) {
        return ApiResponse.success(countryService.createCountry(countryDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateCountry(@PathVariable("id") Long id, @RequestBody CountryDto countryDto) {
        return ApiResponse.success(countryService.updateCountry(id, countryDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteCountry(@PathVariable Long id) {
        return ApiResponse.success(countryService.deleteCountryById(id));
    }
}
