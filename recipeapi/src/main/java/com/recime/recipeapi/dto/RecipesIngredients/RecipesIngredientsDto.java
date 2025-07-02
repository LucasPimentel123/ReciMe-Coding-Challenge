package com.recime.recipeapi.dto.RecipesIngredients;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecipesIngredientsDto {
    private Long ingredientId;
    private Long recipeId;
    private String metric;
    private Double quantity;
}
