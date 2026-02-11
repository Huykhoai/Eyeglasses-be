package org.erp.vnoptic.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDto {
    private Long id;
    private String cid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
