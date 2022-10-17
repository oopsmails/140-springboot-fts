package com.oopsmails.springboothibernatesearch.repository;

import com.oopsmails.springboothibernatesearch.model.Plant;
import org.springframework.stereotype.Repository;


@Repository
public interface PlantRepository extends SearchRepository<Plant, Long> {
}
