package com.mealmaster.mealmasterfinal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double quantity;
    private String unit; // "pieces", "grams", "ml", etc.

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
