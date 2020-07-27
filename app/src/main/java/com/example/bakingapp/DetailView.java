package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class DetailView extends AppCompatActivity implements StepsAdapter.ListItemClickListener{
    private Food recipe;
    private String name;
    private String stringSteps;
    private String stringIngredients;
    private List<Step> steps_list;
    private List<Ingredient> ingredientList;
    private RecyclerView mIngredientsRecyclerView;
    private RecyclerView mStepsRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        mIngredientsRecyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
        mStepsRecyclerView = (RecyclerView) findViewById(R.id.recipe_steps_recyclerView);
        RecyclerView.LayoutManager stepLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager ingredientLayoutManager = new LinearLayoutManager(this);
        StepsAdapter stepsAdapter = new StepsAdapter(this, DetailView.this, this);
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("food");
        assert recipe != null;
        name = recipe.getName();
        setTitle(name);
        steps_list = recipe.getSteps();
        ingredientList = recipe.getIngredients();
        stepsAdapter.setSteps(steps_list);
        ingredientsAdapter.setIngredientList(ingredientList);
        stepsAdapter.setName(recipe.getName());
        mIngredientsRecyclerView.setLayoutManager(ingredientLayoutManager);
        mIngredientsRecyclerView.setAdapter(ingredientsAdapter);
        mStepsRecyclerView.setAdapter(stepsAdapter);
        mStepsRecyclerView.setLayoutManager(stepLayoutManager);
    }

    @Override
    public void onListItemClick(int item) {
    }
}