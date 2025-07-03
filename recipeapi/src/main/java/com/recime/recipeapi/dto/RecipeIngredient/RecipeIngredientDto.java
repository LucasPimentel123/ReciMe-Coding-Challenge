package com.recime.recipeapi.dto.RecipeIngredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDto {
    private Long ingredientId;
    private Long recipeId;
    private String metric;
    private Double quantity;
}
