package org.erp.vnoptic.mapper;

import org.mapstruct.Named;

public interface GenericMapper<E, D> {
    @Named("toDto")
    D toDto(E entity);
    @Named("toEntity")
    E toEntity(D dto);
}
