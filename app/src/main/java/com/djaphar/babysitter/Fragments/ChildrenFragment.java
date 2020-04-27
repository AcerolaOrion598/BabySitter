package com.djaphar.babysitter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChildrenFragment extends MyFragment {

    private ChildrenViewModel childrenViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView childrenRecyclerView, parentsRecyclerView;
    private RelativeLayout childrenListLayout;
    private ConstraintLayout kidInfoContainer, parentInfoContainer;
    private LinearLayout kidNameContainer, kidPatronymicContainer, kidSurnameContainer, kidAgeContainer, kidLockerContainer, kidBloodTypeContainer;
    private ScrollView kidInfoSv, parentInfoSv;
    private TextView kidNameContent, kidPatronymicContent, kidSurnameContent, kidAgeContent, kidLockerContent, kidBloodTypeContent,
    parentNameContent, parentPatronymicContent, parentSurnameContent, parentRoleContent, parentKidContent, parentPhoneNumContent;
    private ImageView kidPhoto, parentPhoto;
    private Kid currentKid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childrenViewModel = new ViewModelProvider(this).get(ChildrenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_children, container, false);
        childrenRecyclerView = root.findViewById(R.id.children_recycler_view);
        parentsRecyclerView = root.findViewById(R.id.parents_recycler_view);
        childrenListLayout = root.findViewById(R.id.children_list_layout);
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
        kidNameContent = root.findViewById(R.id.kid_name_content);
        kidPatronymicContent = root.findViewById(R.id.kid_patronymic_content);
        kidSurnameContent = root.findViewById(R.id.kid_surname_content);
        kidAgeContent = root.findViewById(R.id.kid_age_content);
        kidLockerContent = root.findViewById(R.id.kid_locker_content);
        kidBloodTypeContent = root.findViewById(R.id.kid_blood_type_content);
        parentNameContent = root.findViewById(R.id.parent_name_content);
        parentPatronymicContent = root.findViewById(R.id.parent_patronymic_content);
        parentSurnameContent = root.findViewById(R.id.parent_surname_content);
        parentRoleContent = root.findViewById(R.id.parent_role_content);
        parentKidContent = root.findViewById(R.id.parent_kid_content);
        parentPhoneNumContent = root.findViewById(R.id.parent_phone_num_content);
        kidPhoto = root.findViewById(R.id.kid_photo);
        parentPhoto = root.findViewById(R.id.parent_photo);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_children));
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

        kidNameContainer.setOnClickListener(lView -> new MainDialog("Имя", currentKid.getName(),
                kidNameContent).show(getParentFragmentManager(), "dialog"));
        kidPatronymicContainer.setOnClickListener(lView -> new MainDialog("Отчество", currentKid.getPatronymic(),
                kidPatronymicContent).show(getParentFragmentManager(), "dialog"));
        kidSurnameContainer.setOnClickListener(lView -> new MainDialog("Фамилия", currentKid.getSurname(),
                kidSurnameContent).show(getParentFragmentManager(), "dialog"));
        kidAgeContainer.setOnClickListener(lView -> new MainDialog("Возраст", currentKid.getAge(),
                kidAgeContent).show(getParentFragmentManager(), "dialog"));
        kidLockerContainer.setOnClickListener(lView -> new MainDialog("Шкафчик", currentKid.getLocker().toString(),
                kidLockerContent).show(getParentFragmentManager(), "dialog"));
        kidBloodTypeContainer.setOnClickListener(lView -> new MainDialog("Группа Крови", currentKid.getBloodType(),
                kidBloodTypeContent).show(getParentFragmentManager(), "dialog"));
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
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
            actionBarTitle = getString(R.string.title_children);
            viewToShow = childrenListLayout;
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
        parentsRecyclerView.setAdapter(new ParentsRecyclerViewAdapter(kid, this));
        parentsRecyclerView.setNestedScrollingEnabled(false);
        parentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        Animation animation = ViewDriver.showView(kidInfoContainer, R.anim.show_right_animation, context);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                childrenListLayout.setVisibility(View.GONE);
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
