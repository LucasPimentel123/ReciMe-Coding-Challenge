package com.recime.recipeapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.recipe.RecipeDto;
import com.recime.recipeapi.dto.recipe.RecipeResponseDto;
import com.recime.recipeapi.dto.recipe.RecipeUpdateDto;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.RecipeRepository;

@SpringBootTest
@ActiveProfiles("test")
public class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    public void setUp() {
        recipeRepository.deleteAll();
        recipeRepository.saveAll(List.of(
                new Recipe(null, "Cacio e Pepe", "Cacio e Pepe", 2, null, null),
                new Recipe(null, "Spaghetti Carbonara", "Spaghetti Carbonara", 4, null,
                        null)));

        recipeRepository.flush();
    }

    @Test
    public void save_ShouldSaveRecipe_WhenRecipeIsProvided() {
        Recipe recipe = new Recipe(null, "Test Recipe", "Test Description", 4, null,
                null);
        Optional<Recipe> savedRecipe = recipeService.save(recipe);

        assertNotNull(savedRecipe.get().getRecipeId());
        assertEquals(recipe.getTitle(), savedRecipe.get().getTitle());
    }

    @Test
    @Transactional
    @Rollback
    public void save_ShouldReturnEmptyOptional_WhenExceptionOccurs() {
        Recipe recipe = new Recipe(null, null, "Test Description", 4, null, null);
        Optional<Recipe> savedRecipe = recipeService.save(recipe);

        assertEquals(Optional.empty(), savedRecipe);
    }

    @Test
    public void getAll_ShouldReturnAllRecipes_WhenNoFiltersAreProvided() {
        Optional<List<Recipe>> recipes = recipeService.getAll(null);

        assertNotNull(recipes.get());
        assertEquals(2, recipes.get().size());
    }

    @Test
    public void getById_ShouldReturnRecipe_WhenRecipeExists() {
        List<Recipe> allRecipes = recipeRepository.findAll();
        assertFalse(allRecipes.isEmpty());

        Long recipeId = allRecipes.get(0).getRecipeId();
        Optional<Recipe> retrievedRecipe = recipeService.getById(recipeId);
        assertNotNull(retrievedRecipe.get());
        assertEquals("Cacio e Pepe", retrievedRecipe.get().getTitle());
    }

    @Test
    public void getById_ShouldReturnEmptyOptional_WhenRecipeDoesNotExist() {
        Optional<Recipe> recipe = recipeService.getById(999L);
        assertEquals(Optional.empty(), recipe);
    }

    @Test
    public void delete_ShouldDeleteRecipe_WhenRecipeExists() {
        List<Recipe> allRecipes = recipeRepository.findAll();
        assertFalse(allRecipes.isEmpty());

        Long recipeId = allRecipes.get(0).getRecipeId();
        int initialCount = allRecipes.size();

        recipeService.delete(recipeId);

        List<Recipe> remainingRecipes = recipeRepository.findAll();
        assertEquals(initialCount - 1, remainingRecipes.size());
    }

    @Test
    public void update_ShouldUpdateRecipe_WhenRecipeExists() {
        List<Recipe> allRecipes = recipeRepository.findAll();
        assertFalse(allRecipes.isEmpty());

        Long recipeId = allRecipes.get(0).getRecipeId();
        Optional<Recipe> retrievedRecipe = recipeService.getById(recipeId);
        assertNotNull(retrievedRecipe.get());

        RecipeUpdateDto recipeUpdateDto = new RecipeUpdateDto("Updated Recipe",
                "Updated Description", 4);
        Optional<Recipe> updatedRecipe = recipeService.update(recipeId,
                recipeUpdateDto);
        assertNotNull(updatedRecipe.get());
        assertEquals("Updated Recipe", updatedRecipe.get().getTitle());
        assertEquals("Updated Description", updatedRecipe.get().getDescription());
    }

    @Test
    public void getAllWithFilters_ShouldReturnFilteredRecipes_WhenFiltersAreProvided() {
        List<RecipeResponseDto> recipes = recipeService.getAllWithFilters(Optional.empty(), Optional.of(2),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertNotNull(recipes);
        assertEquals(1, recipes.size());
    }

    @Test
    public void getAllWithFilters_ShouldReturnAllRecipes_WhenFiltersAreNotProvided() {
        List<RecipeResponseDto> recipes = recipeService.getAllWithFilters(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertNotNull(recipes);
        assertEquals(2, recipes.size());
    }

    @Test
    public void getMappedResponseDtoById_ShouldReturnRecipeResponseDto_WhenRecipeExists() {
        List<Recipe> allRecipes = recipeRepository.findAll();
        assertFalse(allRecipes.isEmpty());

        Long recipeId = allRecipes.get(0).getRecipeId();
        Optional<RecipeResponseDto> retrievedRecipe = recipeService.getMappedResponseDtoById(recipeId);
        assertNotNull(retrievedRecipe.get());
        assertEquals("Cacio e Pepe", retrievedRecipe.get().getTitle());
    }
}
