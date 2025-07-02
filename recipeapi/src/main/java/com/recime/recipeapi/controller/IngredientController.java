package com.recime.recipeapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

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

    @PostMapping()
    public ResponseEntity<Ingredient> save(@RequestBody Ingredient e) {
        Optional<Ingredient> savedIngredient = service.save(e);
        if (savedIngredient.isPresent()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}