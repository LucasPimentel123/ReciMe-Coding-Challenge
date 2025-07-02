package com.recime.recipeapi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.recime.recipeapi.dto.IngredientWithMeasurementDto;
import com.recime.recipeapi.model.RecepiesIngredients;
import com.recime.recipeapi.repository.RecepiesIngredientsRepository;

@Service
public class RecepiesIngredientsService {

    private final RecepiesIngredientsRepository repository;

    public RecepiesIngredientsService(RecepiesIngredientsRepository repository) {
        this.repository = repository;
    }

    public Optional<RecepiesIngredients> save(RecepiesIngredients e) {
        return Optional.of(repository.save(e));
    }

    public IngredientWithMeasurementDto mapRecepiesIngredientsToIngredientWithMeasurementDto(
            RecepiesIngredients recepiesIngredients) {
        IngredientWithMeasurementDto dto = new IngredientWithMeasurementDto();
        dto.setIngredientId(recepiesIngredients.getIngredient().getIngredientId());
        dto.setName(recepiesIngredients.getIngredient().getName());
        dto.setIsVegetarian(recepiesIngredients.getIngredient().getIsVegetarian());
        dto.setMetric(recepiesIngredients.getMetric());
        dto.setQuantity(recepiesIngredients.getQuantity());
        return dto;
    }
}
