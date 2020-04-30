package com.djaphar.babysitter.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.babysitter.Fragments.BillingFragment;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Bill;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Kid;
import com.djaphar.babysitter.SupportClasses.OtherClasses.ViewDriver;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class BillingRecyclerViewAdapter extends RecyclerView.Adapter<BillingRecyclerViewAdapter.ViewHolder> {

    private BillingFragment billingFragment;
    private ArrayList<Bill> bills;

    public BillingRecyclerViewAdapter(ArrayList<Bill> bills, BillingFragment billingFragment) {
        this.bills = bills;
        this.billingFragment = billingFragment;
        if (getItemCount() == 0) {
            bills.add(new Bill("У Вас пока нет ни одного счёта", null, null, null, null));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.billing_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.parentLayout.setOnClickListener(lView ->
                billingFragment.showBill(bills.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = bills.get(position);
        holder.billingListThemeTv.setText(bill.getTheme());
        if (bill.getStatus() == null) {
            holder.billingListThemeTv.setTextColor(billingFragment.getResources().getColor(R.color.colorBlack30));
            holder.billingListSwitchableContainer.setVisibility(View.GONE);
            return;
        }
        holder.billingListThemeTv.setTextColor(billingFragment.getResources().getColor(R.color.colorBlack87));
        holder.billingListSwitchableContainer.setVisibility(View.VISIBLE);
        float price = bill.getPrice();
        String priceStr;
        if (price == (int) price) {
            priceStr = (int) price + "р.";
        } else {
            priceStr = String.format(Locale.US, "%.2f", price) + "р.";
        }
        holder.billingListPriceTv.setText(priceStr);
        Kid kid = bill.getKid();
        String target = billingFragment.getString(R.string.billing_target_default_text);
        if (kid != null) {
            target = kid.getName() + " " + kid.getSurname();
        }
        holder.billingListTargetTv.setText(target);
        ViewDriver.setStatusTvOptions(holder.billingListStatusTv, billingFragment.getResources(), bill.getStatus());
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parentLayout;
        private ConstraintLayout billingListSwitchableContainer;
        private TextView billingListThemeTv, billingListTargetTv, billingListPriceTv, billingListStatusTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parent_layout_billing);
            billingListSwitchableContainer = itemView.findViewById(R.id.billing_list_switchable_container);
            billingListStatusTv = itemView.findViewById(R.id.billing_list_status_tv);
            billingListThemeTv = itemView.findViewById(R.id.billing_list_theme_tv);
            billingListTargetTv = itemView.findViewById(R.id.billing_list_target_tv);
            billingListPriceTv = itemView.findViewById(R.id.billing_list_price_tv);
        }
    }
}
