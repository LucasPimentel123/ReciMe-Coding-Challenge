package com.recime.recipeapi.mapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientWithMeasuresDto;
import com.recime.recipeapi.dto.instruction.InstructionDto;
import com.recime.recipeapi.dto.recipe.RecipeDto;
import com.recime.recipeapi.dto.recipe.RecipeResponseDto;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.model.RecipeIngredient;

@Component
public class RecipeMapper {

    private final InstructionMapper instructionMapper;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public RecipeMapper(InstructionMapper instructionMapper, RecipeIngredientMapper recipeIngredientMapper) {
        this.instructionMapper = instructionMapper;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    public RecipeResponseDto toResponseDto(Recipe recipe, List<Instruction> instructions,
            List<RecipeIngredient> recipeIngredients) {

        List<InstructionDto> instructionDtos = instructions == null
                ? Collections.emptyList()
                : instructions.stream()
                        .map(instructionMapper::toDto)
                        .sorted(Comparator.comparing(InstructionDto::getStep))
                        .collect(Collectors.toList());

        List<RecipeIngredientWithMeasuresDto> ingredientWithMeasurementDtos = recipeIngredients == null
                ? Collections.emptyList()
                : recipeIngredients.stream()
                        .map(recipeIngredientMapper::toRecipeIngredientWithMeasuresDto)
                        .collect(Collectors.toList());

        return new RecipeResponseDto(recipe.getRecipeId(), recipe.getTitle(), recipe.getDescription(),
                recipe.getServings(), instructionDtos, ingredientWithMeasurementDtos);
    }

    public Recipe toEntity(RecipeDto recipeDto) {
        return new Recipe(null, recipeDto.getTitle(), recipeDto.getDescription(), recipeDto.getServings());
    }
}
