package com.recime.recipeapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "isVegetarian", nullable = false)
    private Boolean isVegetarian;
    
}