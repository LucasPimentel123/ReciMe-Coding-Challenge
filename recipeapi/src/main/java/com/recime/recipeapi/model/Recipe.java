package com.recime.recipeapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    public Recipe(Long recipeId, String title, String description, Integer servings) {
        this.recipeId = recipeId;
        this.title = title;
        this.description = description;
        this.servings = servings;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "servings", nullable = false)
    private Integer servings;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Instruction> instructions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipesIngredients> recipiesIngredients;

}