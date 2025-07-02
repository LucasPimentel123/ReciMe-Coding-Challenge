package com.recime.recipeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recime.recipeapi.model.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

}
