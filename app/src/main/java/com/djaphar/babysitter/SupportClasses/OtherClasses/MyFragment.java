package com.djaphar.babysitter.SupportClasses.OtherClasses;

import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class MyFragment extends Fragment {

    public boolean everythingIsClosed() {return false;}
    public void backWasPressed(){}
    public void returnFieldValue(String value, View calledView){}
    public void newBtnWasPressed(){}
}
