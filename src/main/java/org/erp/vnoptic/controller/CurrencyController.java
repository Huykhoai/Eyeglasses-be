package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.CurrencyDto;
import org.erp.vnoptic.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Currency", description = "Endpoints for user CRUD Currency")
public class CurrencyController {
    CurrencyService currencyService;

    @GetMapping("/all")
    @Operation(summary = "get all currency available")
    public ApiResponse<?> getAllCurrencies() {
        return ApiResponse.success("success", currencyService.getAllCurrency());
    }

    @GetMapping("/page/{page}")
    @Operation(summary = "get pagination currency available")
    public ApiResponse<?> getPageCurrencies(
            @PathVariable int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String query) {
        return ApiResponse.success(currencyService.getCurrencies(page, size, query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id available")
    public ApiResponse<?> getCurrencyById(@PathVariable("id") Long id) {
        return ApiResponse.success(currencyService.getCurrencyById(id));
    }

    @PostMapping("/create")
    public ApiResponse<?> createCurrency(@RequestBody CurrencyDto currencyDto) {
        return ApiResponse.success(currencyService.createCurrency(currencyDto));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateCurrency(@PathVariable("id") Long id, @RequestBody CurrencyDto currencyDto) {
        return ApiResponse.success(currencyService.updateCurrency(id, currencyDto));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteCurrency(@PathVariable Long id) {
        return ApiResponse.success(currencyService.deleteCurrencyById(id));
    }
}
