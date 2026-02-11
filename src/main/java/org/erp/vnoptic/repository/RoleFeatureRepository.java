package org.erp.vnoptic.repository;

import org.erp.vnoptic.entity.RoleFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleFeatureRepository extends JpaRepository<RoleFeature, Integer> {

    @Query("SELECT DISTINCT rf.feature.url FROM RoleFeature rf WHERE rf.role.id IN :roleIds")
    List<String> findFeatureUrlsByRoleIds(List<Integer> roleIds);
}
