package com.mealmaster.mealmasterfinal.repository;

import com.mealmaster.mealmasterfinal.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
