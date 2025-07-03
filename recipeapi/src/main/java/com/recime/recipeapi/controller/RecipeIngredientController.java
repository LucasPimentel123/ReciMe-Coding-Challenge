package com.recime.recipeapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientDto;
import com.recime.recipeapi.dto.RecipeIngredient.RecipeIngredientIdDto;
import com.recime.recipeapi.model.RecipeIngredient;
import com.recime.recipeapi.service.RecipeIngredientService;

@RestController
@RequestMapping("/recipe-ingredients")
public class RecipeIngredientController {

    private final RecipeIngredientService service;

    public RecipeIngredientController(RecipeIngredientService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<RecipeIngredientDto>> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllMappedToDto());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping()
    public ResponseEntity<RecipeIngredient> save(@RequestBody RecipeIngredientDto recepiesIngredientsDto) {
        Optional<RecipeIngredient> savedRecepiesIngredients = service.save(recepiesIngredientsDto);
        if (savedRecepiesIngredients.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping()
    public ResponseEntity<RecipeIngredient> update(@RequestBody RecipeIngredientDto recepiesIngredientsDto) {
        Optional<RecipeIngredient> updatedRecepiesIngredients = service.update(recepiesIngredientsDto);
        if (updatedRecepiesIngredients.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedRecepiesIngredients.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping()
    public ResponseEntity<RecipeIngredient> delete(@RequestBody RecipeIngredientIdDto recepiesIngredientsIdDto) {
        try {
            service.delete(recepiesIngredientsIdDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
