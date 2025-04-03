package com.mealmaster.mealmasterfinal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageUrl;
    private String prepTime;

    @Lob
    private String description;

    private String author; // futur lien avec utilisateur

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredientsList = new ArrayList<>();

    // Constructeur pratique
    public Recipe(String name, String imageUrl, String prepTime, String description, String author) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.prepTime = prepTime;
        this.description = description;
        this.author = author;
    }

    // Getter explicite pour compatibilité avec le service
    public List<Ingredient> getIngredients() {
        return ingredientsList;
    }
}
