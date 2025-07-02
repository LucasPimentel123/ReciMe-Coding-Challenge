package com.recime.recipeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recime.recipeapi.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
