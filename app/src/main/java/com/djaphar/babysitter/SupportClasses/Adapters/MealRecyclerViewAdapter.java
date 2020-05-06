package com.djaphar.babysitter.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djaphar.babysitter.Fragments.SettingsFragment;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Food;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealRecyclerViewAdapter extends RecyclerView.Adapter<MealRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Food> foods;
    private SettingsFragment settingsFragment;

    public MealRecyclerViewAdapter(ArrayList<Food> foods, SettingsFragment settingsFragment) {
        this.foods = foods;
        this.settingsFragment = settingsFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.meal_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mealDeleteTv.setOnClickListener(lView -> settingsFragment.deleteMeal(foods.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mealTv.setText(foods.get(position).getName());
        if (position > 0) {
            holder.itemView.setBackground(settingsFragment.getResources().getDrawable(R.drawable.thin_top_border));
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mealTv, mealDeleteTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealTv = itemView.findViewById(R.id.meal_tv);
            mealDeleteTv = itemView.findViewById(R.id.meal_delete_tv);
        }
    }
}
