package com.recime.recipeapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "measurements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
    @EmbeddedId
    private MeasurementId id;

    @Column(name = "metric", nullable = false)
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