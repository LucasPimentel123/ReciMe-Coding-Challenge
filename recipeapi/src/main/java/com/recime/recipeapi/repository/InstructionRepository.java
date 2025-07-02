package com.recime.recipeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recime.recipeapi.model.Instruction;

public interface InstructionRepository extends JpaRepository<Instruction, Long> {

}
