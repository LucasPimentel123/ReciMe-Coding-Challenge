package com.recime.recipeapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instructions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instruction { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instructionId;

    @Column(name = "step", nullable = false)
    private Integer step;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}