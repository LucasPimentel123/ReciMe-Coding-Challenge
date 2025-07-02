package com.recime.recipeapi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponseDto {
    private Long recipeId;
    private String title;
    private String description;
    private Integer servings;
    private List<InstructionDto> instructions;
    private List<IngredientWithMeasurementDto> ingredients;
}
