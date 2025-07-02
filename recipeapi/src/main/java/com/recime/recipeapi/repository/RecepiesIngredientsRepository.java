package com.recime.recipeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recime.recipeapi.model.RecepiesIngredients;
import com.recime.recipeapi.model.RecepiesIngredientsId;

public interface RecepiesIngredientsRepository extends JpaRepository<RecepiesIngredients, RecepiesIngredientsId> {

}
