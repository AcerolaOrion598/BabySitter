package com.djaphar.babysitter.SupportClasses.OtherClasses;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.djaphar.babysitter.R;

public class ViewDriver {

    public static Animation showView(View view, int animationResource, Context context) {
        Animation animation = null;

        if (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
            animation = animateView(view, animationResource, context);
        }

        return animation;
    }

    public static Animation hideView(View view, int animationResource, Context context) {
        Animation animation = null;

        if (view.getVisibility() == View.VISIBLE) {
            animation = animateView(view, animationResource, context);
            view.setVisibility(View.GONE);
        }

        return animation;
    }

    private static Animation animateView(View view, int animationResource, Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, animationResource);
        view.startAnimation(animation);
        return animation;
    }

    public static void setStatusTvOptions(TextView tv, Resources resources, boolean isTrue) {
        String status;
        int color;
        if (isTrue) {
            status = resources.getString(R.string.bill_status_true);
            color = resources.getColor(R.color.colorGreen60);
        } else {
            status = resources.getString(R.string.bill_status_false);
            color = resources.getColor(R.color.colorRed60);
        }
        tv.setTextColor(color);
        tv.setText(status);
    }

    public static void toggleChildViewsEnable(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                toggleChildViewsEnable(viewGroup.getChildAt(i), enabled);
            }
        }
    }
}
