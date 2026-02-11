package org.erp.vnoptic.repository;

import org.erp.vnoptic.entity.AccountRole;
import org.erp.vnoptic.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Integer> {

    @Query("SELECT ar.role.name FROM AccountRole ar WHERE ar.account.id = :accountId")
    List<String> findRoleNamesByAccountId(Long accountId);

    @Query("SELECT ar.role.id FROM AccountRole ar WHERE ar.account.id = :accountId")
    List<Integer> findRoleIdsByAccountId(Long accountId);


    @Query("""
    SELECT r FROM AccountRole ar
    JOIN ar.role r
    JOIN ar.account a
    WHERE a.id = :accountId
    """)
    List<Role> findAllByAccount_Id(Long accountId);
}
