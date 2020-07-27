package com.example.bakingapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestbakingAPI {
    @GET(value = "baking.json")
    Call<List<Food>> getRecipes();
}
