package com.playground

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.playground.card.Spot
import com.squareup.picasso.Picasso

class SimpleItemRecyclerViewAdapter(
        private var spots: List<Spot> = emptyList(),
        private val listener: OnItemClickListener
) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, spot: Spot, imageView: ImageView)
        //fun onItemClick(position: Int, spot: Spot, imageView: RoundedImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_list_content, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = spots[position]
        holder.name.text = "${spot.id}. ${spot.name}"
        holder.city.text = spot.city

        Picasso.get()
                .load(spot.url)
                .noFade()
                .fit()
                .centerCrop()
                .error(android.R.color.darker_gray)
                .into(holder.image)
        //holder.image.loadImage(spot.url)
        holder.image.tag = "${spot.url}"
        ViewCompat.setTransitionName(holder.image, "${spot.url}")

        holder.itemView.setOnClickListener { _ ->
            listener.onItemClick(position, spot, holder.image)
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
        val name: TextView = view.findViewById(R.id.item_name)
        var city: TextView = view.findViewById(R.id.item_city)
        //var image: RoundedImageView = view.findViewById(R.id.item_image)
        var image: ImageView = view.findViewById(R.id.item_image)
    }

}
