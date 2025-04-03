package com.mealmaster.mealmasterfinal.controller;

import com.mealmaster.mealmasterfinal.model.Recipe;
import com.mealmaster.mealmasterfinal.repository.RecipeRepository;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/recipes/add")
    public String showAddRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "add-recipe";
    }

    @PostMapping("/recipes/add")
    public String processAddRecipe(@ModelAttribute Recipe recipe) {
    if (recipe.getIngredientsList() != null) {
        recipe.getIngredientsList().forEach(ingredient -> ingredient.setRecipe(recipe));
    }
    recipeRepository.save(recipe);
    return "redirect:/main";
}


    @GetMapping("/recipes/search")
    public String searchRecipes(@RequestParam("query") String query, Model model) {
        List<Recipe> recipes = recipeRepository.findByNameContainingIgnoreCase(query);
        model.addAttribute("recipes", recipes);
        model.addAttribute("query", query); // pour réutiliser dans l’input
        return "main";
    }

}
