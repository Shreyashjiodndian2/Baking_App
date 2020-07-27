package com.example.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class recipiesAdapter extends RecyclerView.Adapter<recipiesAdapter.RecipeAdapterViewHolder>{
    private List<Food> foods;
    private ListItemClickListener listener;
    private Context textcontext;
    private Activity activity;

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int resId = R.layout.recipes;
        View view = layoutInflater.inflate(resId, parent, false);
        RecipeAdapterViewHolder recipeAdapterViewHolder = new RecipeAdapterViewHolder(view);
        return recipeAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, final int position) {
        holder.bind(position);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(textcontext, DetailView.class);
                intent.putExtra("food", (Parcelable) foods.get(position));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {if (foods != null) return foods.size();
        return 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(int item);
    }
    public recipiesAdapter(Context context, Activity activity, ListItemClickListener itemClickListener){
        this.textcontext = context;
        this.activity = activity;
        this.listener = itemClickListener;
    }
    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;
        RecipeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.recipe);
        }
        void bind(int listIndex) {
            if (foods.get(listIndex) != null) {
                mTextView.setText(foods.get(listIndex).getName());
                mTextView.getText();
            }
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onListItemClick(pos);
        }
    }
    void setFoods(List<Food> foods){
        this.foods = foods;
    }
}
