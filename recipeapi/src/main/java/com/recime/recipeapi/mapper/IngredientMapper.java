package com.recime.recipeapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientWithMeasuresDto;
import com.recime.recipeapi.dto.ingredient.IngredientDto;
import com.recime.recipeapi.model.Ingredient;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientDto toDto(Ingredient ingredient);

    @Mapping(target = "ingredientId", ignore = true)
    @Mapping(target = "name", source = "ingredientDto.name")
    @Mapping(target = "isVegetarian", source = "ingredientDto.isVegetarian")
    Ingredient toEntity(IngredientDto ingredientDto);

    @Mapping(target = "ingredientId", ignore = true)
    @Mapping(target = "name", source = "recipeIngredientWithMeasuresDto.name")
    @Mapping(target = "isVegetarian", source = "recipeIngredientWithMeasuresDto.isVegetarian")
    Ingredient toEntity(RecipeIngredientWithMeasuresDto recipeIngredientWithMeasuresDto);

}
