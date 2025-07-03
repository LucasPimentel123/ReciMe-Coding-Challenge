package com.recime.recipeapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recime.recipeapi.dto.instruction.InstructionDto;
import com.recime.recipeapi.dto.instruction.InstructionResponseDto;
import com.recime.recipeapi.mapper.InstructionMapper;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.model.Recipe;
import com.recime.recipeapi.repository.InstructionRepository;
import com.recime.recipeapi.repository.RecipeRepository;

@Service
public class InstructionService implements ServiceInterface<Instruction> {

    private final InstructionRepository repository;
    private final RecipeRepository recipeRepository;
    private final InstructionMapper instructionMapper;

    public InstructionService(InstructionRepository repository, RecipeRepository recipeRepository,
            InstructionMapper instructionMapper) {
        this.repository = repository;
        this.recipeRepository = recipeRepository;
        this.instructionMapper = instructionMapper;
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
            Instruction instruction = instructionMapper.toEntity(instructionDto, recipe.get());
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
        return this.getAll().stream().map(instructionMapper::toResponseDto).collect(Collectors.toList());
    }

    public Optional<InstructionResponseDto> getMappedToDtoById(Long id) {
        Optional<Instruction> instruction = this.getById(id);
        if (instruction.isPresent()) {
            return Optional.of(instructionMapper.toResponseDto(instruction.get()));
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

    @Transactional
    public Optional<Instruction> saveDto(InstructionDto instructionDto, Recipe recipe) {
        Instruction instruction = instructionMapper.toEntity(instructionDto, recipe);
        return this.save(instruction);
    }

}