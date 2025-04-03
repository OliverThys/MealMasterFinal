package com.mealmaster.mealmasterfinal.controller;

import com.mealmaster.mealmasterfinal.model.Recipe;
import com.mealmaster.mealmasterfinal.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/main")
    public String showMain(Model model, @RequestParam(value = "query", required = false) String query) {
        List<Recipe> recipes = (query != null && !query.isEmpty())
                ? recipeRepository.findByNameContainingIgnoreCase(query)
                : recipeRepository.findAll();

        model.addAttribute("recipes", recipes);
        model.addAttribute("query", query);

        // Pour affichage (Mon, Tue, ...)
        model.addAttribute("days", List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));

        // Pour l'id technique (mon, tue, ...)
        model.addAttribute("dayKeys", List.of("mon", "tue", "wed", "thu", "fri", "sat", "sun"));

        return "main";
    }
}
