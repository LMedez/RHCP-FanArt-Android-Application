package com.luc.resources.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.animation.AnimationUtils

private const val STATE_SCROLLED_DOWN = 1
private const val STATE_SCROLLED_UP = 2
private var currentState = STATE_SCROLLED_UP
private var currentAnimator: ViewPropertyAnimator? = null
const val ENTER_ANIMATION_DURATION = 325
const val EXIT_ANIMATION_DURATION = 225

fun View.slideUp(onEnd: () -> Unit = {}) {
    if (currentState == STATE_SCROLLED_UP) {
        return
    }
    if (currentAnimator != null) {
        currentAnimator!!.cancel()
        this.clearAnimation()
    }
    currentState = STATE_SCROLLED_UP

    animateChildTo(
        this,
        0,
        ENTER_ANIMATION_DURATION.toLong(),
        AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR, { onEnd() })
}

fun View.slideDown(onEnd: () -> Unit = {}) {
    if (currentState == STATE_SCROLLED_DOWN) {
        return
    }

    if (currentAnimator != null) {
        currentAnimator!!.cancel()
        this.clearAnimation()
    }

    currentState = STATE_SCROLLED_DOWN
    animateChildTo(
        this,
        height,
        EXIT_ANIMATION_DURATION.toLong(),
        AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR, { onEnd() })
}


private fun animateChildTo(
    child: View, targetY: Int, duration: Long, interpolator: TimeInterpolator, onEnd: () -> Unit,
) {
    currentAnimator = child
        .animate()
        .translationY(targetY.toFloat())
        .setInterpolator(interpolator)
        .setDuration(duration)
        .setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                    onEnd()
                }
            })
}

private fun scaleXY(
    view: View,
    to: Float,
    interpolatorApplied: TimeInterpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR,
) {
    view.animate().scaleX(to).scaleY(to).apply {
        interpolator = interpolatorApplied
        duration = 300
    }
}

fun View.scaleXY(to: Float) {
    this.scaleX = to
    this.scaleY = to
}

fun ImageButton.startFavAnimation() {

    if (this.scaleX == 0f) scaleXY(this, 1f, OvershootInterpolator(3f))
    else scaleXY(this, 0f, AnticipateInterpolator(3f))
}

fun startSwapTextToImageViewAnimation(textView: TextView, imageView: ImageView, swapIn: Boolean) {
    if (swapIn) {
        scaleXY(textView, 0f)
        scaleXY(imageView, 1f)
    } else {
        scaleXY(textView, 1f)
        scaleXY(imageView, 0f)
    }
}

fun ImageView.loadUrl(imageUrl: String) {
    Glide.with(this.context)
        .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade(200))
        .into(this)

}

// TextViewExtensions
fun TextView.setTextWithAnimation(
    newText: String,
    durationMs: Long = 150,
) {
    animate().alpha(0f).setUpdateListener {
        if (it.animatedValue as Float > 0.7){
            text = newText
            it.reverse()
        }

    }.duration = durationMs
}

fun ImageView.loadUrlWithAnimation(imageUrl: String) {
    animate().alpha(0f).setUpdateListener {
        if (it.animatedValue as Float > 0.7) {
            loadUrl(imageUrl)
            it.reverse()
        }
    }.duration = 100
}
