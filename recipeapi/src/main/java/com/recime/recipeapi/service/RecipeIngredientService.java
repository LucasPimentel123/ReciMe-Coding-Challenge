package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientDto;
import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientIdDto;
import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientWithMeasuresDto;
import com.recime.recipeapi.mapper.RecipeIngredientMapper;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.model.RecipeIngredient;
import com.recime.recipeapi.model.RecipeIngredientId;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.IngredientRepository;
import com.recime.recipeapi.repository.RecipeRepository;
import com.recime.recipeapi.repository.RecipeIngredientRepository;

@Service
public class RecipeIngredientService implements ServiceInterface<RecipeIngredient> {

    private final RecipeIngredientRepository repository;
    private final IngredientService ingredientService;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public RecipeIngredientService(RecipeIngredientRepository repository,
            IngredientService ingredientService,
            RecipeRepository recipeRepository,
            RecipeIngredientMapper recipeIngredientMapper) {
        this.repository = repository;
        this.ingredientService = ingredientService;
        this.recipeRepository = recipeRepository;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    @Transactional
    public Optional<RecipeIngredient> save(RecipeIngredient recipeIngredient) {
        try {
            return Optional.of(repository.save(recipeIngredient));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<RecipeIngredient> save(List<RecipeIngredient> recipesIngredients) {
        try {
            return repository.saveAll(recipesIngredients);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public Optional<RecipeIngredient> save(RecipeIngredientDto recipeIngredientDto) {
        Optional<Ingredient> ingredient = ingredientService.getById(recipeIngredientDto.getIngredientId());
        Optional<Recipe> recipe = recipeRepository.findById(recipeIngredientDto.getRecipeId());
        if (ingredient.isPresent() && recipe.isPresent()) {
            RecipeIngredientId id = new RecipeIngredientId(ingredient.get().getIngredientId(),
                    recipe.get().getRecipeId());
            RecipeIngredient recipeIngredientEntity = recipeIngredientMapper.toEntity(id, ingredient.get(),
                    recipe.get(), recipeIngredientDto);
            return this.save(recipeIngredientEntity);
        }
        return Optional.empty();
    }

    public List<RecipeIngredient> getAll() {
        return repository.findAll();
    }

    public List<RecipeIngredientDto> getAllMappedToDto() {
        return repository.findAll().stream().map(recipeIngredientMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<RecipeIngredient> getById(Long ingredientId, Long recipeId) {
        RecipeIngredientId id = new RecipeIngredientId(ingredientId, recipeId);
        return repository.findById(id);
    }

    public Optional<RecipeIngredient> update(RecipeIngredientDto recipeIngredientDto) {
        Optional<RecipeIngredient> recipeIngredient = this.getById(recipeIngredientDto.getIngredientId(),
                recipeIngredientDto.getRecipeId());
        if (recipeIngredient.isPresent()) {
            recipeIngredient.get().setMetric(recipeIngredientDto.getMetric());
            recipeIngredient.get().setQuantity(recipeIngredientDto.getQuantity());
            return this.save(recipeIngredient.get());
        }
        return Optional.empty();
    }

    public void delete(RecipeIngredientIdDto recipeIngredientIdDto) {
        RecipeIngredientId recipeIngredientId = new RecipeIngredientId(
                recipeIngredientIdDto.getIngredientId(),
                recipeIngredientIdDto.getRecipeId());
        repository.deleteById(recipeIngredientId);
    }

    @Transactional
    public void deleteByRecipeId(Long recipeId) {
        repository.deleteByRecipe_RecipeId(recipeId);
    }

    @Transactional
    public void deleteByIngredientId(Long ingredientId) {
        repository.deleteByIngredient_IngredientId(ingredientId);
    }

    @Transactional
    public Optional<RecipeIngredient> saveDto(RecipeIngredientWithMeasuresDto ingredientDto,
            Ingredient ingredient, Recipe recipe) {

        RecipeIngredientId id = new RecipeIngredientId(ingredient.getIngredientId(), recipe.getRecipeId());

        RecipeIngredient recipeIngredient = recipeIngredientMapper.toEntity(id, ingredient, recipe,
                ingredientDto);

        Optional<RecipeIngredient> savedRecipeIngredient = this.save(recipeIngredient);

        if (savedRecipeIngredient.isPresent()) {
            return savedRecipeIngredient;
        } else {
            throw new RuntimeException(
                    "Error saving recipe ingredients: " + recipeIngredient.getIngredient().getName());
        }
    }

    @Transactional
    public void saveIngredientsAndRecipesMeasurements(RecipeIngredientWithMeasuresDto recipeIngredientWithMeasuresDto,
            Recipe recipe) {
        Optional<Ingredient> savedIngredient = ingredientService.findOrSaveDto(recipeIngredientWithMeasuresDto);

        if (savedIngredient.isPresent()) {
            this.saveDto(recipeIngredientWithMeasuresDto, savedIngredient.get(), recipe);
        } else {
            throw new RuntimeException("Error saving ingredient: " + recipeIngredientWithMeasuresDto.getName());
        }
    }
}
