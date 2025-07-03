package com.recime.recipeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recime.recipeapi.model.RecipeIngredient;
import com.recime.recipeapi.model.RecipeIngredientId;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {
    void deleteByRecipe_RecipeId(Long recipeId);

    void deleteByIngredient_IngredientId(Long ingredientId);
}
