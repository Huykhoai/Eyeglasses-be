package org.erp.vnoptic.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EmployeeDto extends BaseDto {
    private String email;
    private String username;
}
