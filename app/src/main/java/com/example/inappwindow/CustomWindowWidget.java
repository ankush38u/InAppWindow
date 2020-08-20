package com.example.inappwindow;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

public class CustomWindowWidget extends PopupWindow {

    public static final int LAYOUT_FULL_SCREEN = 1;
    public static final int LAYOUT_HALF_SCREEN = 2;
    public static final int LAYOUT_MINI_SCREEN = 3;
    public static final int LAYOUT_ARROW = 4;
    private static final int ANIMATION_TIME = 1000;
    private static final String TAG = CustomWindowWidget.class.getCanonicalName();
    private Context context;
    private int mPosX = 0, mPosY = 0;
    private Handler handler;
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private int currentLayout;
    private int width = 0;
    private int height = 0;
    private RelativeLayout rootLayout;

    private int frameCount;


    public CustomWindowWidget(@NonNull Context context) {
        super(context);
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(PixelDpConverter.dpToPx(8, context));
        }
        setOutsideTouchable(false);
        setFocusable(false);
        setTouchable(true);
        setClippingEnabled(false);
        handler = new Handler();
        SCREEN_WIDTH = PixelDpConverter.getScreenWidthPixels(context);
        SCREEN_HEIGHT = PixelDpConverter.getScreenHeightPixels(context);
        //setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void show(View parent, int layout_mode) {
        rootLayout = getContentView().findViewById(R.id.root_layout);
        rootLayout.setOnClickListener(v -> {
            if (currentLayout == LAYOUT_MINI_SCREEN) {
                animate(0, 0, SCREEN_WIDTH / 3, SCREEN_WIDTH, SCREEN_HEIGHT / 3, SCREEN_HEIGHT, LAYOUT_FULL_SCREEN);
            } else if (currentLayout == LAYOUT_FULL_SCREEN) {
                animate(SCREEN_WIDTH - SCREEN_WIDTH / 3, 0, SCREEN_WIDTH, SCREEN_WIDTH / 3, SCREEN_HEIGHT, SCREEN_HEIGHT / 3, LAYOUT_MINI_SCREEN);
            }
        });
        showAtLocation(parent, Gravity.TOP | Gravity.LEFT, mPosX, mPosY); //Solves Android pie bug need to start from 0,0 to start coordinate system from top left
        changeLayoutMode(layout_mode);
    }


    private void changeLayoutMode(int layout) {
        switch (layout) {
            case LAYOUT_FULL_SCREEN:
                mPosX = 0;
                mPosY = 0;
                update(mPosX, mPosY, SCREEN_WIDTH, SCREEN_HEIGHT);
                break;
            case LAYOUT_HALF_SCREEN:
                mPosX = 0;
                mPosY = 0;
                update(mPosX, mPosY, SCREEN_WIDTH, SCREEN_HEIGHT / 2);
                break;
            case LAYOUT_MINI_SCREEN:
                mPosX = SCREEN_WIDTH - SCREEN_WIDTH / 3;
                mPosY = 0;
                update(mPosX, mPosY, SCREEN_WIDTH / 3, SCREEN_HEIGHT / 3);
                break;
        }

        currentLayout = layout;
    }


    private void animate(int toX, int toY, int fromWidth, int toWidth, int fromHeight, int toHeight, int toLayoutMode) {
        AnimatorSet animationSet = new AnimatorSet();
        //ValueAnimator.setFrameDelay(40);
        ValueAnimator anim = ValueAnimator.ofInt(fromWidth, toWidth);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            width = val;
        });
        anim.setDuration(ANIMATION_TIME);


        ValueAnimator anim2 = ValueAnimator.ofInt(fromHeight, toHeight);
        anim2.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            height = val;
            frameCount++;
        });
        anim2.setDuration(ANIMATION_TIME);


        ValueAnimator anim3 = ValueAnimator.ofInt(mPosX, toX);
        anim3.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            mPosX = val;
        });
        anim3.setDuration(ANIMATION_TIME);


        ValueAnimator anim4 = ValueAnimator.ofInt(mPosY, toY);
        anim4.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            mPosY = val;
            handler.post(() -> {
                //width = SCREEN_WIDTH - mPosX; //testing if changing width based on position, as the position changes that much width is increased or decreased, should produce better results?
                Log.d(TAG, "Updated mPosX: " + mPosX + ", mPosY: " + mPosY + ", width: " + width + ", height: " + height);
                update(mPosX, mPosY, width, height);
            });
        });
        anim4.setDuration(ANIMATION_TIME);
        animationSet.play(anim).with(anim2).with(anim3).with(anim4);
        //animationSet.setInterpolator(new AnticipateInterpolator());
        animationSet.cancel();
        frameCount = 0;
        animationSet.start();
        animationSet.setDuration(ANIMATION_TIME);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "Number of frames: " + frameCount);
                currentLayout = toLayoutMode;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

}
