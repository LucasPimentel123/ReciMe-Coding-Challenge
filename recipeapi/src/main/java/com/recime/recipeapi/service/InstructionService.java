package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.instruction.InstructionDto;
import com.recime.recipeapi.dto.instruction.InstructionResponseDto;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.InstructionRepository;
import com.recime.recipeapi.repository.RecipeRepository;

@Service
public class InstructionService implements ServiceInterface<Instruction> {

    private final InstructionRepository repository;
    private final RecipeRepository recipeRepository;

    public InstructionService(InstructionRepository repository, RecipeRepository recipeRepository) {
        this.repository = repository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public Optional<Instruction> save(Instruction e) {
        try {
            return Optional.of(repository.save(e));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<Instruction> save(List<Instruction> e) {
        try {
            return repository.saveAll(e);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public Optional<Instruction> save(InstructionDto instructionDto) {
        Optional<Recipe> recipe = recipeRepository.findById(instructionDto.getRecipeId());
        if (recipe.isPresent()) {
            Instruction instruction = new Instruction(null, instructionDto.getStep(), instructionDto.getDescription(),
                    recipe.get());
            return this.save(instruction);
        }
        return Optional.empty();
    }

    public List<Instruction> getAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public List<InstructionResponseDto> getAllMappedToDto() {
        return this.getAll().stream().map(this::mapInstructionToResponseDto).collect(Collectors.toList());
    }

    public Optional<InstructionResponseDto> getMappedToDtoById(Long id) {
        Optional<Instruction> instruction = this.getById(id);
        if (instruction.isPresent()) {
            return Optional.of(this.mapInstructionToResponseDto(instruction.get()));
        }
        return Optional.empty();
    }

    public Optional<Instruction> getById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Instruction> update(Long id, InstructionDto instructionUpdateDto) {
        Optional<Instruction> instruction = this.getById(id);
        if (instruction.isPresent()) {
            instruction.get().setStep(instructionUpdateDto.getStep());
            instruction.get().setDescription(instructionUpdateDto.getDescription());
            return this.save(instruction.get());
        }
        return Optional.empty();
    }

    @Transactional
    public void deleteByRecipeId(Long recipeId) {
        repository.deleteByRecipe_RecipeId(recipeId);
    }

    public InstructionDto mapInstructionToDto(Instruction instruction) {
        InstructionDto dto = new InstructionDto();
        dto.setStep(instruction.getStep());
        dto.setDescription(instruction.getDescription());
        dto.setRecipeId(instruction.getRecipe().getRecipeId());
        return dto;
    }

    public InstructionResponseDto mapInstructionToResponseDto(Instruction instruction) {
        InstructionResponseDto dto = new InstructionResponseDto();
        dto.setInstructionId(instruction.getInstructionId());
        dto.setStep(instruction.getStep());
        dto.setDescription(instruction.getDescription());
        dto.setRecipeId(instruction.getRecipe().getRecipeId());
        return dto;
    }

    public Instruction mapInstructionDtoToEntity(InstructionDto instructionDto, Recipe recipe) {
        return new Instruction(null, instructionDto.getStep(), instructionDto.getDescription(), recipe);
    }

    @Transactional
    public Optional<Instruction> mapDtoAndSave(InstructionDto instructionDto, Recipe recipe) {
        Instruction instruction = mapInstructionDtoToEntity(instructionDto, recipe);
        return this.save(instruction);
    }

}