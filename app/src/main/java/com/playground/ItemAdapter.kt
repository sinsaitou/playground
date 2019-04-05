package com.playground

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.PagerAdapter
import com.playground.card.Spot
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ItemAdapter(private val items: List<Spot>) : PagerAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val imageView = ImageView(container.context)
        val spot = items[position]
        Picasso.get()
                .load(spot.url)
                .noFade()
                .error(android.R.color.darker_gray)
                .into(imageView, object: Callback {
                    override fun onSuccess() {
                        Log.d("★", "onSuccess")
                    }

                    override fun onError(e: Exception?) {
                        Log.d("★", "onError[$e]")
                    }
                })

        imageView.tag = "item_$position"
        ViewCompat.setTransitionName(imageView, "item_$position")
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(collection: ViewGroup, pos: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}
