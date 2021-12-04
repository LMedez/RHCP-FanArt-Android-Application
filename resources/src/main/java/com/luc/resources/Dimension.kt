package com.luc.resources

import android.content.Context

fun Context.dimensionFromAttribute(attribute: Int): Int {
        val attributes = obtainStyledAttributes(intArrayOf(attribute))
        val dimension = attributes.getDimensionPixelSize(0, 0)
        attributes.recycle()
        return dimension
    }