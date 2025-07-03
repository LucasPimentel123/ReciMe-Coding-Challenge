package com.recime.recipeapi.dto.RecipeIngredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RecipeIngredientWithMeasuresDto {
    private String name;
    private Boolean isVegetarian;
    private String metric;
    private Double quantity;
}
