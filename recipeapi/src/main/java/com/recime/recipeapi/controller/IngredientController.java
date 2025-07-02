package com.recime.recipeapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

import com.recime.recipeapi.dto.ingredient.IngredientDto;
import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.service.IngredientService;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService service;

    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Ingredient> getAllIngredients() {
        return service.getAllIngredients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable Long id) {
        Optional<Ingredient> ingredient = service.getById(id);
        if (ingredient.isPresent()) {
            return ResponseEntity.ok(ingredient.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping()
    public ResponseEntity<Ingredient> save(@RequestBody IngredientDto ingredientDto) {
        Optional<Ingredient> savedIngredient = service.save(ingredientDto);
        if (savedIngredient.isPresent()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> update(@PathVariable Long id,
            @RequestBody IngredientDto ingredientDto) {
        Optional<Ingredient> updatedIngredient = service.update(id, ingredientDto);
        if (updatedIngredient.isPresent()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ingredient> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}