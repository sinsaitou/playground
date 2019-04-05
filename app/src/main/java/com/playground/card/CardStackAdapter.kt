package com.playground.card

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.playground.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CardStackAdapter(
        private var spots: List<Spot> = emptyList(),
        private val onViewHolderListener: OnViewHolderListener
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    interface OnViewHolderListener {
        fun onBindCompleted(position: Int, spot: Spot)
        fun onClickItem(position: Int, spot: Spot, imageView: ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = spots[position]
        holder.name.text = "${spot.id}. ${spot.name}"
        holder.city.text = spot.city

        holder.image.tag = "${spot.url}"
        ViewCompat.setTransitionName(holder.image, "${spot.url}")
//        holder.container.tag = "${spot.url}"
//        ViewCompat.setTransitionName(holder.container, "${spot.url}")
        Log.d("★", "CardAdapter viewId[${holder.container.id}]")

        Picasso.get()
                .load(spot.url)
                .noFade()
                .error(android.R.color.darker_gray)
                .into(holder.image, object: Callback {
                    override fun onSuccess() {
                        onViewHolderListener.onBindCompleted(position, spot)
                    }

                    override fun onError(e: Exception?) {
                        onViewHolderListener.onBindCompleted(position, spot)
                    }
                })

        holder.itemView.setOnClickListener { v ->
            onViewHolderListener.onClickItem(position, spot, holder.image)
        }
    }

    override fun getItemCount(): Int {
        return spots.size
    }

    fun setSpots(spots: List<Spot>) {
        this.spots = spots
    }

    fun getSpots(): List<Spot> {
        return spots
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: CardView = view.findViewById(R.id.card_container)
        val name: TextView = view.findViewById(R.id.card_name)
        var city: TextView = view.findViewById(R.id.card_city)
        var image: ImageView = view.findViewById(R.id.card_image)
    }

}
