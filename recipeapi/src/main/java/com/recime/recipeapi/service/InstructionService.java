package com.recime.recipeapi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.recime.recipeapi.dto.InstructionDto;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.repository.InstructionRepository;

@Service
public class InstructionService {

    private final InstructionRepository repository;

    public InstructionService(InstructionRepository repository) {
        this.repository = repository;
    }

    public Optional<Instruction> createInstruction(Instruction e) {
        return Optional.of(repository.save(e));
    }

    public InstructionDto mapInstructionToDto(Instruction instruction) {
        InstructionDto dto = new InstructionDto();
        dto.setInstructionId(instruction.getInstructionId());
        dto.setStep(instruction.getStep());
        dto.setDescription(instruction.getDescription());
        return dto;
    }

}