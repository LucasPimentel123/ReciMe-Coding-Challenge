package com.recime.recipeapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.recime.recipeapi.dto.instruction.InstructionDto;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.InstructionRepository;
import com.recime.recipeapi.repository.RecipeRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class InstructionServiceTest {

    @Autowired
    private InstructionService instructionService;

    @Autowired
    private InstructionRepository instructionRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    public void setUp() {
        instructionRepository.deleteAll();
        recipeRepository.deleteAll();
        Recipe savedRecipe = recipeRepository.save(new Recipe(null, "Recipe 1", "Description 1", 1));
        instructionRepository.saveAll(List.of(
                new Instruction(null, 1, "Description 1", savedRecipe),
                new Instruction(null, 2, "Description 2", savedRecipe)));
        instructionRepository.flush();
    }

    @Test
    public void save_ShouldSaveInstruction_WhenInstructionIsProvided() {
        Recipe savedRecipe = recipeRepository.findAll().get(0);

        Instruction instruction = new Instruction(null, 3, "Description 3", savedRecipe);
        Optional<Instruction> savedInstruction = instructionService.save(instruction);
        assertNotNull(savedInstruction.get().getInstructionId());
        assertEquals(instruction.getStep(), savedInstruction.get().getStep());
    }

    @Test
    @Transactional
    @Rollback
    public void save_ShouldReturnEmptyOptional_WhenExceptionOccurs() {
        Instruction instruction = new Instruction(null, 3, null, null);
        Optional<Instruction> savedInstruction = instructionService.save(instruction);
        assertEquals(Optional.empty(), savedInstruction);
    }

    @Test
    public void saveDto_ShouldSaveInstruction_WhenInstructionDtoIsProvided() {
        Recipe savedRecipe = recipeRepository.findAll().get(0);
        InstructionDto instructionDto = new InstructionDto(3, "Description 3", savedRecipe.getRecipeId());
        Optional<Instruction> savedInstruction = instructionService.saveDto(instructionDto, savedRecipe);
        assertNotNull(savedInstruction.get().getInstructionId());
        assertEquals(instructionDto.getStep(), savedInstruction.get().getStep());
    }

    @Test
    public void update_ShouldUpdateInstruction_WhenInstructionIsProvided() {
        Recipe savedRecipe = recipeRepository.findAll().get(0);
        List<Instruction> allInstructions = instructionRepository.findAll();
        assertFalse(allInstructions.isEmpty());

        Instruction instruction = allInstructions.get(0);

        InstructionDto instructionDto = new InstructionDto(instruction.getStep() + 1, "Description 4",
                savedRecipe.getRecipeId());

        Optional<Instruction> updatedInstruction = instructionService.update(instruction.getInstructionId(),
                instructionDto);
        assertNotNull(updatedInstruction.get().getInstructionId());
        assertEquals(instructionDto.getStep(), updatedInstruction.get().getStep());
    }

    @Test
    public void update_ShouldReturnEmptyOptional_WhenInstructionDoesNotExist() {
        InstructionDto instructionDto = new InstructionDto(1, "Description 4", 1L);
        Optional<Instruction> updatedInstruction = instructionService.update(999L, instructionDto);
        assertEquals(Optional.empty(), updatedInstruction);
    }
}
