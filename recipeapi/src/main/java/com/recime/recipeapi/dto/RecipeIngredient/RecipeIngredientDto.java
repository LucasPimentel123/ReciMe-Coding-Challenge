package com.recime.recipeapi.dto.RecipeIngredient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecipeIngredientDto {
    private Long ingredientId;
    private Long recipeId;
    private String metric;
    private Double quantity;
}
