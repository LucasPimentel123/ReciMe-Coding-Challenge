package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.ingredient.IngredientDto;
import com.recime.recipeapi.mapper.IngredientMapper;
import com.recime.recipeapi.dto.RecipesIngredients.RecipesIngredientsWithMeasuresDto;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.repository.IngredientRepository;
import com.recime.recipeapi.repository.RecipesIngredientsRepository;

@Service
public class IngredientService implements ServiceInterface<Ingredient> {

    private final IngredientRepository repository;
    private final RecipesIngredientsRepository recipesIngredientsRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientRepository repository, RecipesIngredientsRepository recipesIngredientsRepository,
            IngredientMapper ingredientMapper) {
        this.repository = repository;
        this.recipesIngredientsRepository = recipesIngredientsRepository;
        this.ingredientMapper = ingredientMapper;
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
        Ingredient ingredient = ingredientMapper.toEntity(ingredientDto);
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
        recipesIngredientsRepository.deleteByIngredient_IngredientId(id);
        repository.deleteById(id);
    }

    @Transactional
    public Optional<Ingredient> findOrSaveDto(RecipesIngredientsWithMeasuresDto ingredientDto) {
        Ingredient ingredient = ingredientMapper.toEntity(ingredientDto);
        return repository.findByName(ingredient.getName())
                .or(() -> this.save(ingredient));
    }

}