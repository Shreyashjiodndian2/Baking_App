package com.example.bakingapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    public static List<Food> getFoods(String food_json_string){
        List<Food> foods = new ArrayList<Food>();
        JSONObject temp_json_object;
        try {
            JSONArray food_json_object = new JSONArray(food_json_string);
            for (int i = 0;i<food_json_object.length();i++){
                /*temp_json_object = food_json_object.getJSONObject(i);
                foods.add(new Food(temp_json_object.getString("name"),
                        temp_json_object.getString("ingredients"),
                        temp_json_object.getString("steps")));*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foods;
    }
    static List<Step> getSteps(String step_json_string){
        List<Step> steps = new ArrayList<Step>();
        JSONObject temp_json_object;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        try {
            JSONArray step_json_object = new JSONArray(step_json_string);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return steps;
    }
    static List<Ingredient> getIngredients(String ingredients_json_String){
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        JSONObject temp_json_object;
        JSONArray ingredient_json_array = null;
        try {
            ingredient_json_array = new JSONArray(ingredients_json_String);
            for (int i = 0;i<ingredient_json_array.length();i++){
                temp_json_object = ingredient_json_array.getJSONObject(i);
                /*ingredients.add(new Ingredient(Float.parseFloat(temp_json_object.getString("quantity")),
                        temp_json_object.getString("measure"),
                        temp_json_object.getString("ingredient")));*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredients;
    }
    static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("baking.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
