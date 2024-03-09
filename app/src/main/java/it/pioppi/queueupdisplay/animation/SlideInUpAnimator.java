package it.pioppi.queueupdisplay.animation;

import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class SlideInUpAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationY(-holder.itemView.getHeight());
        holder.itemView.setAlpha(0);
        ViewCompat.animate(holder.itemView)
                .translationY(0)
                .alpha(1)
                .setInterpolator(new DecelerateInterpolator(1.0f))
                .setDuration(1800)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchAddStarting(holder);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        dispatchAddFinished(holder);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        view.setTranslationY(0);
                        view.setAlpha(1);
                    }
                })
                .start();

        return false;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .translationY(holder.itemView.getHeight())
                .alpha(0)
                .setInterpolator(new DecelerateInterpolator(1.0f))
                .setDuration(getRemoveDuration())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchRemoveStarting(holder);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        dispatchRemoveFinished(holder);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        view.setTranslationY(0);
                        view.setAlpha(1);
                    }
                })
                .start();

        return false;
    }
}
