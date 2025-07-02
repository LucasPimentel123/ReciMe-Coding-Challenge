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

import com.recime.recipeapi.dto.RecipesIngredients.RecipesIngredientsDto;
import com.recime.recipeapi.dto.RecipesIngredients.RecipesIngredientsIdDto;
import com.recime.recipeapi.model.RecipesIngredients;
import com.recime.recipeapi.model.RecipesIngredientsId;
import com.recime.recipeapi.service.RecipesIngredientsService;

@RestController
@RequestMapping("/recipe-ingredients")
public class RecipesIngredientsController {

    private final RecipesIngredientsService service;

    public RecipesIngredientsController(RecipesIngredientsService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<RecipesIngredientsDto>> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllMappedToDto());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping()
    public ResponseEntity<RecipesIngredients> save(@RequestBody RecipesIngredientsDto recepiesIngredientsDto) {
        Optional<RecipesIngredients> savedRecepiesIngredients = service.save(recepiesIngredientsDto);
        if (savedRecepiesIngredients.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping()
    public ResponseEntity<RecipesIngredients> update(@RequestBody RecipesIngredientsDto recepiesIngredientsDto) {
        Optional<RecipesIngredients> updatedRecepiesIngredients = service.update(recepiesIngredientsDto);
        if (updatedRecepiesIngredients.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping()
    public ResponseEntity<RecipesIngredients> delete(@RequestBody RecipesIngredientsIdDto recepiesIngredientsIdDto) {
        try {
            service.delete(recepiesIngredientsIdDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
