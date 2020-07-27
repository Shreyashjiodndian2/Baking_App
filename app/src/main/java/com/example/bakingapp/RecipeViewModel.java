package com.example.bakingapp;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeViewModel extends AndroidViewModel {
    private MutableLiveData<List<Food>> foods = new MutableLiveData<List<Food>>();
    private boolean isRecipesAvailable = false;
    public RecipeViewModel(@NonNull Application application) {
        super(application);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetworkUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestbakingAPI requestbakingAPI = retrofit.create(RequestbakingAPI.class);
        Call<List<Food>> call = requestbakingAPI.getRecipes();
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (!response.isSuccessful()){
                    Log.v("Fetching food", "failed");
                    return;
                }
                foods.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.v("Fetching food", Objects.requireNonNull(t.getMessage()));

            }
        });
    }
    public LiveData<List<Food>> getFoods(){
        return foods;
    }

}
