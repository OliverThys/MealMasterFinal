package com.mealmaster.mealmasterfinal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mealmaster.mealmasterfinal.dto.IngredientDTO;
import com.mealmaster.mealmasterfinal.model.CalendarSlot;
import com.mealmaster.mealmasterfinal.model.Ingredient;
import com.mealmaster.mealmasterfinal.model.Recipe;
import com.mealmaster.mealmasterfinal.repository.CalendarSlotRepository;
import com.mealmaster.mealmasterfinal.repository.RecipeRepository;
import java.util.Map;


@Service
public class ShoppingListService {

    @Autowired
    private CalendarSlotRepository calendarSlotRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public List<IngredientDTO> getAggregatedIngredients(String username) {
        List<CalendarSlot> slots = calendarSlotRepository.findByUsername(username);
        Map<String, IngredientDTO> aggregated = new HashMap<>();

        for (CalendarSlot slot : slots) {
            Recipe recipe = recipeRepository.findByName(slot.getRecipeName()).orElse(null);
            if (recipe != null) {
                recipe.getIngredientsList().size(); // 👈 force le chargement
            }

            if (recipe == null) continue;

            for (Ingredient ing : recipe.getIngredientsList() ) {
                String key = ing.getName().toLowerCase() + "_" + ing.getUnit();

                aggregated.compute(key, (k, existing) -> {
                    if (existing == null) {
                        return new IngredientDTO(ing.getName(), ing.getQuantity(), ing.getUnit());
                    } else {
                        existing.setQuantity(existing.getQuantity() + ing.getQuantity());
                        return existing;
                    }
                });
            }
        }

        return new ArrayList<>(aggregated.values());
    }
}

