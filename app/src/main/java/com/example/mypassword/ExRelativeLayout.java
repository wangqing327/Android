package com.example.mypassword;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class ExRelativeLayout extends RelativeLayout {


    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;
    private boolean mIsExpand;

    public ExRelativeLayout(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public ExRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public ExRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initExpandView();
    }

    private void initExpandView() {


        mExpandAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand);
        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.VISIBLE);
                //下面这行代码必不可少，编辑框会失去焦点
                findViewById(R.id.ssk).requestFocus();
            }
        });

        mCollapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.collapse);
        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
            }
        });

    }

    public void collapse() {
        if (mIsExpand) {
            mIsExpand = false;
            clearAnimation();
            startAnimation(mCollapseAnimation);

        }
    }

    public void expand() {
        if (!mIsExpand) {
            mIsExpand = true;
            clearAnimation();
            startAnimation(mExpandAnimation);
        }
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    public void setContentView() {
        View view;
        view = LayoutInflater.from(getContext()).inflate(R.layout.exrelative, null);
        removeAllViews();
        addView(view);
    }


}
