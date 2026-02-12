package org.erp.vnoptic.controller;

import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.dto.BaseDto;
import org.erp.vnoptic.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    
    @GetMapping("/me")
    public ApiResponse<BaseDto> getAccount(
        @AuthenticationPrincipal UserPrincipal user
    ) {
        BaseDto baseDto = BaseDto.builder()
                .id(user.getId())
                .name(user.getUsername()).build();
        return ApiResponse.success("Account fetched successfully", baseDto);
    }
}
