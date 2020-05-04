package com.djaphar.babysitter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djaphar.babysitter.Activities.MainActivity;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.Adapters.ChildrenRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.Adapters.MainDialog;
import com.djaphar.babysitter.SupportClasses.Adapters.ParentsRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Kid;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Parent;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitter.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitter.ViewModels.ChildrenViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChildrenFragment extends MyFragment {

    private ChildrenViewModel childrenViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView childrenRecyclerView, parentsRecyclerView;
    private ConstraintLayout kidInfoContainer, parentInfoContainer, childrenListContainer;
    private LinearLayout kidNameContainer, kidPatronymicContainer, kidSurnameContainer, kidAgeContainer, kidLockerContainer, kidBloodTypeContainer;
    private ScrollView kidInfoSv, parentInfoSv;
    private TextView kidNameContent, kidPatronymicContent, kidSurnameContent, kidAgeContent, kidLockerContent, kidBloodTypeContent, inviteCodeContent,
    parentNameContent, parentPatronymicContent, parentSurnameContent, parentRoleContent, parentKidContent, parentPhoneNumContent;
    private ImageButton newKidBtn;
    private ImageView kidPhoto, parentPhoto;
    private Kid currentKid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childrenViewModel = new ViewModelProvider(this).get(ChildrenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_children, container, false);
        childrenRecyclerView = root.findViewById(R.id.children_recycler_view);
        parentsRecyclerView = root.findViewById(R.id.parents_recycler_view);
        kidInfoContainer = root.findViewById(R.id.kid_info_container);
        parentInfoContainer = root.findViewById(R.id.parent_info_container);
        kidNameContainer = root.findViewById(R.id.kid_name_container);
        kidPatronymicContainer = root.findViewById(R.id.kid_patronymic_container);
        kidSurnameContainer = root.findViewById(R.id.kid_surname_container);
        kidAgeContainer = root.findViewById(R.id.kid_age_container);
        kidLockerContainer = root.findViewById(R.id.kid_locker_container);
        kidBloodTypeContainer = root.findViewById(R.id.kid_blood_type_container);
        kidInfoSv = root.findViewById(R.id.kid_info_sv);
        parentInfoSv = root.findViewById(R.id.parent_info_sv);
        childrenListContainer = root.findViewById(R.id.children_list_container);
        kidNameContent = root.findViewById(R.id.kid_name_content);
        kidPatronymicContent = root.findViewById(R.id.kid_patronymic_content);
        kidSurnameContent = root.findViewById(R.id.kid_surname_content);
        kidAgeContent = root.findViewById(R.id.kid_age_content);
        kidLockerContent = root.findViewById(R.id.kid_locker_content);
        kidBloodTypeContent = root.findViewById(R.id.kid_blood_type_content);
        inviteCodeContent = root.findViewById(R.id.invite_code_content);
        parentNameContent = root.findViewById(R.id.parent_name_content);
        parentPatronymicContent = root.findViewById(R.id.parent_patronymic_content);
        parentSurnameContent = root.findViewById(R.id.parent_surname_content);
        parentRoleContent = root.findViewById(R.id.parent_role_content);
        parentKidContent = root.findViewById(R.id.parent_kid_content);
        parentPhoneNumContent = root.findViewById(R.id.parent_phone_num_content);
        newKidBtn = root.findViewById(R.id.new_kid_btn);
        kidPhoto = root.findViewById(R.id.kid_photo);
        parentPhoto = root.findViewById(R.id.parent_photo);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_children));
            setBackBtnState(false);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        childrenViewModel.getKids().observe(getViewLifecycleOwner(), kids -> {
            if (kids == null) {
                return;
            }
            childrenRecyclerView.setAdapter(new ChildrenRecyclerViewAdapter(kids, this));
            childrenRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        kidNameContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.name_title_text), currentKid.getName(),
                kidNameContent).show(getParentFragmentManager(), "dialog"));
        kidPatronymicContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.patronymic_title_text), currentKid.getPatronymic(),
                kidPatronymicContent).show(getParentFragmentManager(), "dialog"));
        kidSurnameContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.surname_title_text), currentKid.getSurname(),
                kidSurnameContent).show(getParentFragmentManager(), "dialog"));
        kidAgeContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.kid_age_title_text), currentKid.getAge(),
                kidAgeContent).show(getParentFragmentManager(), "dialog"));
        kidLockerContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.kid_locker_title_text), currentKid.getLocker().toString(),
                kidLockerContent).show(getParentFragmentManager(), "dialog"));
        kidBloodTypeContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.kid_blood_type_title_text), currentKid.getBloodType(),
                kidBloodTypeContent).show(getParentFragmentManager(), "dialog"));
        newKidBtn.setOnClickListener(lView -> {
            View inflatedView = View.inflate(context, R.layout.new_kid_dialog, null);
            EditText kidNameEd = inflatedView.findViewById(R.id.kid_name_ed);
            EditText kidPatronymicEd = inflatedView.findViewById(R.id.kid_patronymic_ed);
            EditText kidSurnameEd = inflatedView.findViewById(R.id.kid_surname_ed);
            kidNameEd.setHint(R.string.name_title_text);
            kidPatronymicEd.setHint(R.string.patronymic_title_text);
            kidSurnameEd.setHint(R.string.surname_title_text);
            new AlertDialog.Builder(context)
                    .setView(inflatedView)
                    .setTitle(R.string.new_kid_title)
                    .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                    .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {

                    })
                    .show();
        });
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(boolean visible) {
        mainActivity.setBackBtnState(visible);
    }

    public boolean everythingIsClosed() {
        return parentInfoContainer.getVisibility() != View.VISIBLE && kidInfoContainer.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        String actionBarTitle = "";
        View viewToShow = null;
        View viewToHide = null;
        if (parentInfoContainer.getVisibility() == View.VISIBLE) {
            actionBarTitle = currentKid.getName() + " " + currentKid.getSurname();
            viewToShow = kidInfoContainer;
            viewToHide = parentInfoContainer;
        } else if (kidInfoContainer.getVisibility() == View.VISIBLE) {
            setBackBtnState(false);
            ViewDriver.toggleChildViewsEnable(kidInfoContainer, false);
            actionBarTitle = getString(R.string.title_children);
            viewToShow = childrenListContainer;
            viewToHide = kidInfoContainer;
        }

        setActionBarTitle(actionBarTitle);
        if (viewToShow == null) {
            return;
        }
        viewToShow.setVisibility(View.VISIBLE);

        if (viewToHide == null) {
            return;
        }
        ViewDriver.hideView(viewToHide, R.anim.hide_right_animation, context);
    }

    public void showKidInfo(Kid kid) {
        currentKid = kid;
        kidInfoSv.scrollTo(0, kidInfoSv.getTop());
        Glide.with(context).load(kid.getPhotoUrl()).into(kidPhoto);
        setActionBarTitle(kid.getName() + " " + kid.getSurname());
        kidNameContent.setText(kid.getName());
        kidPatronymicContent.setText(kid.getPatronymic());
        kidSurnameContent.setText(kid.getSurname());
        kidAgeContent.setText(kid.getAge());
        kidLockerContent.setText(String.valueOf(kid.getLocker()));
        kidBloodTypeContent.setText(kid.getBloodType());
        inviteCodeContent.setText(String.valueOf(kid.getInvite()));
        parentsRecyclerView.setAdapter(new ParentsRecyclerViewAdapter(kid, this));
        parentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        setBackBtnState(true);
        ViewDriver.toggleChildViewsEnable(kidInfoContainer, true);
        Animation animation = ViewDriver.showView(kidInfoContainer, R.anim.show_right_animation, context);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                childrenListContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    public void showParentInfo(Parent parent, Kid kid) {
        Glide.with(context).load(parent.getPhotoUrl()).into(parentPhoto);
        parentInfoSv.scrollTo(0, parentInfoSv.getTop());
        setActionBarTitle(parent.getName() + " " + parent.getSurname());
        parentNameContent.setText(parent.getName());
        parentPatronymicContent.setText(parent.getPatronymic());
        parentSurnameContent.setText(parent.getSurname());
        parentRoleContent.setText(parent.getRole());
        String kidFullName = kid.getName() + " " + kid.getPatronymic() + " " + kid.getSurname();
        parentKidContent.setText(kidFullName);
        parentPhoneNumContent.setText(parent.getPhoneNum());
        Animation animation = ViewDriver.showView(parentInfoContainer, R.anim.show_right_animation, context);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                kidInfoContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}
