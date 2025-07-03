package com.recime.recipeapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.recime.recipeapi.dto.RecipesIngredients.RecipesIngredientsDto;
import com.recime.recipeapi.dto.RecipesIngredients.RecipesIngredientsWithMeasuresDto;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.model.RecipesIngredients;
import com.recime.recipeapi.model.RecipesIngredientsId;

@Mapper(componentModel = "spring")
public interface RecipesIngredientsMapper {

    @Mapping(target = "ingredientId", source = "recipesIngredients.ingredient.ingredientId")
    @Mapping(target = "recipeId", source = "recipesIngredients.recipe.recipeId")
    @Mapping(target = "metric", source = "recipesIngredients.metric")
    @Mapping(target = "quantity", source = "recipesIngredients.quantity")
    RecipesIngredientsDto toDto(RecipesIngredients recipesIngredients);

    @Mapping(target = "name", source = "recipesIngredients.ingredient.name")
    @Mapping(target = "isVegetarian", source = "recipesIngredients.ingredient.isVegetarian")
    @Mapping(target = "metric", source = "recipesIngredients.metric")
    @Mapping(target = "quantity", source = "recipesIngredients.quantity")
    RecipesIngredientsWithMeasuresDto toRecipesIngredientsWithMeasuresDto(
            RecipesIngredients recipesIngredients);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "ingredient", source = "ingredient")
    @Mapping(target = "recipe", source = "recipe")
    @Mapping(target = "metric", source = "recipesIngredientsWithMeasuresDto.metric")
    @Mapping(target = "quantity", source = "recipesIngredientsWithMeasuresDto.quantity")
    RecipesIngredients toEntity(RecipesIngredientsId id, Ingredient ingredient, Recipe recipe,
            RecipesIngredientsWithMeasuresDto recipesIngredientsWithMeasuresDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "ingredient", source = "ingredient")
    @Mapping(target = "recipe", source = "recipe")
    @Mapping(target = "metric", source = "recipesIngredientsDto.metric")
    @Mapping(target = "quantity", source = "recipesIngredientsDto.quantity")
    RecipesIngredients toEntity(RecipesIngredientsId id, Ingredient ingredient, Recipe recipe,
            RecipesIngredientsDto recipesIngredientsDto);

}
