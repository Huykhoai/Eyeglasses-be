package org.erp.vnoptic.responese;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigResponse<T> {
    List<T> data;
    Integer totalItems;
}
