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
import com.recime.recipeapi.model.Measurement;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.RecipeRepository;

@Service
public class RecipeService {

    private final RecipeRepository repository;
    private final InstructionService instructionService;
    private final MeasurementService measurementService;

    public RecipeService(RecipeRepository repository, InstructionService instructionService,
            MeasurementService measurementService) {
        this.repository = repository;
        this.instructionService = instructionService;
        this.measurementService = measurementService;
    }

    public List<RecipeResponseDto> getAllRecipes() {
        Optional<List<Recipe>> recipes = Optional.of(repository.findAll());

        if (recipes.isPresent()) {
            return recipes.get().stream().map(recipe -> {
                return mapRecipeToDto(recipe, recipe.getInstructions(), recipe.getMeasurements());
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public RecipeResponseDto getRecipeById(Long id) {
        Optional<Recipe> recipe = repository.findById(id);
        if (recipe.isPresent()) {
            RecipeResponseDto dto = mapRecipeToDto(recipe.get(), recipe.get().getInstructions(),
                    recipe.get().getMeasurements());
            return dto;
        }
        return null;
    }

    private RecipeResponseDto mapRecipeToDto(Recipe recipe, List<Instruction> instructions,
            List<Measurement> measurements) {

        List<InstructionDto> instructionDtos = instructions == null
                ? Collections.emptyList()
                : instructions.stream().map(instructionService::mapInstructionToDto)
                        .collect(Collectors.toList());

        List<IngredientWithMeasurementDto> ingredientWithMeasurementDtos = measurements == null
                ? Collections.emptyList()
                : measurements.stream().map(measurementService::mapMeasurementToIngredientWithMeasurementDto)
                        .collect(Collectors.toList());

        return new RecipeResponseDto(recipe.getRecipeId(), recipe.getTitle(), recipe.getDescription(),
                recipe.getServings(), instructionDtos, ingredientWithMeasurementDtos);
    }
}
