package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.TaxDto;
import org.erp.vnoptic.service.TaxService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Tax", description = "Endpoints for user CRUD Tax")
public class TaxController {
    TaxService taxService;

    @GetMapping("/all")
    @Operation(summary = "get all tax available")
    public ApiResponse<?> getAllTaxes() {
        return ApiResponse.success("success", taxService.getAllTax());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination tax available")
    public ApiResponse<?> getPageTaxes(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(taxService.getTaxes(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getTaxById(@PathVariable("id") Long id) {
        return ApiResponse.success(taxService.getTaxById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createTax(@RequestBody TaxDto taxDto) {
        return ApiResponse.success(taxService.createTax(taxDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateTax(@PathVariable("id") Long id, @RequestBody TaxDto taxDto) {
        return ApiResponse.success(taxService.updateTax(id, taxDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteTax(@PathVariable Long id) {
        return ApiResponse.success(taxService.deleteTaxById(id));
    }
}
