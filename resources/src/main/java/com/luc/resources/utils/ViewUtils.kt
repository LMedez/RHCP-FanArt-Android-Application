package com.luc.resources.utils

import android.view.View
import android.view.ViewTreeObserver
import com.google.android.material.snackbar.Snackbar
import com.luc.resources.R

fun <T : View> T.height(function: (Int) -> Unit) {
    if (height == 0)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                function(height)
            }
        })
    else function(height)
}

fun showSnackBar(text: String, view: View) =
    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()

fun showSnackBar(view: View,isShuffleEnabled: Boolean, onIsEnabled: () -> Unit, onIsDisabled: () -> Unit ) {
    if (isShuffleEnabled) {
        Snackbar.make(view, R.string.shuffleIsEnabled, Snackbar.LENGTH_SHORT).setAction(R.string.disable) {onIsEnabled()}.show()
    } else {
        Snackbar.make(view, R.string.shuffleEnabled, Snackbar.LENGTH_SHORT).setAction(R.string.undo) {onIsDisabled()}.show()

    }
}