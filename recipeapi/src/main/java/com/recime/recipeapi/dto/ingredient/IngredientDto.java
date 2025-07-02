package com.recime.recipeapi.dto.ingredient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientDto {
    private String name;
    private Boolean isVegetarian;
}
