package com.mealmaster.mealmasterfinal.dto;

import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class IngredientDTO {
    private String name;
    private Double quantity;
    private String unit;
}

