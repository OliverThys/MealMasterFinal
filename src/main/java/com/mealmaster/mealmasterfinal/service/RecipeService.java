package com.mealmaster.mealmasterfinal.service;

import com.mealmaster.mealmasterfinal.model.Recipe;
import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    void saveRecipe(Recipe recipe);
    Recipe getRecipeById(Long id);
}
