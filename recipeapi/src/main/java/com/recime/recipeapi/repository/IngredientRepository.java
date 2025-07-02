package com.recime.recipeapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recime.recipeapi.model.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAllByNameIn(List<String> names);

    Optional<Ingredient> findByName(String name);
}