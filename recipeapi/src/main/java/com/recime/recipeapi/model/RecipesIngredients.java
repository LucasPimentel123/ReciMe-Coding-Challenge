package com.recime.recipeapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipes_ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipesIngredients {
    @EmbeddedId
    private RecipesIngredientsId id;

    @Column(name = "metric", nullable = true)
    private String metric;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @ManyToOne
    @MapsId("ingredient_id")
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @ManyToOne
    @MapsId("recipe_id")
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}