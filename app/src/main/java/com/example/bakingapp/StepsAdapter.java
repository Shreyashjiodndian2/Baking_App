package com.example.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    public static List<Step> steps;
    private StepsAdapter.ListItemClickListener listener;
    private Context stepContext;
    private Activity activity;
    private String name;

    public StepsAdapter(Context context, Activity activity, ListItemClickListener itemClickListener) {
        this.stepContext = context;
        this.activity = activity;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int resId = R.layout.step;
        View view = layoutInflater.inflate(resId, parent, false);
        StepsViewHolder stepViewHolder = new StepsViewHolder(view);
        return stepViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, final int position) {
        holder.bind(position);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(stepContext, Steps_Description.class);
                intent.putExtra("step", (Parcelable) steps.get(position));
                intent.putExtra("name", name);
                intent.putExtra("length", steps.size());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {if (steps != null) return steps.size();
        return 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface ListItemClickListener {
        void onListItemClick(int item);
    }
    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;
        private ImageView mImageView;
        StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.steps_Tv);
            mImageView = (ImageView) itemView.findViewById(R.id.steps_thumbnail);
        }
        void bind(int listIndex) {
            if (steps.get(listIndex) != null) {
                String shortDescription = steps.get(listIndex).getShortDescription();
                String thumbnail = steps.get(listIndex).getThumbnailURL();
                if (thumbnail != null || thumbnail.length() !=0) {
                    Glide.with(stepContext).load(thumbnail).placeholder(R.drawable.placeholder_2).into(mImageView);
                }
                mImageView.setImageResource(R.drawable.placeholder_2);
                mTextView.setText(shortDescription);
            }
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onListItemClick(pos);
        }
    }
    void setSteps(List<Step> stepsList){
        this.steps = stepsList;
    }
}
