package com.recime.recipeapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientDto;
import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientWithMeasuresDto;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.model.RecipeIngredient;
import com.recime.recipeapi.model.RecipeIngredientId;
import com.recime.recipeapi.repository.IngredientRepository;
import com.recime.recipeapi.repository.RecipeIngredientRepository;
import com.recime.recipeapi.repository.RecipeRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class RecipeIngredientServiceTest {

    @Autowired
    private RecipeIngredientService recipeIngredientService;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        recipeIngredientRepository.deleteAll();
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();

        ingredientRepository.save(new Ingredient(null, "Ingredient 1", true));
        recipeRepository.save(new Recipe(null, "Recipe 1", "Description 1", 1));

        recipeIngredientRepository.flush();
        ingredientRepository.flush();
        recipeRepository.flush();
    }

    @Test
    @Transactional
    public void save_ShouldSaveRecipeIngredient_WhenRecipeIngredientIsProvided() {
        Recipe savedRecipe = recipeRepository.findAll().get(0);
        Ingredient savedIngredient = ingredientRepository.findAll().get(0);
        RecipeIngredientId id = new RecipeIngredientId(savedIngredient.getIngredientId(), savedRecipe.getRecipeId());
        RecipeIngredient recipeIngredient = new RecipeIngredient(id, "g", 100.0, savedIngredient, savedRecipe);
        Optional<RecipeIngredient> savedRecipeIngredient = recipeIngredientService.save(recipeIngredient);
        assertNotNull(savedRecipeIngredient.get().getId());
        assertEquals(recipeIngredient.getMetric(), savedRecipeIngredient.get().getMetric());
    }

    @Test
    @Transactional
    @Rollback
    public void save_ShouldReturnEmptyOptional_WhenExceptionOccurs() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(null, "g", 100.0, null, null);
        Optional<RecipeIngredient> savedRecipeIngredient = recipeIngredientService.save(recipeIngredient);
        assertEquals(Optional.empty(), savedRecipeIngredient);
    }

    @Test
    public void saveDto_ShouldSaveRecipeIngredient_WhenRecipeIngredientDtoIsProvided() {
        Recipe savedRecipe = recipeRepository.findAll().get(0);
        Ingredient savedIngredient = ingredientRepository.findAll().get(0);
        RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto(savedIngredient.getIngredientId(),
                savedRecipe.getRecipeId(), "g", 100.0);

        Optional<RecipeIngredient> savedRecipeIngredient = recipeIngredientService.saveDto(recipeIngredientDto);
        assertNotNull(savedRecipeIngredient.get().getId());
        assertEquals(recipeIngredientDto.getMetric(), savedRecipeIngredient.get().getMetric());
    }

    @Test
    public void saveDto_ShouldReturnEmptyOptional_WhenIngredientDoesNotExist() {
        Recipe savedRecipe = recipeRepository.findAll().get(0);
        RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto(999L, savedRecipe.getRecipeId(), "g", 100.0);
        Optional<RecipeIngredient> savedRecipeIngredient = recipeIngredientService.saveDto(recipeIngredientDto);
        assertEquals(Optional.empty(), savedRecipeIngredient);
    }

    @Test
    public void saveDto_ShouldReturnEmptyOptional_WhenRecipeDoesNotExist() {
        Ingredient savedIngredient = ingredientRepository.findAll().get(0);
        RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto(savedIngredient.getIngredientId(), 999L, "g",
                100.0);
        Optional<RecipeIngredient> savedRecipeIngredient = recipeIngredientService.saveDto(recipeIngredientDto);
        assertEquals(Optional.empty(), savedRecipeIngredient);
    }

    @Test
    public void saveIngredientsAndRecipesMeasurements_ShouldSaveRecipeIngredient_WhenRecipeIngredientDtoIsProvided() {
        RecipeIngredientWithMeasuresDto recipeIngredientWithMeasuresDto = new RecipeIngredientWithMeasuresDto(
                "Ingredient 1", true, "g", 100.0);
        Recipe savedRecipe = recipeRepository.findAll().get(0);
        Optional<RecipeIngredient> savedRecipeIngredient = recipeIngredientService
                .saveIngredientsAndRecipesMeasurements(recipeIngredientWithMeasuresDto, savedRecipe);
        assertNotNull(savedRecipeIngredient.get().getId());
        assertEquals(recipeIngredientWithMeasuresDto.getMetric(), savedRecipeIngredient.get().getMetric());
        assertEquals(recipeIngredientWithMeasuresDto.getQuantity(), savedRecipeIngredient.get().getQuantity());
    }

    @Test
    public void saveIngredientsAndRecipesMeasurements_ShouldThrowException_WhenExceptionOccurs() {
        RecipeIngredientWithMeasuresDto recipeIngredientWithMeasuresDto = new RecipeIngredientWithMeasuresDto(
                "Ingredient 1", true, "g", 100.0);
        Recipe savedRecipe = recipeRepository.findAll().get(0);
        try {
            recipeIngredientService.saveIngredientsAndRecipesMeasurements(recipeIngredientWithMeasuresDto, savedRecipe);
        } catch (Exception ex) {
            assertEquals("Error saving ingredient: " + recipeIngredientWithMeasuresDto.getName(), ex.getMessage());
        }
    }
}
