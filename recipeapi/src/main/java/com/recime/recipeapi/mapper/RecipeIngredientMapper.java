package com.recime.recipeapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientDto;
import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientWithMeasuresDto;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.model.RecipeIngredient;
import com.recime.recipeapi.model.RecipeIngredientId;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {

        @Mapping(target = "ingredientId", source = "recipeIngredient.ingredient.ingredientId")
        @Mapping(target = "recipeId", source = "recipeIngredient.recipe.recipeId")
        @Mapping(target = "metric", source = "recipeIngredient.metric")
        @Mapping(target = "quantity", source = "recipeIngredient.quantity")
        RecipeIngredientDto toDto(RecipeIngredient recipeIngredient);

        @Mapping(target = "name", source = "recipeIngredient.ingredient.name")
        @Mapping(target = "isVegetarian", source = "recipeIngredient.ingredient.isVegetarian")
        @Mapping(target = "metric", source = "recipeIngredient.metric")
        @Mapping(target = "quantity", source = "recipeIngredient.quantity")
        RecipeIngredientWithMeasuresDto toRecipeIngredientWithMeasuresDto(
                        RecipeIngredient recipeIngredient);

        @Mapping(target = "id", source = "id")
        @Mapping(target = "ingredient", source = "ingredient")
        @Mapping(target = "recipe", source = "recipe")
        @Mapping(target = "metric", source = "recipeIngredientWithMeasuresDto.metric")
        @Mapping(target = "quantity", source = "recipeIngredientWithMeasuresDto.quantity")
        RecipeIngredient toEntity(RecipeIngredientId id, Ingredient ingredient, Recipe recipe,
                        RecipeIngredientWithMeasuresDto recipeIngredientWithMeasuresDto);

        @Mapping(target = "id", source = "id")
        @Mapping(target = "ingredient", source = "ingredient")
        @Mapping(target = "recipe", source = "recipe")
        @Mapping(target = "metric", source = "recipeIngredientDto.metric")
        @Mapping(target = "quantity", source = "recipeIngredientDto.quantity")
        RecipeIngredient toEntity(RecipeIngredientId id, Ingredient ingredient, Recipe recipe,
                        RecipeIngredientDto recipeIngredientDto);

}
