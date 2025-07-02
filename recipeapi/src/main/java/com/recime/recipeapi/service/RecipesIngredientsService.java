package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.RecipesIngredients.RecipesIngredientsDto;
import com.recime.recipeapi.dto.RecipesIngredients.RecipesIngredientsIdDto;
import com.recime.recipeapi.dto.ingredient.IngredientWithMeasurementDto;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.model.RecipesIngredients;
import com.recime.recipeapi.model.RecipesIngredientsId;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.IngredientRepository;
import com.recime.recipeapi.repository.RecipeRepository;
import com.recime.recipeapi.repository.RecipesIngredientsRepository;

@Service
public class RecipesIngredientsService implements ServiceInterface<RecipesIngredients> {

    private final RecipesIngredientsRepository repository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public RecipesIngredientsService(RecipesIngredientsRepository repository,
            IngredientRepository ingredientRepository,
            RecipeRepository recipeRepository) {
        this.repository = repository;
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public Optional<RecipesIngredients> save(RecipesIngredients recepiesIngredients) {
        try {
            return Optional.of(repository.save(recepiesIngredients));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<RecipesIngredients> save(List<RecipesIngredients> recepiesIngredients) {
        try {
            return repository.saveAll(recepiesIngredients);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public Optional<RecipesIngredients> save(RecipesIngredientsDto recipiesIngredientsDto) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(recipiesIngredientsDto.getIngredientId());
        Optional<Recipe> recipe = recipeRepository.findById(recipiesIngredientsDto.getRecipeId());
        if (ingredient.isPresent() && recipe.isPresent()) {
            RecipesIngredientsId id = new RecipesIngredientsId(ingredient.get().getIngredientId(),
                    recipe.get().getRecipeId());
            return this.save(new RecipesIngredients(id, recipiesIngredientsDto.getMetric(),
                    recipiesIngredientsDto.getQuantity(), ingredient.get(), recipe.get()));
        }
        return Optional.empty();
    }

    public List<RecipesIngredients> getAll() {
        return repository.findAll();
    }

    public List<RecipesIngredientsDto> getAllMappedToDto() {
        return repository.findAll().stream().map(this::mapRecepiesIngredientsToDto)
                .collect(Collectors.toList());
    }

    public RecipesIngredientsDto mapRecepiesIngredientsToDto(RecipesIngredients recepiesIngredients) {
        RecipesIngredientsDto dto = new RecipesIngredientsDto();
        dto.setIngredientId(recepiesIngredients.getIngredient().getIngredientId());
        dto.setRecipeId(recepiesIngredients.getRecipe().getRecipeId());
        dto.setMetric(recepiesIngredients.getMetric());
        dto.setQuantity(recepiesIngredients.getQuantity());
        return dto;
    }

    public Optional<RecipesIngredients> getById(Long ingredientId, Long recipeId) {
        RecipesIngredientsId id = new RecipesIngredientsId(ingredientId, recipeId);
        return repository.findById(id);
    }

    public Optional<RecipesIngredients> update(RecipesIngredientsDto recepiesIngredientsDto) {
        Optional<RecipesIngredients> recepiesIngredients = this.getById(recepiesIngredientsDto.getIngredientId(),
                recepiesIngredientsDto.getRecipeId());
        if (recepiesIngredients.isPresent()) {
            recepiesIngredients.get().setMetric(recepiesIngredientsDto.getMetric());
            recepiesIngredients.get().setQuantity(recepiesIngredientsDto.getQuantity());
            return this.save(recepiesIngredients.get());
        }
        return Optional.empty();
    }

    public void delete(RecipesIngredientsIdDto recepiesIngredientsIdDto) {
        RecipesIngredientsId recepiesIngredientsId = new RecipesIngredientsId(
                recepiesIngredientsIdDto.getIngredientId(),
                recepiesIngredientsIdDto.getRecipeId());
        repository.deleteById(recepiesIngredientsId);
    }

    @Transactional
    public void deleteByRecipeId(Long recipeId) {
        repository.deleteByRecipe_RecipeId(recipeId);
    }

    @Transactional
    public void deleteByIngredientId(Long ingredientId) {
        repository.deleteByIngredient_IngredientId(ingredientId);
    }

    public IngredientWithMeasurementDto mapRecepiesIngredientsToIngredientWithMeasurementDto(
            RecipesIngredients recepiesIngredients) {
        IngredientWithMeasurementDto dto = new IngredientWithMeasurementDto();
        dto.setName(recepiesIngredients.getIngredient().getName());
        dto.setIsVegetarian(recepiesIngredients.getIngredient().getIsVegetarian());
        dto.setMetric(recepiesIngredients.getMetric());
        dto.setQuantity(recepiesIngredients.getQuantity());
        return dto;
    }

    @Transactional
    public Optional<RecipesIngredients> mapDtoAndSave(IngredientWithMeasurementDto ingredientDto,
            Ingredient ingredient, Recipe recipe) {

        RecipesIngredientsId id = new RecipesIngredientsId(ingredient.getIngredientId(), recipe.getRecipeId());

        RecipesIngredients recepiesIngredients = new RecipesIngredients(id, ingredientDto.getMetric(),
                ingredientDto.getQuantity(), ingredient, recipe);

        Optional<RecipesIngredients> savedRecepiesIngredients = this.save(recepiesIngredients);

        if (savedRecepiesIngredients.isPresent()) {
            return savedRecepiesIngredients;
        } else {
            throw new RuntimeException(
                    "Error saving recepies ingredients: " + recepiesIngredients.getIngredient().getName());
        }
    }
}
