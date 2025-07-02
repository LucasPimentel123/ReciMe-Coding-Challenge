package com.recime.recipeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructionDto {
    private Long instructionId;
    private Integer step;
    private String description;
}
