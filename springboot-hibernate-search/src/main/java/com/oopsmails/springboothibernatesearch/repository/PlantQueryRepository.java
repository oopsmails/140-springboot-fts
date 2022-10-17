package com.oopsmails.springboothibernatesearch.repository;

import com.oopsmails.springboothibernatesearch.model.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantQueryRepository extends PagingAndSortingRepository<Plant, Long> {

    @Query(value = "SELECT id, created_at, family, name, scientific_name " +
            "FROM plant WHERE CONTAINS(name, ?1) ",
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM plant WHERE CONTAINS(name, ?1)"
    )
    Page<Plant> search(String keyword, Pageable pageable);

}
