package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.recime.recipeapi.dto.IngredientWithMeasurementDto;
import com.recime.recipeapi.dto.InstructionDto;
import com.recime.recipeapi.dto.RecipeResponseDto;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.model.RecepiesIngredients;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.RecipeRepository;

@Service
public class RecipeService {

    private final RecipeRepository repository;
    private final InstructionService instructionService;
    private final RecepiesIngredientsService recepiesIngredientsService;

    public RecipeService(RecipeRepository repository, InstructionService instructionService,
            RecepiesIngredientsService recepiesIngredientsService) {
        this.repository = repository;
        this.instructionService = instructionService;
        this.recepiesIngredientsService = recepiesIngredientsService;
    }

    public List<RecipeResponseDto> getAllRecipes() {
        Optional<List<Recipe>> recipes = Optional.of(repository.findAll());

        if (recipes.isPresent()) {
            return recipes.get().stream().map(recipe -> {
                return mapRecipeToDto(recipe, recipe.getInstructions(), recipe.getRecepiesIngredients());
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public RecipeResponseDto getRecipeById(Long id) {
        Optional<Recipe> recipe = repository.findById(id);
        if (recipe.isPresent()) {
            RecipeResponseDto dto = mapRecipeToDto(recipe.get(), recipe.get().getInstructions(),
                    recipe.get().getRecepiesIngredients());
            return dto;
        }
        return null;
    }

    private RecipeResponseDto mapRecipeToDto(Recipe recipe, List<Instruction> instructions,
            List<RecepiesIngredients> recepiesIngredients) {

        List<InstructionDto> instructionDtos = instructions == null
                ? Collections.emptyList()
                : instructions.stream().map(instructionService::mapInstructionToDto)
                        .collect(Collectors.toList());

        List<IngredientWithMeasurementDto> ingredientWithMeasurementDtos = recepiesIngredients == null
                ? Collections.emptyList()
                : recepiesIngredients.stream()
                        .map(recepiesIngredientsService::mapRecepiesIngredientsToIngredientWithMeasurementDto)
                        .collect(Collectors.toList());

        return new RecipeResponseDto(recipe.getRecipeId(), recipe.getTitle(), recipe.getDescription(),
                recipe.getServings(), instructionDtos, ingredientWithMeasurementDtos);
    }
}
