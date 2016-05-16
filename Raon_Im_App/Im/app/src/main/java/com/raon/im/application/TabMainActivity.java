package com.raon.im.application;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TabHost;

import com.raon.lee.im.R;

/**
 * Created by EunBin on 2016-03-19.
 *
 * This activity is for the tab that contains
 * MainActivty, CompanyListActivity, PermissionListActivity, OptionActivity
 */
@SuppressWarnings("deprecation")
public class TabMainActivity extends TabActivity {

    private BackPressCloseHandler backPressCloseHandler;
    private int currentTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);

        final TabHost mTab = getTabHost();
        currentTab = mTab.getCurrentTab();
        TabHost.TabSpec spec;
        Intent intent;

        // First Tab - Personal Information
        intent = new Intent(this, MainActivity.class);
        spec = mTab.newTabSpec("PersonalDataTab").setIndicator("Personal Data").setContent(intent);
        mTab.addTab(spec);

        // Second Tab - Company List
        intent = new Intent(this, CompanyListActivity.class);
        spec = mTab.newTabSpec("CompanyListTab").setIndicator("Company List").setContent(intent);
        mTab.addTab(spec);

        // Third Tab - Permission List
        intent = new Intent(this, PermissionListActivity.class);
        spec = mTab.newTabSpec("PermissionTab").setIndicator("Permission").setContent(intent);
        mTab.addTab(spec);

        // Fourth Tab - Timer List
        intent = new Intent(this, OptionActivity.class);
        spec = mTab.newTabSpec("OptionTab").setIndicator("Option").setContent(intent);
        mTab.addTab(spec);

        // set the slide effect to the view when changing the tab
        getTabHost().setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                View currentView = getTabHost().getCurrentView();
                if (getTabHost().getCurrentTab() > currentTab) {
                    currentView.setAnimation(inFromRightAnimation());
                } else {
                    currentView.setAnimation(outToRightAnimation());
                }

                currentTab = getTabHost().getCurrentTab();
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    // used for the slide effect
    public Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(240);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public Animation outToRightAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(240);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }
}
