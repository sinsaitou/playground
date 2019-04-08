package com.playground.view

import android.content.Context
import android.util.AttributeSet
import com.makeramen.roundedimageview.RoundedImageView
import android.content.res.TypedArray
import com.playground.R


class SquaredRoundedImageView : RoundedImageView {

  private var isSquared: Boolean = false

  constructor(context: Context?) : this(context, null)
  constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    var a: TypedArray? = null
    try {
      a = context?.obtainStyledAttributes(
        attrs, R.styleable.SquaredRoundedImageView, defStyleAttr, 0) ?: return

      isSquared = a.getBoolean(R.styleable.SquaredRoundedImageView_isSquared, false)
    } finally {
      a?.recycle()
    }
  }

  public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if(isSquared) {
      super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    if(isSquared) {
      super.onSizeChanged(w, w, oldw, oldh)
    } else {
      super.onSizeChanged(w, h, oldw, oldh)
    }
  }
}
