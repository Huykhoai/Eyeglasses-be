package org.erp.vnoptic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.disContinue = false")
    List<T> findAllAvailable();

    @Query("""
                SELECT e FROM #{#entityName} e
                WHERE e.disContinue = false
                AND (:query IS NULL OR (
                    lower(e.cid) like lower(concat('%', :query, '%')) OR
                    lower(e.name) like lower(concat('%', :query, '%'))
                ))
            """)
    Page<T> getPagination(@Param("query") String query, Pageable pageable);

    @Query("SELECT e FROM #{#entityName} e WHERE e.disContinue = false AND lower(e.cid) = lower(:cid)")
    Optional<T> findByCidAvailable(@Param("cid") String cid);

    @Query("SELECT e FROM #{#entityName} e WHERE e.disContinue = false AND e.id = :id")
    Optional<T> findByIdAvailable(@Param("id") ID id);
}
