package com.recime.recipeapi.dto.RecipeIngredient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecipeIngredientIdDto {
    private Long ingredientId;
    private Long recipeId;
}
