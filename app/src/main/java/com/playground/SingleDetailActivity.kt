package com.playground

import android.app.Activity
import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.playground.card.Spot
import com.playground.databinding.ActivitySingleDetailBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class SingleDetailActivity : AppCompatActivity() {

    companion object {
        val ARG_ITEM = "item"
        val ARG_POSITION = "position"
        val ARG_INIT_POSITION = "init_position"
    }

    private lateinit var binding: ActivitySingleDetailBinding

    private val spot by lazy { intent.getSerializableExtra(ARG_ITEM) as Spot }
    private val position by lazy { intent.getIntExtra(ARG_POSITION, 0) }
    private val initPosition by lazy { intent.getIntExtra(ARG_INIT_POSITION, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_detail)
        title = SingleDetailActivity.javaClass.simpleName

        supportPostponeEnterTransition()

        setResultPosition()
        setupSpot()

        setupEnterSharedElementCallback()
    }

    private fun setupEnterSharedElementCallback() {
        val transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition_card_to_detail)
        window.sharedElementEnterTransition = transition

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                Log.d("★", "SingleDetailActivity setEnterSharedElementCallback# onMapSharedElements")
                binding.root.findViewWithTag<View>(spot.url)?.let { imageView ->
                    sharedElements?.let { elements ->
                        Log.d("★", "SingleDetailActivity setEnterSharedElementCallback elements[$elements]")
                        elements.clear()
                        elements[spot.url] = imageView
                    }
                }
            }
        })
    }

    private fun setResultPosition() {
        val data = Intent()
        val bundle = Bundle()
        bundle.putInt(ARG_POSITION, position)
        bundle.putInt(ARG_INIT_POSITION, initPosition)
        data.putExtras(bundle)

        setResult(Activity.RESULT_OK, data)
    }

    private fun setupSpot() {
        Picasso.get()
                .load(spot.url)
                .noFade()
                .error(android.R.color.darker_gray)
                .into(binding.detailImage, object: Callback {
                    override fun onSuccess() {
                        supportStartPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        supportStartPostponedEnterTransition()
                    }
                })

        binding.detailImage.tag = "${spot.url}"
        ViewCompat.setTransitionName(binding.detailImage, "${spot.url}")

        binding.detailName.text = "${spot.id}. ${spot.name}"
        binding.detailCity.text = spot.city
    }
}
