package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.ingredient.IngredientWithMeasurementDto;
import com.recime.recipeapi.dto.instruction.InstructionDto;
import com.recime.recipeapi.dto.recipe.RecipeDto;
import com.recime.recipeapi.dto.recipe.RecipeResponseDto;
import com.recime.recipeapi.dto.recipe.RecipeUpdateDto;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.model.RecipesIngredients;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.RecipeRepository;
import com.recime.recipeapi.specification.RecipeSpecification;

@Service
public class RecipeService implements ServiceInterface<Recipe> {

    private final RecipeRepository repository;
    private final InstructionService instructionService;
    private final RecipesIngredientsService recipiesIngredientsService;
    private final IngredientService ingredientService;

    public RecipeService(RecipeRepository repository, InstructionService instructionService,
            RecipesIngredientsService recipiesIngredientsService, IngredientService ingredientService) {
        this.repository = repository;
        this.instructionService = instructionService;
        this.recipiesIngredientsService = recipiesIngredientsService;
        this.ingredientService = ingredientService;
    }

    @Transactional
    public Optional<Recipe> save(Recipe recipe) {
        try {
            return Optional.of(repository.save(recipe));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<Recipe> save(List<Recipe> recipes) {
        try {
            return repository.saveAll(recipes);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public List<RecipeResponseDto> getAllWithFilters(Optional<Boolean> isVegetarian, Optional<Integer> servings,
            Optional<String> instructionContent, Optional<List<String>> includeIngredients,
            Optional<List<String>> excludeIngredients) {

        Specification<Recipe> specification = RecipeSpecification.withFilters(isVegetarian, servings,
                instructionContent,
                includeIngredients, excludeIngredients);

        Optional<List<Recipe>> recipes = Optional.of(repository.findAll(specification));

        if (recipes.isPresent()) {
            return recipes.get().stream().map(recipe -> {
                return mapRecipeToResponseDto(recipe, recipe.getInstructions(), recipe.getRecipiesIngredients());
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public Optional<Recipe> getById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Optional<RecipeResponseDto> getMappedResponseDtoById(Long id) {
        Optional<Recipe> recipe = this.getById(id);
        if (recipe.isPresent()) {
            RecipeResponseDto dto = mapRecipeToResponseDto(recipe.get(), recipe.get().getInstructions(),
                    recipe.get().getRecipiesIngredients());
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Recipe> mapDtoAndSave(RecipeDto recipeRequestDto) {
        Recipe recipeToSave = new Recipe(null, recipeRequestDto.getTitle(), recipeRequestDto.getDescription(),
                recipeRequestDto.getServings());

        Optional<Recipe> savedRecipe = this.save(recipeToSave);

        if (savedRecipe.isPresent()) {
            this.saveRecipeInstructions(recipeRequestDto.getInstructions(), savedRecipe.get());
            this.saveRecipeIngredients(recipeRequestDto.getIngredients(), savedRecipe.get());
        } else {
            return Optional.empty();
        }

        return savedRecipe;
    }

    @Transactional
    private void saveRecipeInstructions(List<InstructionDto> instructions, Recipe recipe) {
        instructions.forEach(instructionDto -> instructionService.mapInstructionDtoToEntity(instructionDto,
                recipe));
    }

    @Transactional
    private void saveRecipeIngredients(List<IngredientWithMeasurementDto> ingredients, Recipe recipe) {
        ingredients.forEach(ingredientDto -> ingredientService.saveIngredientsAndRecipesMeasurements(ingredientDto,
                recipe));
    }

    public Optional<Recipe> update(Long id, RecipeUpdateDto recipeUpdateDto) {
        Optional<Recipe> recipe = this.getById(id);
        if (recipe.isPresent()) {
            recipe.get().setTitle(recipeUpdateDto.getTitle());
            recipe.get().setDescription(recipeUpdateDto.getDescription());
            recipe.get().setServings(recipeUpdateDto.getServings());
            return this.save(recipe.get());
        }
        return Optional.empty();
    }

    @Transactional
    public void delete(Long id) {
        instructionService.deleteByRecipeId(id);
        recipiesIngredientsService.deleteByRecipeId(id);
        repository.deleteById(id);
    }

    private RecipeResponseDto mapRecipeToResponseDto(Recipe recipe, List<Instruction> instructions,
            List<RecipesIngredients> recipiesIngredients) {

        List<InstructionDto> instructionDtos = instructions == null
                ? Collections.emptyList()
                : instructions.stream()
                        .map(instructionService::mapInstructionToDto)
                        .sorted(Comparator.comparing(InstructionDto::getStep))
                        .collect(Collectors.toList());

        List<IngredientWithMeasurementDto> ingredientWithMeasurementDtos = recipiesIngredients == null
                ? Collections.emptyList()
                : recipiesIngredients.stream()
                        .map(recipiesIngredientsService::mapRecepiesIngredientsToIngredientWithMeasurementDto)
                        .collect(Collectors.toList());

        return new RecipeResponseDto(recipe.getRecipeId(), recipe.getTitle(), recipe.getDescription(),
                recipe.getServings(), instructionDtos, ingredientWithMeasurementDtos);
    }
}
