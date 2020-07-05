package com.spring.tacocloud.data;

import com.spring.tacocloud.Ingredient;

public interface IngredientRepository {

    Iterable<Ingredient> findAll();

    Ingredient findOneById(String id);

    Ingredient save(Ingredient ingredient);
}
