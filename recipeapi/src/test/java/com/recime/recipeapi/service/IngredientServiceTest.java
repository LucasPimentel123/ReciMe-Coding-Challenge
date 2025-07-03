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
import org.springframework.test.annotation.DirtiesContext;

import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientWithMeasuresDto;
import com.recime.recipeapi.dto.ingredient.IngredientDto;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.repository.IngredientRepository;
import com.recime.recipeapi.repository.RecipeIngredientRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IngredientServiceTest {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        recipeIngredientRepository.deleteAll();
        ingredientRepository.deleteAll();
        ingredientRepository.saveAll(List.of(
                new Ingredient(null, "Test Ingredient Service 1", true),
                new Ingredient(null, "Test Ingredient Service 2", false)));
        ingredientRepository.flush();
    }

    @Test
    public void save_ShouldSaveIngredient_WhenIngredientIsProvided() {
        Ingredient ingredient = new Ingredient(null, "Test Ingredient Service 3", true);
        Optional<Ingredient> savedIngredient = ingredientService.save(ingredient);
        assertNotNull(savedIngredient.get().getIngredientId());
        assertEquals(ingredient.getName(), savedIngredient.get().getName());
    }

    @Test
    @Transactional
    @Rollback
    public void save_ShouldReturnEmptyOptional_WhenExceptionOccurs() {
        Ingredient ingredient = new Ingredient(null, null, true);
        Optional<Ingredient> savedIngredient = ingredientService.save(ingredient);
        assertEquals(Optional.empty(), savedIngredient);
    }

    @Test
    public void saveDto_ShouldSaveIngredient_WhenIngredientDtoIsProvided() {
        IngredientDto ingredientDto = new IngredientDto("Test Ingredient Service 4", true);
        Optional<Ingredient> savedIngredient = ingredientService.saveDto(ingredientDto);
        assertNotNull(savedIngredient.get().getIngredientId());
        assertEquals(ingredientDto.getName(), savedIngredient.get().getName());
    }

    @Test
    public void update_ShouldUpdateIngredient_WhenIngredientIsProvided() {
        List<Ingredient> allIngredients = ingredientRepository.findAll();
        assertFalse(allIngredients.isEmpty());

        Ingredient retrievedIngredient = allIngredients.get(0);

        IngredientDto ingredientDto = new IngredientDto(retrievedIngredient.getName() + " Updated", true);
        Optional<Ingredient> updatedIngredient = ingredientService.update(retrievedIngredient.getIngredientId(),
                ingredientDto);
        assertNotNull(updatedIngredient.get().getIngredientId());
        assertEquals(ingredientDto.getName(), updatedIngredient.get().getName());
    }

    @Test
    public void update_ShouldReturnEmptyOptional_WhenIngredientDoesNotExist() {
        IngredientDto ingredientDto = new IngredientDto("Test Ingredient 5", true);
        Optional<Ingredient> updatedIngredient = ingredientService.update(999L, ingredientDto);
        assertEquals(Optional.empty(), updatedIngredient);
    }

    @Test
    public void findOrSaveDto_ShouldFindIngredient_WhenIngredientExists() {
        RecipeIngredientWithMeasuresDto ingredientDto = new RecipeIngredientWithMeasuresDto("Test Ingredient 6", true,
                "g", 100.0);
        Optional<Ingredient> ingredientDb = ingredientService.findOrSaveDto(ingredientDto);
        assertNotNull(ingredientDb.get().getIngredientId());
        assertEquals(ingredientDto.getName(), ingredientDb.get().getName());
    }

    @Test
    public void findOrSaveDto_ShouldSaveIngredient_WhenIngredientDoesNotExist() {
        RecipeIngredientWithMeasuresDto ingredientDto = new RecipeIngredientWithMeasuresDto("Test Ingredient 7", true,
                "g", 100.0);
        Optional<Ingredient> savedIngredient = ingredientService.findOrSaveDto(ingredientDto);
        assertNotNull(savedIngredient.get().getIngredientId());
        assertEquals(ingredientDto.getName(), savedIngredient.get().getName());
    }
}
