package com.djaphar.babysitter.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.babysitter.Fragments.BillingFragment;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillTargetListRecyclerViewAdapter extends RecyclerView.Adapter<BillTargetListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Child> children;
    private BillingFragment billingFragment;

    public BillTargetListRecyclerViewAdapter(ArrayList<Child> children, BillingFragment billingFragment) {
        this.children = children;
        this.billingFragment = billingFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.bill_target_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.parentLayout.setOnClickListener(lView -> billingFragment.setBillTarget(children.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Child child = children.get(position);
        String fullName = child.getName() + " " + child.getSurname();
        holder.billTargetListTv.setText(fullName);
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parentLayout;
        private TextView billTargetListTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_bill_target_list);
            billTargetListTv = itemView.findViewById(R.id.bill_target_list_tv);
        }
    }


}
