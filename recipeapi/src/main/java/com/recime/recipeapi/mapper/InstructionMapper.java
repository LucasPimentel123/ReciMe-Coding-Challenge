package com.recime.recipeapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.recime.recipeapi.dto.instruction.InstructionDto;
import com.recime.recipeapi.dto.instruction.InstructionResponseDto;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.model.Recipe;

@Mapper(componentModel = "spring")
public interface InstructionMapper {

    @Mapping(target = "step", source = "instruction.step")
    @Mapping(target = "description", source = "instruction.description")
    @Mapping(target = "recipeId", source = "instruction.recipe.recipeId")
    InstructionDto toDto(Instruction instruction);

    @Mapping(target = "step", source = "instructionDto.step")
    @Mapping(target = "description", source = "instructionDto.description")
    @Mapping(target = "recipe", source = "recipe")
    @Mapping(target = "instructionId", ignore = true)
    Instruction toEntity(InstructionDto instructionDto, Recipe recipe);

    @Mapping(target = "instructionId", source = "instruction.instructionId")
    @Mapping(target = "step", source = "instruction.step")
    @Mapping(target = "description", source = "instruction.description")
    @Mapping(target = "recipeId", source = "instruction.recipe.recipeId")
    InstructionResponseDto toResponseDto(Instruction instruction);

    @Mapping(target = "step", source = "instructionResponseDto.step")
    @Mapping(target = "description", source = "instructionResponseDto.description")
    @Mapping(target = "recipe", source = "recipe")
    @Mapping(target = "instructionId", source = "instructionResponseDto.instructionId")
    Instruction toEntity(InstructionResponseDto instructionResponseDto, Recipe recipe);

}