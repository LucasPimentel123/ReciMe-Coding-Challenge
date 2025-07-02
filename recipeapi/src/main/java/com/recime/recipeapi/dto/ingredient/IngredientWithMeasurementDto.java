package com.recime.recipeapi.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientWithMeasurementDto {
    private String name;
    private Boolean isVegetarian;
    private String metric;
    private Double quantity;
}
