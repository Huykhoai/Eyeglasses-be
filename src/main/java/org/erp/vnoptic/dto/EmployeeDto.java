package org.erp.vnoptic.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto extends BaseDto {
    private String email;
    private String username;
}
