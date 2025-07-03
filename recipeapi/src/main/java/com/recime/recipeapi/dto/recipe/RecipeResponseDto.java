package com.recime.recipeapi.dto.recipe;

import java.util.List;

import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientWithMeasuresDto;
import com.recime.recipeapi.dto.instruction.InstructionDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RecipeResponseDto extends RecipeDto {
    private Long recipeId;

    public RecipeResponseDto(Long recipeId, String title, String description, Integer servings,
            List<InstructionDto> instructions, List<RecipeIngredientWithMeasuresDto> ingredients) {
        super(title, description, servings, instructions, ingredients);
        this.recipeId = recipeId;
    }
}
