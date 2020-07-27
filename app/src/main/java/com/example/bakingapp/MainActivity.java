package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.lang.UCharacter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements recipiesAdapter.ListItemClickListener{
    private recipiesAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Food> foods;
    private ProgressBar mProgressBar;
    private TextView mErrorMsg;
    private RecipeViewModel recipeViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recipes_recyclerView);
        mProgressBar = (ProgressBar) findViewById(R.id.recipe_Pb);
        mErrorMsg = (TextView) findViewById(R.id.error_Tv);
        mAdapter = new recipiesAdapter(this, MainActivity.this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        mAdapter.setFoods(recipeViewModel.getFoods().getValue());
        recipeViewModel.getFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods_parameter) {
                foods = foods_parameter;
                if (foods == null || foods.size() == 0){
                    mErrorMsg.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
                else {
                    mProgressBar.setVisibility(View.GONE);
                    mAdapter.setFoods(foods);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onListItemClick(int item) {

    }
}