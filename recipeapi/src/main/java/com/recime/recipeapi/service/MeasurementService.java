package com.recime.recipeapi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.recime.recipeapi.dto.IngredientWithMeasurementDto;
import com.recime.recipeapi.model.Measurement;
import com.recime.recipeapi.repository.MeasurementRepository;

@Service
public class MeasurementService {

    private final MeasurementRepository repository;

    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    public Optional<Measurement> save(Measurement e) {
        return Optional.of(repository.save(e));
    }

    public IngredientWithMeasurementDto mapMeasurementToIngredientWithMeasurementDto(Measurement measurement) {
        IngredientWithMeasurementDto dto = new IngredientWithMeasurementDto();
        dto.setIngredientId(measurement.getIngredient().getIngredientId());
        dto.setName(measurement.getIngredient().getName());
        dto.setIsVegetarian(measurement.getIngredient().getIsVegetarian());
        dto.setMetric(measurement.getMetric());
        dto.setQuantity(measurement.getQuantity());
        return dto;
    }
}
