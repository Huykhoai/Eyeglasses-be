package org.erp.vnoptic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
public class Employee extends BaseEntity {
    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "cid", nullable = false, length = 30)
    private String cid;

    @Size(max = 50)
    @Nationalized
    @Column(name = "email", length = 50)
    private String email;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @NotNull
    @Column(name = "status_em_id", nullable = false)
    private Integer statusEmId;

    @Column(name = "department_id")
    private Integer departmentId;

    @NotNull
    @Column(name = "dis_continue", nullable = false)
    private Boolean disContinue = false;
}