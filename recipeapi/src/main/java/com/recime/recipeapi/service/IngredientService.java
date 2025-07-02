package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.ingredient.IngredientDto;
import com.recime.recipeapi.dto.ingredient.IngredientWithMeasurementDto;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.IngredientRepository;

@Service
public class IngredientService implements ServiceInterface<Ingredient> {

    private final IngredientRepository repository;
    private final RecipesIngredientsService recepiesIngredientsService;

    public IngredientService(IngredientRepository repository, RecipesIngredientsService recepiesIngredientsService) {
        this.repository = repository;
        this.recepiesIngredientsService = recepiesIngredientsService;
    }

    @Transactional
    public Optional<Ingredient> save(Ingredient ingredient) {
        try {
            return Optional.of(repository.save(ingredient));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<Ingredient> save(List<Ingredient> ingredients) {
        try {
            return repository.saveAll(ingredients);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public Optional<Ingredient> save(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient(null, ingredientDto.getName(), ingredientDto.getIsVegetarian());
        return this.save(ingredient);
    }

    public List<Ingredient> getAllIngredients() {
        return repository.findAll();
    }

    public Optional<Ingredient> getById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Optional<Ingredient> update(Long id, IngredientDto ingredientDto) {
        Optional<Ingredient> ingredient = this.getById(id);
        if (ingredient.isPresent()) {
            ingredient.get().setName(ingredientDto.getName());
            ingredient.get().setIsVegetarian(ingredientDto.getIsVegetarian());
            return this.save(ingredient.get());
        }
        return Optional.empty();
    }

    @Transactional
    public void delete(Long id) {
        recepiesIngredientsService.deleteByIngredientId(id);
        repository.deleteById(id);
    }

    @Transactional
    public void saveIngredientsAndRecipesMeasurements(IngredientWithMeasurementDto ingredientDto, Recipe recipe) {
        Ingredient ingredient = mapIngredientWithMeasurementDtoToEntity(ingredientDto);
        Optional<Ingredient> savedIngredient = repository.findByName(ingredient.getName())
                .or(() -> this.save(ingredient));

        if (savedIngredient.isPresent()) {
            recepiesIngredientsService.mapDtoAndSave(ingredientDto, savedIngredient.get(), recipe);
        } else {
            throw new RuntimeException("Error saving ingredient: " + ingredient.getName());
        }
    }

    public Ingredient mapIngredientWithMeasurementDtoToEntity(IngredientWithMeasurementDto ingredientDto) {
        return new Ingredient(null, ingredientDto.getName(), ingredientDto.getIsVegetarian());
    }
}