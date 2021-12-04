package com.luc.resources.animation

import android.view.View

fun View.hide(animate: Boolean = false) {

    if (animate) {
        this.animate().apply {
            duration = 500
            alpha(0f)
            withEndAction {
                visibility = View.GONE
            }
            start()
        }
    } else {
        this.visibility = View.GONE
    }
}

fun View.show(animate: Boolean = false) {
    this.visibility = View.VISIBLE
    if (animate) {
        this.alpha = 0f
        this.animate().apply {
            duration = 300
            alpha(1f)
            start()
        }
    }
}