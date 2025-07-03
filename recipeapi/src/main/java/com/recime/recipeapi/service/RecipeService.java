package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.RecipesIngredients.RecipesIngredientsWithMeasuresDto;
import com.recime.recipeapi.dto.instruction.InstructionDto;
import com.recime.recipeapi.dto.recipe.RecipeDto;
import com.recime.recipeapi.dto.recipe.RecipeResponseDto;
import com.recime.recipeapi.dto.recipe.RecipeUpdateDto;
import com.recime.recipeapi.mapper.RecipeMapper;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.RecipeRepository;
import com.recime.recipeapi.specification.RecipeSpecification;

@Service
public class RecipeService implements ServiceInterface<Recipe> {

    private final RecipeRepository repository;
    private final InstructionService instructionService;
    private final RecipesIngredientsService recipesIngredientsService;
    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeRepository repository, InstructionService instructionService,
            RecipesIngredientsService recipesIngredientsService, RecipeMapper recipeMapper) {
        this.repository = repository;
        this.instructionService = instructionService;
        this.recipesIngredientsService = recipesIngredientsService;
        this.recipeMapper = recipeMapper;
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

    public Optional<List<Recipe>> getAll(Specification<Recipe> specification) {
        try {
            return Optional.of(repository.findAll(specification));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Optional<Recipe> getById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Transactional
    public void delete(Long id) {
        instructionService.deleteByRecipeId(id);
        recipesIngredientsService.deleteByRecipeId(id);
        repository.deleteById(id);
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
    public List<RecipeResponseDto> getAllWithFilters(Optional<Boolean> isVegetarian, Optional<Integer> servings,
            Optional<String> instructionContent, Optional<List<String>> includeIngredients,
            Optional<List<String>> excludeIngredients) {

        Specification<Recipe> specification = RecipeSpecification.withFilters(isVegetarian, servings,
                instructionContent,
                includeIngredients, excludeIngredients);

        Optional<List<Recipe>> recipes = this.getAll(specification);

        if (recipes.isPresent()) {
            return recipes.get().stream()
                    .map(recipe -> recipeMapper.toResponseDto(recipe, recipe.getInstructions(),
                            recipe.getRecipiesIngredients()))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Transactional
    public Optional<RecipeResponseDto> getMappedResponseDtoById(Long id) {
        Optional<Recipe> recipe = this.getById(id);
        if (recipe.isPresent()) {
            RecipeResponseDto dto = recipeMapper.toResponseDto(recipe.get(),
                    recipe.get().getInstructions(), recipe.get().getRecipiesIngredients());
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Recipe> saveDto(RecipeDto recipeRequestDto) {
        Recipe recipeToSave = recipeMapper.toEntity(recipeRequestDto);

        Optional<Recipe> savedRecipe = this.save(recipeToSave);

        if (savedRecipe.isPresent()) {

            this.saveRecipeInstructions(recipeRequestDto.getInstructions(), savedRecipe.get());
            this.saveRecipeIngredients(recipeRequestDto.getIngredients(), savedRecipe.get());

        } else {
            return Optional.empty();
        }

        return savedRecipe;
    }

    private void saveRecipeInstructions(List<InstructionDto> instructions, Recipe recipe) {
        instructions.forEach(instructionDto -> instructionService.saveDto(instructionDto, recipe));
    }

    @Transactional
    private void saveRecipeIngredients(List<RecipesIngredientsWithMeasuresDto> ingredients, Recipe recipe) {
        ingredients
                .forEach(ingredientDto -> recipesIngredientsService.saveIngredientsAndRecipesMeasurements(ingredientDto,
                        recipe));
    }
}
