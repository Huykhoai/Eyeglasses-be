package org.erp.vnoptic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class GroupTypeDto extends BaseDto {
    private String description;
}
