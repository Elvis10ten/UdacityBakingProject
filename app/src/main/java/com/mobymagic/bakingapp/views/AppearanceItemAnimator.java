package com.mobymagic.bakingapp.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

public class AppearanceItemAnimator extends DefaultItemAnimator {

    private static int screenHeight = 0;

    private int lastAddAnimatedItem = -2;

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getLayoutPosition() > lastAddAnimatedItem) {
            lastAddAnimatedItem++;
            runEnterAnimation(viewHolder);
            return false;
        }

        dispatchAddFinished(viewHolder);
        return false;
    }

    private void runEnterAnimation(final RecyclerView.ViewHolder holder) {
        final int screenHeight = getScreenHeight(holder.itemView.getContext());
        holder.itemView.setTranslationY(screenHeight);
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.0f))
                .setDuration(700)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }

                }).start();
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

}