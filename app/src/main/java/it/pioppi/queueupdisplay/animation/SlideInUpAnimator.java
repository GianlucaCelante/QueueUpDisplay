package it.pioppi.queueupdisplay.animation;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.ArrayDeque;
import java.util.Queue;

public class SlideInUpAnimator extends SimpleItemAnimator {

    private Queue<Runnable> animationQueue = new ArrayDeque<>();
    private boolean isAnimating = false;

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Runnable addAnimation = () -> {
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
                            isAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            dispatchAddFinished(holder);
                            isAnimating = false;
                            runNextAnimation();
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            view.setTranslationY(0);
                            view.setAlpha(1);
                            isAnimating = false;
                            runNextAnimation();
                        }
                    })
                    .start();
        };

        if (isAnimating) {
            animationQueue.add(addAnimation);
        } else {
            addAnimation.run();
        }

        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        Runnable moveAnimation = () -> {
            holder.itemView.setTranslationX(fromX - toX);
            holder.itemView.setTranslationY(fromY - toY);
            ViewCompat.animate(holder.itemView)
                    .translationX(0)
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(1.0f))
                    .setDuration(1800)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            dispatchMoveStarting(holder);
                            isAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            dispatchMoveFinished(holder);
                            isAnimating = false;
                            runNextAnimation();
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            view.setTranslationX(0);
                            view.setTranslationY(0);
                            isAnimating = false;
                            runNextAnimation();
                        }
                    })
                    .start();
        };

        if (isAnimating) {
            animationQueue.add(moveAnimation);
        } else {
            moveAnimation.run();
        }
        return true;
    }


    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        Runnable changeAnimation = () -> {
            // Definisci una trasformazione o una transizione appropriata
            ViewCompat.animate(newHolder.itemView)
                    .translationX(toLeft - fromLeft)
                    .translationY(toTop - fromTop)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(1800)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            dispatchChangeStarting(newHolder, true);
                            isAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            dispatchChangeFinished(newHolder, true);
                            isAnimating = false;
                            runNextAnimation();
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            view.setTranslationX(0);
                            view.setTranslationY(0);
                            isAnimating = false;
                            runNextAnimation();
                        }
                    })
                    .start();
        };

        if (isAnimating) {
            animationQueue.add(changeAnimation);
        } else {
            changeAnimation.run();
        }
        return true;
    }


    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        Runnable removeAnimation = () -> {
            ViewCompat.animate(holder.itemView)
                    .translationY(holder.itemView.getHeight())
                    .alpha(0)
                    .setInterpolator(new DecelerateInterpolator(1.0f))
                    .setDuration(getRemoveDuration())
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            dispatchRemoveStarting(holder);
                            isAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            dispatchRemoveFinished(holder);
                            isAnimating = false;
                            runNextAnimation();
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            view.setTranslationY(0);
                            view.setAlpha(1);
                            isAnimating = false;
                            runNextAnimation();
                        }
                    })
                    .start();
        };

        if (isAnimating) {
            animationQueue.add(removeAnimation);
        } else {
            removeAnimation.run();
        }

        return false;
    }

    private void runNextAnimation() {
        if (!animationQueue.isEmpty() && !isAnimating) {
            Runnable nextAnimation = animationQueue.poll();
            if (nextAnimation != null) {
                nextAnimation.run();
            }
        }
    }

    @Override
    public void runPendingAnimations() {
        if (!isAnimating && !animationQueue.isEmpty()) {
            runNextAnimation();
        }
    }

    @Override
    public void endAnimation(@NonNull RecyclerView.ViewHolder item) {
        // Placeholder: End animations for specific item, if applicable.
    }

    @Override
    public void endAnimations() {
        // Placeholder: End all ongoing animations, if applicable.
    }

    @Override
    public boolean isRunning() {
        return isAnimating || !animationQueue.isEmpty();
    }
}
