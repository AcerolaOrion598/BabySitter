package com.djaphar.babysitter.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.babysitter.Fragments.EventFragment;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Food;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Ration;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealDialogRecyclerViewAdapter extends RecyclerView.Adapter<MealDialogRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Food> foods;
    private ArrayList<Ration> checkedFoods;
    private EventFragment eventFragment;
    private boolean denied;

    public MealDialogRecyclerViewAdapter(ArrayList<Food> foods, ArrayList<Ration> checkedFoods, EventFragment eventFragment, boolean denied) {
        this.foods = foods;
        this.checkedFoods = checkedFoods;
        this.eventFragment = eventFragment;
        this.denied = denied;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.meal_dialog_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mealFoodCb.setOnCheckedChangeListener((compoundButton, checked) ->
                eventFragment.setCheckedFood(foods.get(viewHolder.getAdapterPosition()), checked, denied));
        viewHolder.parentLayout.setOnClickListener(lView -> viewHolder.mealFoodCb.performClick());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foods.get(position);
        for (Ration checkedFood : checkedFoods) {
            if (food.getName().equals(checkedFood.getName())) {
                holder.mealFoodCb.setChecked(true);
            }
        }
        holder.mealFoodName.setText(food.getName());
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parentLayout;
        private TextView mealFoodName;
        private CheckBox mealFoodCb;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_meal_dialog);
            mealFoodCb = itemView.findViewById(R.id.meal_food_cb);
            mealFoodName = itemView.findViewById(R.id.meal_food_name);
        }
    }
}
