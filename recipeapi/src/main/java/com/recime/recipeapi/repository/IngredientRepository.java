package com.recime.recipeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recime.recipeapi.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}