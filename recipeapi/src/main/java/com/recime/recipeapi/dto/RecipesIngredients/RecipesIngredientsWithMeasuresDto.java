package com.recime.recipeapi.dto.RecipesIngredients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RecipesIngredientsWithMeasuresDto {
    private String name;
    private Boolean isVegetarian;
    private String metric;
    private Double quantity;
}
