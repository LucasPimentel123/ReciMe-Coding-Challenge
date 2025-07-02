package com.recime.recipeapi.dto.RecipesIngredients;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecipesIngredientsIdDto {
    private Long ingredientId;
    private Long recipeId;
}
