package com.recime.recipeapi.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RecepiesIngredientsId implements Serializable {

    private Long ingredient_id;
    private Long recipe_id;
}