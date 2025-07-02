package com.recime.recipeapi.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeUpdateDto {
    private String title;
    private String description;
    private Integer servings;
}
