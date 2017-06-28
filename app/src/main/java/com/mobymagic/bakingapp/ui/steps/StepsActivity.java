package com.mobymagic.bakingapp.ui.steps;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.data.model.Step;
import com.mobymagic.bakingapp.databinding.ActivityStepsBinding;
import com.mobymagic.bakingapp.ui.base.BaseActivity;

import java.util.ArrayList;

public class StepsActivity extends BaseActivity {

    // region Constants
    private static final String EXTRA_STEPS_LIST = "EXTRA_STEPS_LIST";
    private static final String EXTRA_SELECTED_RECIPE_NAME = "EXTRA_SELECTED_RECIPE_NAME";
    private static final String EXTRA_CURRENT_STEP = "EXTRA_CURRENT_STEP";
    // endregion

    // region Member Variables
    private ActivityStepsBinding mViewBinding;
    // endregion

    // region New Intent Creation
    public static Intent newIntent(@NonNull Context context,
                                   @NonNull ArrayList<Step> stepList,
                                   @NonNull String recipeName,
                                   int currentStep) {
        Intent intent = new Intent(context, StepsActivity.class);
        intent.putExtra(EXTRA_STEPS_LIST, stepList);
        intent.putExtra(EXTRA_SELECTED_RECIPE_NAME, recipeName);
        intent.putExtra(EXTRA_CURRENT_STEP, currentStep);
        return intent;
    }
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_steps);

        final ArrayList<Step> stepList = getIntent().getExtras().getParcelableArrayList(EXTRA_STEPS_LIST);
        final int currentStep = getIntent().getExtras().getInt(EXTRA_CURRENT_STEP);
        String currentRecipeName = getIntent().getExtras().getString(EXTRA_SELECTED_RECIPE_NAME);

        setupToolbarWithBackButton();
        setTitle(currentRecipeName);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mViewBinding.tabLayout.setVisibility(View.GONE);

            if(getToolbar() != null) {
                getToolbar().setVisibility(View.GONE);
            }
        }

        if(stepList == null) {
            // Shouldn't happen, but just in case
            throw new RuntimeException("Steps List was not passed to StepsActivity");
        }

        mViewBinding.viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                                  @Override
                                  public Fragment getItem(int position) {
                                      return StepsFragment.newInstance(stepList.get(position));
                                  }

                                  @Override
                                  public int getCount() {
                                      return stepList.size();
                                  }

                                  @Override
                                  public CharSequence getPageTitle(int position) {
                                      return getString(R.string.steps_number_format,
                                              (stepList.get(position).getId() + 1));
                                  }

                              });

        mViewBinding.tabLayout.setupWithViewPager(mViewBinding.viewPager);

        mViewBinding.viewPager.setCurrentItem(currentStep);
    }
    // endregion

}
