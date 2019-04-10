package com.playground.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class RoundedImageView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet), Target {
    constructor(context: Context) : this(context, null)

    private var drawable: Drawable? = null
        set(value) {
            field = value
            postInvalidate()
        }

    fun loadImage(url: String?) {
        Log.d("★", "loadImage[$url]")
        if (url == null) {
            drawable = null
        } else {
            Picasso.get()
                    .load(url)
                    .placeholder(android.R.color.white)
                    .error(android.R.color.white)
                    .into(this)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d("★", "onDraw")
        drawable?.setBounds(0, 0, width, height)
        drawable?.draw(canvas)
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        Log.d("★", "onPrepareLoad")
        drawable = placeHolderDrawable
    }


    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        Log.d("★", "onBitmapFailed")
        drawable = errorDrawable
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        Log.d("★", "onBitmapLoaded")
        val roundedDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedDrawable.isCircular = true
        //roundedDrawable.cornerRadius = dp2px(DEFAULT_RADIUS)
        drawable = roundedDrawable
    }

    private fun dp2px(dp: Int): Float {
        return (dp * Resources.getSystem().getDisplayMetrics().density)
    }

    companion object {
        private const val DEFAULT_RADIUS = 4
    }
}