package com.recime.recipeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recime.recipeapi.model.RecipesIngredients;
import com.recime.recipeapi.model.RecipesIngredientsId;

@Repository
public interface RecipesIngredientsRepository extends JpaRepository<RecipesIngredients, RecipesIngredientsId> {
    void deleteByRecipe_RecipeId(Long recipeId);

    void deleteByIngredient_IngredientId(Long ingredientId);
}
