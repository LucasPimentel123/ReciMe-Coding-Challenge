package com.recime.recipeapi.dto.instruction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructionResponseDto {
    private Long instructionId;
    private Integer step;
    private String description;
    private Long recipeId;
}
