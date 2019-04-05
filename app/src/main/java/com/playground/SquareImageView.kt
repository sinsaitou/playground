package com.playground

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 * Created by verno on 2016/12/19.
 */

class SquareImageView constructor(@NonNull context: Context, @Nullable attrs: AttributeSet? = null) : ImageView(context, attrs) {

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
