package com.recime.recipeapi.dto.recipe;

import java.util.List;

import com.recime.recipeapi.dto.ingredient.IngredientWithMeasurementDto;
import com.recime.recipeapi.dto.instruction.InstructionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private String title;
    private String description;
    private Integer servings;
    private List<InstructionDto> instructions;
    private List<IngredientWithMeasurementDto> ingredients;
}