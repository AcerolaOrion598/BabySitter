package com.djaphar.babysitter.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djaphar.babysitter.Activities.MainActivity;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.Adapters.BillTargetListRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.Adapters.BillingRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.Adapters.MainDialog;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Bill;
import com.djaphar.babysitter.SupportClasses.ApiClasses.BillPostModel;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitter.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitter.ViewModels.BillingViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BillingFragment extends MyFragment {

    private BillingViewModel billingViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView billingRecyclerView;
    private LinearLayout billThemeContainer, billPriceContainer, billTargetContainer, billStatusContainer;
    private ConstraintLayout billContainer, billingListContainer;
    private TextView billThemeContent, billPriceContent, billTargetContent, billStatusContent;
    private EditText billDescriptionEd;
    private Button billDeleteBtn, billSaveBtn;
    private ImageButton newBillBtn;
    private Bill currentBill;
    private ArrayList<Child> children;
    private AlertDialog billTargetDialog;
    private HashMap<String, String> authHeader = new HashMap<>();
    private boolean firstOpened = true;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        billingViewModel = new ViewModelProvider(this).get(BillingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_billing, container, false);
        billingRecyclerView = root.findViewById(R.id.billing_recycler_view);
        billThemeContainer = root.findViewById(R.id.bill_theme_container);
        billPriceContainer = root.findViewById(R.id.bill_price_container);
        billTargetContainer = root.findViewById(R.id.bill_target_container);
        billStatusContainer = root.findViewById(R.id.bill_status_container);
        billContainer = root.findViewById(R.id.bill_container);
        billingListContainer = root.findViewById(R.id.billing_list_container);
        billThemeContent = root.findViewById(R.id.bill_theme_content);
        billPriceContent = root.findViewById(R.id.bill_price_content);
        billTargetContent = root.findViewById(R.id.bill_target_content);
        billStatusContent = root.findViewById(R.id.bill_status_content);
        billDescriptionEd = root.findViewById(R.id.bill_description_ed);
        billDeleteBtn = root.findViewById(R.id.bill_delete_btn);
        billSaveBtn = root.findViewById(R.id.bill_save_btn);
        newBillBtn = root.findViewById(R.id.new_bill_btn);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_billing));
            setBackBtnState(View.GONE);
            mainActivity.setNewBtnState(View.GONE);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        billingViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            authHeader.put(getString(R.string.auth_header_key), user.getToken_type() + " " + user.getAccess_token());
            billingViewModel.requestMyBills(authHeader);
            billingViewModel.requestChildrenList(authHeader);
        });

        billingViewModel.getBills().observe(getViewLifecycleOwner(), bills -> {
            if (bills == null) {
                return;
            }
            BillingRecyclerViewAdapter adapter = new BillingRecyclerViewAdapter(bills, this);
            billingRecyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            billingRecyclerView.setLayoutManager(layoutManager);
            billingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (firstOpened) {
                        firstOpened = false;
                        return;
                    }
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                        ViewDriver.hideView(newBillBtn, R.anim.bottom_view_hide_animation, context);
                    } else {
                        ViewDriver.showView(newBillBtn, R.anim.bottom_view_show_animation, context);
                    }
                }
            });
        });

        billingViewModel.getChildren().observe(getViewLifecycleOwner(), children -> {
            if (children == null) {
                return;
            }
            this.children = children;
        });

        billingViewModel.getCurrentBill().observe(getViewLifecycleOwner(), currentBill -> {
            if (currentBill == null) {
                backWasPressed();
            }
        });

        billThemeContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.bill_theme_title_text),
                billThemeContent.getText().toString(), lView).show(getParentFragmentManager(), "dialog"));
        billPriceContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.bill_price_title_text),
                billPriceContent.getText().toString(), lView).show(getParentFragmentManager(), "dialog"));

        billTargetContainer.setOnClickListener(lView -> {
            View inflatedView = View.inflate(context, R.layout.recycler_kid_dialog, null);
            RecyclerView billTargetRecyclerView = inflatedView.findViewById(R.id.bill_target_recycler_view);
            billTargetRecyclerView.setAdapter(new BillTargetListRecyclerViewAdapter(children, this));
            billTargetRecyclerView.setNestedScrollingEnabled(false);
            billTargetRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            ConstraintLayout billTargetDefaultContainer = inflatedView.findViewById(R.id.bill_target_default_container);
            billTargetDefaultContainer.setOnClickListener(container -> setBillTarget(getDefaultChild()));
            billTargetDialog = new AlertDialog.Builder(mainActivity)
                    .setView(inflatedView)
                    .setTitle(R.string.parent_kid_title_text)
                    .create();
            billTargetDialog.show();
        });

        billStatusContainer.setOnClickListener(lView -> {
            View inflatedView = View.inflate(context, R.layout.switch_dialog, null);
            TextView billStatusTv = inflatedView.findViewById(R.id.dialog_status_tv);
            SwitchCompat billStatusSwitch = inflatedView.findViewById(R.id.dialog_status_switch);
            ViewDriver.setStatusTvOptions(billStatusTv, getResources(), currentBill.getStatus());
            billStatusSwitch.setChecked(currentBill.getStatus());
            billStatusSwitch.setOnCheckedChangeListener((compoundButton, isChecked) ->
                    ViewDriver.setStatusTvOptions(billStatusTv, getResources(), isChecked));
            new AlertDialog.Builder(mainActivity)
                    .setView(inflatedView)
                    .setTitle(R.string.bill_status_title_text)
                    .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                    .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                        currentBill.setStatus(billStatusSwitch.isChecked());
                        ViewDriver.setStatusTvOptions(billStatusContent, getResources(), billStatusSwitch.isChecked());
                    })
                    .create()
                    .show();
        });

        billDeleteBtn.setOnClickListener(lView -> new AlertDialog.Builder(context)
                .setTitle(R.string.delete_bill_title)
                .setMessage(R.string.delete_bill_message)
                .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> billingViewModel.requestDeleteBill(authHeader, currentBill.getBillId()))
                .show());

        billSaveBtn.setOnClickListener(lView -> {
            currentBill.setComment(billDescriptionEd.getText().toString());
            BillPostModel model = new BillPostModel(currentBill.getBillId(), currentBill.getChild().getChildId(),
                    currentBill.getTheme(), currentBill.getComment(), currentBill.getSum(), currentBill.getStatus());
            if (currentBill.getBillId() == null) {
                billingViewModel.requestCreateBill(authHeader, model);
                return;
            }
            billingViewModel.requestUpdateBill(authHeader, currentBill.getBillId(), model);
        });

        newBillBtn.setOnClickListener(lView -> openBillContainer(new Bill(null, "" ,"",
                getDefaultChild(), false, 0f)));
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(int visibilityState) {
        mainActivity.setBackBtnState(visibilityState);
    }

    public boolean everythingIsClosed() {
        return billContainer.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        billingViewModel.requestMyBills(authHeader);
        currentBill = null;
        setActionBarTitle(getString(R.string.title_billing));
        setBackBtnState(View.GONE);
        billingListContainer.setVisibility(View.VISIBLE);
        ViewDriver.toggleChildViewsEnable(billContainer, false);
        ViewDriver.hideView(billContainer, R.anim.hide_right_animation, context);
    }

    public void returnFieldValue(String fieldValue, View calledView) {
        switch (calledView.getId()) {
            case R.id.bill_theme_container:
                currentBill.setTheme(fieldValue);
                break;
            case R.id.bill_price_container:
                currentBill.setSum(Float.parseFloat(fieldValue));
                break;
        }
        setBillOptions(currentBill);
    }

    public void setBillTarget(Child child) {
        String fullName = child.getName() + " " + child.getSurname();
        if (child.getChildId() == null) {
            fullName = getString(R.string.billing_target_default_text);
        }
        currentBill.setChild(child);
        billTargetContent.setText(fullName);
        billTargetDialog.cancel();
    }

    public void openBillContainer(Bill bill) {
        currentBill = bill;
        setBillOptions(bill);
        String actionBarTitle = "Новый Счёт";
        setActionBarTitle(actionBarTitle);
        setBackBtnState(View.VISIBLE);
        ViewDriver.toggleChildViewsEnable(billContainer, true);
        Animation animation = ViewDriver.showView(billContainer, R.anim.show_right_animation, context);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                billingListContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private void setBillOptions(Bill bill) {
        billDeleteBtn.setVisibility(View.VISIBLE);
        String billId = bill.getBillId();
        if (billId == null) {
            billDeleteBtn.setVisibility(View.GONE);
        }

        String theme = bill.getTheme();
        if (theme == null) {
            theme = getString(R.string.some_field_is_null);
        }
        billThemeContent.setText(theme);

        float price = bill.getSum();
        if (price == (int) price) {
            billPriceContent.setText(String.valueOf((int) price));
        } else {
            billPriceContent.setText(String.format(Locale.US, "%.2f", bill.getSum()));
        }

        Child child = bill.getChild();
        String target = child.getName() + " " + child.getSurname();
        if (child.getChildId() == null) {
            target = getString(R.string.billing_target_default_text);
        }
        billTargetContent.setText(target);

        String comment = bill.getComment();
        if (comment == null) {
            comment = "";
        }
        billDescriptionEd.setText(comment);

        ViewDriver.setStatusTvOptions(billStatusContent, getResources(), bill.getStatus());
    }

    private Child getDefaultChild() {
        return new Child(null, null, null, null, null, null,
                null, null, null);
    }
}
