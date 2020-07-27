package com.example.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {
    private List<Ingredient> ingredientList;
    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int resId = R.layout.ingredients;
        View view = layoutInflater.inflate(resId, parent, false);
        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() { if(ingredientList != null) {
        return ingredientList.size();
    }

        return 0;
    }
    class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mMeasureTextView;
        IngredientsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.ingredient_);
            mMeasureTextView = (TextView) itemView.findViewById(R.id.measure_Tv);
        }
        void bind(int listIndex) {
            if (ingredientList.get(listIndex) != null) {
                mNameTextView.setText(ingredientList.get(listIndex).getIngredient());
                mMeasureTextView.setText(String.format("%s%s", ingredientList.get(listIndex).getQuantity(), ingredientList.get(listIndex).getMeasure()));
            }
        }
    }
    void setIngredientList(List<Ingredient> ingredients){
        this.ingredientList = ingredients;
    }
}
