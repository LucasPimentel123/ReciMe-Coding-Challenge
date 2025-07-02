package com.recime.recipeapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.repository.IngredientRepository;

@Service
public class IngredientService {

    private final IngredientRepository repository;

    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
    }

    public List<Ingredient> getAllIngredients() {
        return repository.findAll();
    }

    public Optional<Ingredient> save(Ingredient e) {
        return Optional.of(repository.save(e));
    }
}