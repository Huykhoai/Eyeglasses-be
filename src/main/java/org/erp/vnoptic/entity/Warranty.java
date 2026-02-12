package org.erp.vnoptic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "Warranty")
public class Warranty extends BaseEntity{
    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "cid", nullable = false, length = 50)
    private String cid;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(name = "\"value\"", nullable = false)
    private Integer value;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @ColumnDefault("'Month'")
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Size(max = 255)
    @Nationalized   
    @Column(name = "description")
    private String description;

    @NotNull
    @Builder.Default
    @Column(name = "dis_continue", nullable = false)
    private Boolean disContinue = false;
}