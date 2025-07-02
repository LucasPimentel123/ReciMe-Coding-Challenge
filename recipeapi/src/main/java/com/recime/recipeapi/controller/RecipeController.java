package com.recime.recipeapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recime.recipeapi.dto.RecipeResponseDto;
import com.recime.recipeapi.service.RecipeService;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping()
    public List<RecipeResponseDto> getAllRecipes() {
        return service.getAllRecipes();
    }

    @GetMapping("/{id}")
    public RecipeResponseDto getRecipeById(@PathVariable Long id) {
        return service.getRecipeById(id);
    }
}
