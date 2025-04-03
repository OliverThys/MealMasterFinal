package com.mealmaster.mealmasterfinal.repository;

import com.mealmaster.mealmasterfinal.model.Recipe;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByNameContainingIgnoreCase(String name);

    Optional<Recipe> findByName(String name);


    // possibilité d’ajouter des findByNameContains, etc plus tard
}
