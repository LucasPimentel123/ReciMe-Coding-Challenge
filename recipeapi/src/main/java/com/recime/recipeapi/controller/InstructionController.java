package com.recime.recipeapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recime.recipeapi.dto.instruction.InstructionDto;
import com.recime.recipeapi.dto.instruction.InstructionResponseDto;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.service.InstructionService;

@RestController
@RequestMapping("/instructions")
public class InstructionController {

    private final InstructionService service;

    public InstructionController(InstructionService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<InstructionResponseDto>> getAll() {
        List<InstructionResponseDto> instructionsDto = service.getAllMappedToDto();

        return ResponseEntity.status(HttpStatus.OK).body(instructionsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructionResponseDto> getById(@PathVariable Long id) {
        Optional<InstructionResponseDto> instructionDto = service.getMappedToDtoById(id);
        if (instructionDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(instructionDto.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping()
    public ResponseEntity<InstructionDto> save(@RequestBody InstructionDto instructionDto) {
        Optional<Instruction> savedInstruction = service.save(instructionDto);
        if (savedInstruction.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instruction> update(@PathVariable Long id,
            @RequestBody InstructionDto instructionUpdateDto) {
        Optional<Instruction> updatedInstruction = service.update(id, instructionUpdateDto);
        if (updatedInstruction.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Instruction> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
