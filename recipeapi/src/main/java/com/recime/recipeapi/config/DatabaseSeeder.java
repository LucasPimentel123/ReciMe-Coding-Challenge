package com.recime.recipeapi.config;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.recime.recipeapi.model.*;
import com.recime.recipeapi.repository.*;

@Component
public class DatabaseSeeder implements CommandLineRunner {

        private final IngredientRepository ingredientRepository;
        private final RecipeRepository recipeRepository;
        private final RecipeIngredientRepository recipeIngredientRepository;
        private final InstructionRepository instructionRepository;

        public DatabaseSeeder(IngredientRepository ingredientRepository, RecipeRepository recipeRepository,
                        RecipeIngredientRepository recipeIngredientRepository,
                        InstructionRepository instructionRepository) {
                this.ingredientRepository = ingredientRepository;
                this.recipeRepository = recipeRepository;
                this.recipeIngredientRepository = recipeIngredientRepository;
                this.instructionRepository = instructionRepository;
        }

        @Override
        public void run(String... args) {
                if (recipeRepository.count() == 0) {
                        seedCacioPepeRecipe();
                        seedCarbonaraRecipe();
                }
        }

        private void seedCacioPepeRecipe() {
                List<Ingredient> cacioPepeIngredients = createCacioPepeIngredients();
                ingredientRepository.saveAll(cacioPepeIngredients);

                Recipe cacioPepeRecipe = new Recipe(null, "Cacio e Pepe", "Italian pasta dish.", 2);
                recipeRepository.save(cacioPepeRecipe);

                List<RecipeIngredient> recipeIngredients = createCacioPepeRecipeIngredients(cacioPepeIngredients,
                                cacioPepeRecipe);
                recipeIngredientRepository.saveAll(recipeIngredients);

                List<Instruction> instructions = createCacioPepeInstructions(cacioPepeRecipe);
                instructionRepository.saveAll(instructions);
        }

        private List<Ingredient> createCacioPepeIngredients() {
                Ingredient pasta = new Ingredient(null, "Pasta", true);
                Ingredient pecorino = new Ingredient(null, "Pecorino", true);
                Ingredient blackPepper = new Ingredient(null, "Black Pepper", true);
                return List.of(pasta, pecorino, blackPepper);
        }

        private List<RecipeIngredient> createCacioPepeRecipeIngredients(List<Ingredient> ingredients, Recipe recipe) {
                return List.of(
                                new RecipeIngredient(
                                                new RecipeIngredientId(Long.valueOf(1), Long.valueOf(1)), "g", 100.0,
                                                ingredients.get(0),
                                                recipe),
                                new RecipeIngredient(
                                                new RecipeIngredientId(Long.valueOf(2), Long.valueOf(1)), "g", 120.0,
                                                ingredients.get(1),
                                                recipe),
                                new RecipeIngredient(
                                                new RecipeIngredientId(Long.valueOf(3), Long.valueOf(1)), "g", 8.0,
                                                ingredients.get(2),
                                                recipe));
        }

        private List<Instruction> createCacioPepeInstructions(Recipe recipe) {
                return List.of(
                                new Instruction(null, 1, "Boil the pasta.", recipe),
                                new Instruction(null, 2, "Heat the black pepper.", recipe),
                                new Instruction(null, 3,
                                                "Add the pecorino and black pepper to a bowl with pasta water.",
                                                recipe),
                                new Instruction(null, 4,
                                                "Mix the pecorino and black pepper with the pasta in a pan with low heat.",
                                                recipe));
        }

        private void seedCarbonaraRecipe() {
                List<Ingredient> existingIngredients = ingredientRepository
                                .findAllByNameIn(List.of("Pasta", "Pecorino", "Black Pepper")).stream()
                                .sorted(Comparator.comparing(Ingredient::getName))
                                .collect(Collectors.toList());

                List<Ingredient> newIngredients = createCarbonaraIngredients();
                ingredientRepository.saveAll(newIngredients);

                Recipe carbonaraRecipe = new Recipe(null, "Spaghetti alla Carbonara", "Italian pasta dish.", 2);
                recipeRepository.save(carbonaraRecipe);

                List<RecipeIngredient> recipeIngredients = createCarbonaraRecipeIngredients(
                                existingIngredients,
                                newIngredients,
                                carbonaraRecipe);
                recipeIngredientRepository.saveAll(recipeIngredients);

                List<Instruction> instructions = createCarbonaraInstructions(carbonaraRecipe);
                instructionRepository.saveAll(instructions);
        }

        private List<Ingredient> createCarbonaraIngredients() {
                return List.of(
                                new Ingredient(null, "Egg Yolk", true),
                                new Ingredient(null, "Whole Egg", true),
                                new Ingredient(null, "Guanciale", false));
        }

        private List<RecipeIngredient> createCarbonaraRecipeIngredients(
                        List<Ingredient> existingIngredients,
                        List<Ingredient> newIngredients,
                        Recipe recipe) {
                return List.of(
                                new RecipeIngredient(
                                                new RecipeIngredientId(Long.valueOf(1), Long.valueOf(2)), "g", 100.0,
                                                existingIngredients.get(1), recipe),
                                new RecipeIngredient(
                                                new RecipeIngredientId(Long.valueOf(2), Long.valueOf(2)), "g", 120.0,
                                                existingIngredients.get(2), recipe),
                                new RecipeIngredient(
                                                new RecipeIngredientId(Long.valueOf(3), Long.valueOf(2)), "g", 8.0,
                                                existingIngredients.get(0), recipe),
                                new RecipeIngredient(
                                                new RecipeIngredientId(Long.valueOf(4), Long.valueOf(2)), null, 4.0,
                                                newIngredients.get(0),
                                                recipe),
                                new RecipeIngredient(
                                                new RecipeIngredientId(Long.valueOf(5), Long.valueOf(2)), null, 1.0,
                                                newIngredients.get(1),
                                                recipe),
                                new RecipeIngredient(
                                                new RecipeIngredientId(Long.valueOf(6), Long.valueOf(2)), "g", 150.0,
                                                newIngredients.get(2),
                                                recipe));
        }

        private List<Instruction> createCarbonaraInstructions(Recipe recipe) {
                return List.of(
                                new Instruction(null, 1, "Boil the pasta.", recipe),
                                new Instruction(null, 2, "Fry the guanciale on a cold pan on low heat.", recipe),
                                new Instruction(null, 3,
                                                "Add the egg yolks, whole egg, pecorino and black pepper to a bowl and mix with pasta water.",
                                                recipe),
                                new Instruction(null, 4, "Add the mixture to a pan with the pasta and guanciale.",
                                                recipe),
                                new Instruction(null, 5, "Mix the pasta and guanciale with the mixture.", recipe));
        }
}
