package com.recime.recipeapi.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.recime.recipeapi.model.Ingredient;
import com.recime.recipeapi.model.Instruction;
import com.recime.recipeapi.model.RecipeIngredient;
import com.recime.recipeapi.model.Recipe;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class RecipeSpecification {
    public static Specification<Recipe> withFilters(Optional<Boolean> isVegetarian, Optional<Integer> servings,
            Optional<String> instructionContent, Optional<List<String>> includeIngredients,
            Optional<List<String>> excludeIngredients) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);

            if (servings.isPresent()) {
                predicates.add(cb.equal(root.get("servings"), servings.get()));
            }

            if (instructionContent.isPresent()) {
                predicates.add(getInstructionContentPredicate(cb, instructionContent.get(), query, root));
            }

            if (isVegetarian.isPresent()) {
                predicates.add(getIsVegetarianPredicate(cb, isVegetarian.get(), query, root));
            }

            if (includeIngredients.isPresent() && !includeIngredients.get().isEmpty()) {
                predicates.add(getIngredientFilterPredicate(cb, includeIngredients.get(), query, root, true));
            }

            if (excludeIngredients.isPresent() && !excludeIngredients.get().isEmpty()) {
                predicates.add(getIngredientFilterPredicate(cb, excludeIngredients.get(), query, root, false));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate getInstructionContentPredicate(CriteriaBuilder cb, String instructionContent,
            CriteriaQuery<?> query, Root<Recipe> root) {
        Join<Recipe, Instruction> instructionsJoin = root.join("instructions");
        return cb.like(cb.lower(instructionsJoin.get("description")),
                "%" + instructionContent.toLowerCase() + "%");
    }

    private static Predicate getIsVegetarianPredicate(CriteriaBuilder cb, boolean isVegetarian,
            CriteriaQuery<?> query, Root<Recipe> root) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Recipe> subRoot = subquery.from(Recipe.class);
        Join<Recipe, RecipeIngredient> subRecipeIngredientJoin = subRoot.join("recipeIngredient");
        Join<RecipeIngredient, Ingredient> subIngredientsJoin = subRecipeIngredientJoin.join("ingredient");

        subquery.select(subRoot.get("recipeId"))
                .where(cb.equal(subIngredientsJoin.get("isVegetarian"), false));

        return isVegetarian ? cb.not(root.get("recipeId").in(subquery)) : root.get("recipeId").in(subquery);
    }

    private static Predicate getIngredientFilterPredicate(CriteriaBuilder cb, List<String> ingredients,
            CriteriaQuery<?> query, Root<Recipe> root, Boolean isInclude) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Recipe> subRoot = subquery.from(Recipe.class);
        Join<Recipe, RecipeIngredient> subRecipeIngredientJoin = subRoot.join("recipeIngredient");
        Join<RecipeIngredient, Ingredient> subIngredientsJoin = subRecipeIngredientJoin.join("ingredient");

        subquery.select(subRoot.get("recipeId"))
                .where(cb.lower(subIngredientsJoin.get("name")).in(ingredients.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList())))
                .groupBy(subRoot.get("recipeId"))
                .having(cb.equal(cb.count(subRoot.get("recipeId")),
                        cb.literal(ingredients.size())));

        Predicate includePredicate = root.get("recipeId").in(subquery);

        return isInclude ? includePredicate : cb.not(includePredicate);
    }

}
