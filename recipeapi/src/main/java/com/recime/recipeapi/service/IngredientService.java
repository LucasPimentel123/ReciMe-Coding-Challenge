package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientWithMeasuresDto;
import com.recime.recipeapi.dto.ingredient.IngredientDto;
import com.recime.recipeapi.mapper.IngredientMapper;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.repository.IngredientRepository;
import com.recime.recipeapi.repository.RecipeIngredientRepository;

@Service
public class IngredientService implements ServiceInterface<Ingredient, IngredientDto> {

    private final IngredientRepository repository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientRepository repository, RecipeIngredientRepository recipeIngredientRepository,
            IngredientMapper ingredientMapper) {
        this.repository = repository;
        this.recipeIngredientRepository = recipeIngredientRepository;
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

    public Optional<Ingredient> saveDto(IngredientDto ingredientDto) {
        Ingredient ingredient = ingredientMapper.toEntity(ingredientDto);
        return this.save(ingredient);
    }

    public Optional<List<Ingredient>> getAll() {
        try {
            return Optional.of(repository.findAll());
        } catch (Exception ex) {
            return Optional.empty();
        }
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
        recipeIngredientRepository.deleteByIngredient_IngredientId(id);
        repository.deleteById(id);
    }

    @Transactional
    public Optional<Ingredient> findOrSaveDto(RecipeIngredientWithMeasuresDto ingredientDto) {
        Ingredient ingredient = ingredientMapper.toEntity(ingredientDto);
        return repository.findByName(ingredient.getName())
                .or(() -> this.save(ingredient));
    }

}