package com.mobymagic.bakingapp.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mobymagic.bakingapp.R;

import timber.log.Timber;

public abstract class BaseActivity extends AppCompatActivity {

    // region Member Variables
    private boolean mDestroyed = false;
    private boolean mStopped = false;

    //Since most activities will have a toolbar, it makes sense to have this in our base for easy access
    private @Nullable Toolbar mToolbar;

    private @Nullable ProgressDialog mProgressDialog;
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("Activity created");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        Timber.d("Activity started");
        super.onStart();
        mStopped = false;
    }

    @Override
    protected void onStop() {
        Timber.d("Activity stopped");
        mStopped = true;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Timber.d("Activity destroyed");
        mDestroyed = true;
        super.onDestroy();
    }
    // endregion

    // region Toolbar Helper Methods
    /**
     * Convenience method for setting up the toolbar in an activity that has a toolbar view in its
     * layout. The layout must contain a toolbar with and id => 'toolbar'
     */
    public void setupToolbar() {
        Timber.d("Setting up toolbar");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }


    /**
     * Convenience method for setting up the toolbar with a back button in an activity that has a
     * toolbar view in its layout. The layout must contain a toolbar with and id => 'toolbar'
     */
    public void setupToolbarWithBackButton() {
        Timber.d("Setting up toolbar");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public @Nullable Toolbar getToolbar() {
        return mToolbar;
    }
    // endregion

    // region Menu Item Selected Listener
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Timber.i("Home up button clicked");
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // endregion

    // region Helper Methods

    protected boolean isStopped() {
        return mStopped;
    }

    /**
     * Returns true if the final {@link #onDestroy()} call has been made
     * on the Activity, so this instance is now dead.
     */
    public boolean isDestroyed() {
        return mDestroyed;
    }

    @NonNull
    public ProgressDialog showProgressDialog(@StringRes int dialogMessageRes) {
        return showProgressDialog(getString(dialogMessageRes));
    }

    @NonNull
    private ProgressDialog showProgressDialog(@NonNull String dialogMessage) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(dialogMessage);
        mProgressDialog.show();
        return mProgressDialog;
    }

    public void hideProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    // endregion

}
