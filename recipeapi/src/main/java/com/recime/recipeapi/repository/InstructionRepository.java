package com.recime.recipeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recime.recipeapi.model.Instruction;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long> {
    void deleteByRecipe_RecipeId(Long recipeId);
}
