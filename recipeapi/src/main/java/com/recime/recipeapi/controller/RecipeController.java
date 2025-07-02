package com.recime.recipeapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recime.recipeapi.dto.recipe.RecipeDto;
import com.recime.recipeapi.dto.recipe.RecipeResponseDto;
import com.recime.recipeapi.dto.recipe.RecipeUpdateDto;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.service.RecipeService;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<RecipeResponseDto>> getAll(
            @RequestParam() Optional<Boolean> isVegetarian,
            @RequestParam() Optional<Integer> servings,
            @RequestParam() Optional<String> instructionContent,
            @RequestParam() Optional<List<String>> includeIngredients,
            @RequestParam() Optional<List<String>> excludeIngredients) {

        try {
            List<RecipeResponseDto> recipes = service.getAllWithFilters(isVegetarian, servings, instructionContent,
                    includeIngredients, excludeIngredients);
            return ResponseEntity.ok(recipes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDto> getById(@PathVariable Long id) {
        Optional<RecipeResponseDto> recipe = service.getMappedResponseDtoById(id);
        if (recipe.isPresent()) {
            return ResponseEntity.ok(recipe.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping()
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDto recipeRequestDto) {
        Optional<Recipe> savedRecipe = service.mapDtoAndSave(recipeRequestDto);
        if (savedRecipe.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> update(@PathVariable Long id, @RequestBody RecipeUpdateDto recipeUpdateDto) {
        Optional<Recipe> updatedRecipe = service.update(id, recipeUpdateDto);
        if (updatedRecipe.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Recipe> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
