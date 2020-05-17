package com.djaphar.babysitter.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.djaphar.babysitter.Activities.MainActivity;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.Adapters.ChildrenRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.Adapters.MainDialog;
import com.djaphar.babysitter.SupportClasses.Adapters.ParentsRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Parent;
import com.djaphar.babysitter.SupportClasses.ApiClasses.UpdatePictureModel;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitter.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitter.ViewModels.ChildrenViewModel;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

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
    private LinearLayout kidNameContainer, kidPatronymicContainer, kidSurnameContainer, kidAgeContainer,
            kidLockerContainer, kidBloodTypeContainer, kidDeleteBtn;
    private ScrollView kidInfoSv, parentInfoSv;
    private TextView kidNameContent, kidPatronymicContent, kidSurnameContent, kidAgeContent, kidLockerContent, kidBloodTypeContent, inviteCodeContent,
            parentNameContent, parentPatronymicContent, parentSurnameContent, parentRoleContent, parentKidContent, parentPhoneNumContent;
    private ImageButton newKidBtn, selectPictureBtn, deletePictureBtn, cancelPictureBtn, savePictureBtn;
    private ImageView kidPhoto, parentPhoto;
    private View currentView;
    private Child currentChild;
    private HashMap<String, String> authHeader = new HashMap<>();

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
        kidDeleteBtn = root.findViewById(R.id.kid_delete_btn);
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
        selectPictureBtn = root.findViewById(R.id.select_picture_btn);
        deletePictureBtn = root.findViewById(R.id.delete_picture_btn);
        cancelPictureBtn = root.findViewById(R.id.cancel_picture_btn);
        savePictureBtn = root.findViewById(R.id.save_picture_btn);
        kidPhoto = root.findViewById(R.id.kid_photo);
        parentPhoto = root.findViewById(R.id.parent_photo);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_children));
            setBackBtnState(View.GONE);
            mainActivity.setNewBtnState(View.GONE);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        childrenViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            authHeader.put(getString(R.string.auth_header_key), user.getToken_type() + " " + user.getAccess_token());
            childrenViewModel.requestChildrenList(authHeader);
        });

        childrenViewModel.getChildren().observe(getViewLifecycleOwner(), children -> {
            if (children == null) {
                return;
            }
            childrenRecyclerView.setAdapter(new ChildrenRecyclerViewAdapter(children, this));
            childrenRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        childrenViewModel.getCurrentChild().observe(getViewLifecycleOwner(), currentChild -> {
            if (currentChild == null) {
                backWasPressed();
                return;
            }
            this.currentChild = currentChild;
            setChildOptions(currentChild);
        });

        kidNameContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.name_title_text), currentChild.getName(), lView)
                .show(getParentFragmentManager(), "dialog"));
        kidPatronymicContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.patronymic_title_text), currentChild.getPatronymic(), lView)
                .show(getParentFragmentManager(), "dialog"));
        kidSurnameContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.surname_title_text), currentChild.getSurname(), lView)
                .show(getParentFragmentManager(), "dialog"));
        kidAgeContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.kid_age_title_text), String.valueOf(currentChild.getAge()), lView)
                .show(getParentFragmentManager(), "dialog"));
        kidLockerContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.kid_locker_title_text), currentChild.getLockerNum(), lView)
                .show(getParentFragmentManager(), "dialog"));
        kidBloodTypeContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.kid_blood_type_title_text), currentChild.getBloodType(), lView)
                .show(getParentFragmentManager(), "dialog"));

        kidDeleteBtn.setOnClickListener(lView ->
                new AlertDialog.Builder(mainActivity)
                .setTitle(R.string.kid_delete_title)
                .setMessage(R.string.kid_delete_message)
                .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> childrenViewModel.requestDeleteChild(authHeader, currentChild.getChildId()))
                .show());

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
                        String name = kidNameEd.getText().toString();
                        if (name.equals("")) {
                            name = null;
                        }
                        String surname = kidSurnameEd.getText().toString();
                        if (surname.equals("")) {
                            surname = null;
                        }
                        String patronymic = kidPatronymicEd.getText().toString();
                        if (patronymic.equals("")) {
                            patronymic = null;
                        }
                        childrenViewModel.requestCreateChild(authHeader, new Child(null,null,
                                null, null, name, surname, patronymic, null, null));
                    })
                    .show();
        });

        selectPictureBtn.setOnClickListener(lView -> {
            if (!currentChild.getPhotoLink().equals(getString(R.string.no_picture_url))) {
                if (deletePictureBtn.getVisibility() == View.VISIBLE) {
                    mainActivity.selectPicture();
                } else {
                    ViewDriver.showView(deletePictureBtn, R.anim.show_round_btn, context);
                    currentView = deletePictureBtn;
                }
            } else {
                mainActivity.selectPicture();
            }
        });

        deletePictureBtn.setOnClickListener(lView -> new AlertDialog.Builder(mainActivity)
                .setTitle(R.string.delete_picture_title)
                .setMessage(R.string.delete_picture_message)
                .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                    childrenViewModel.requestDeletePicture(authHeader,
                            new UpdatePictureModel(currentChild.getChildId(), getString(R.string.child_profile), null));
                    ViewDriver.hideView(deletePictureBtn, R.anim.hide_round_btn, context);
                    currentView = kidInfoContainer;
                })
                .show());

        cancelPictureBtn.setOnClickListener(lView -> backWasPressed());

        savePictureBtn.setOnClickListener(lView -> {
            new PictureUploader(((BitmapDrawable) kidPhoto.getDrawable()).getBitmap(), childrenViewModel,
                    authHeader, currentChild.getChildId(), getString(R.string.child_profile)).execute();
            hideCancelBtn();
        });
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(int visibilityState) {
        mainActivity.setBackBtnState(visibilityState);
    }

    public boolean everythingIsClosed() {
        return parentInfoContainer.getVisibility() != View.VISIBLE && kidInfoContainer.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        switch (currentView.getId()) {
            case R.id.parent_info_container:
                childrenViewModel.requestSingleChild(authHeader, currentChild.getChildId());
                setActionBarTitle(currentChild.getName() + " " + currentChild.getSurname());
                kidInfoContainer.setVisibility(View.VISIBLE);
                ViewDriver.hideView(parentInfoContainer, R.anim.hide_right_animation, context);
                if (deletePictureBtn.getVisibility() == View.VISIBLE) {
                    currentView = deletePictureBtn;
                    break;
                }
                currentView = kidInfoContainer;
                break;
            case R.id.kid_info_container:
                childrenViewModel.requestChildrenList(authHeader);
                setBackBtnState(View.GONE);
                ViewDriver.toggleChildViewsEnable(kidInfoContainer, false);
                setActionBarTitle(getString(R.string.title_children));
                childrenListContainer.setVisibility(View.VISIBLE);
                ViewDriver.hideView(kidInfoContainer, R.anim.hide_right_animation, context);
                currentView = null;
                break;
            case R.id.delete_picture_btn:
                ViewDriver.hideView(deletePictureBtn, R.anim.hide_round_btn, context);
                currentView = kidInfoContainer;
                break;
            case R.id.cancel_picture_btn:
                hideCancelBtn();
                childrenViewModel.requestSingleChild(authHeader, currentChild.getChildId());
                break;
        }
    }

    private void hideCancelBtn() {
        ViewDriver.hideView(cancelPictureBtn, R.anim.hide_round_btn, context).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                savePictureBtn.setVisibility(View.GONE);
                selectPictureBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        currentView = kidInfoContainer;
    }

    public void returnFieldValue(String fieldValue, View calledView) {
        switch (calledView.getId()) {
            case R.id.kid_name_container:
                currentChild.setName(fieldValue);
                break;
            case R.id.kid_patronymic_container:
                currentChild.setPatronymic(fieldValue);
                break;
            case R.id.kid_surname_container:
                currentChild.setSurname(fieldValue);
                break;
            case R.id.kid_age_container:
                currentChild.setAge(Integer.parseInt(fieldValue));
                break;
            case R.id.kid_locker_container:
                currentChild.setLockerNum(fieldValue);
                break;
            case R.id.kid_blood_type_container:
                currentChild.setBloodType(fieldValue);
                break;
        }
        childrenViewModel.requestUpdateChild(authHeader, currentChild.getChildId(), currentChild);
    }

    public void showKidInfo(Child child) {
        childrenViewModel.requestSingleChild(authHeader, child.getChildId());
        currentChild = child;
        kidInfoSv.scrollTo(0, kidInfoSv.getTop());
        setBackBtnState(View.VISIBLE);
        ViewDriver.toggleChildViewsEnable(kidInfoContainer, true);
        currentView = kidInfoContainer;
        ViewDriver.showView(kidInfoContainer, R.anim.show_right_animation, context).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                childrenListContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setChildOptions(Child child) {
        Glide.with(context).load(child.getPhotoLink()).into(kidPhoto);
        setActionBarTitle(child.getName() + " " + child.getSurname());
        kidNameContent.setText(child.getName());
        kidPatronymicContent.setText(child.getPatronymic());
        kidSurnameContent.setText(child.getSurname());

        String ageStr;
        Integer age = child.getAge();
        if (age == null) {
            ageStr = getString(R.string.some_field_is_null);
        } else {
            ageStr = String.valueOf(age);
        }
        kidAgeContent.setText(ageStr);

        String lockerNum = child.getLockerNum();
        if (lockerNum == null || lockerNum.equals("")) {
            lockerNum = getString(R.string.some_field_is_null);
        }
        kidLockerContent.setText(lockerNum);

        String bloodType = child.getBloodType();
        if (bloodType == null || bloodType.equals("")) {
            bloodType = getString(R.string.some_field_is_null);
        }
        kidBloodTypeContent.setText(bloodType);

        inviteCodeContent.setText(child.getChildId());
        parentsRecyclerView.setAdapter(new ParentsRecyclerViewAdapter(child, this));
        parentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void showParentInfo(Parent parent, Child child) {
        Glide.with(context).load(parent.getPhotoLink()).into(parentPhoto);
        parentInfoSv.scrollTo(0, parentInfoSv.getTop());
        setParentOptions(parent, child);
        currentView = parentInfoContainer;
        ViewDriver.showView(parentInfoContainer, R.anim.show_right_animation, context).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                kidInfoContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setParentOptions(Parent parent, Child child) {
        setActionBarTitle(parent.getName() + " " + parent.getSurname());
        parentNameContent.setText(parent.getName());
        parentPatronymicContent.setText(parent.getPatronymic());
        parentSurnameContent.setText(parent.getSurname());
        parentRoleContent.setText(parent.getRelationDegree());
        String kidFullName = child.getName() + " " + child.getPatronymic() + " " + child.getSurname();
        parentKidContent.setText(kidFullName);
        parentPhoneNumContent.setText(String.valueOf(parent.getPhone()));
    }

    public void setSelectedPicture(Uri selectedPictureUri) {
        selectPictureBtn.setVisibility(View.GONE);
        deletePictureBtn.setVisibility(View.GONE);
        cancelPictureBtn.setVisibility(View.VISIBLE);
        savePictureBtn.setVisibility(View.VISIBLE);
        currentView = cancelPictureBtn;
        kidPhoto.setImageURI(selectedPictureUri);
    }

    private static class PictureUploader extends AsyncTask<Void, Void, Void> {
        private Bitmap picture;
        private ChildrenViewModel childrenViewModel;
        private HashMap<String, String> authHeader;
        private String childId, profile;

        PictureUploader(Bitmap picture, ChildrenViewModel childrenViewModel, HashMap<String, String> authHeader,
                        String childId, String profile) {
            this.picture = picture;
            this.childrenViewModel = childrenViewModel;
            this.authHeader = authHeader;
            this.childId = childId;
            this.profile = profile;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            String pictureStr = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            childrenViewModel.requestUpdatePicture(authHeader, new UpdatePictureModel(childId, profile, pictureStr));
            return null;
        }
    }
}
