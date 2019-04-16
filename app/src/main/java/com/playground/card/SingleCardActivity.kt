package com.playground.card

import android.app.Activity
import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.playground.R
import com.playground.databinding.ActivitySingleCardBinding
import com.playground.transitions.CardTransitionSet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable

class SingleCardActivity : AppCompatActivity() {

    companion object {
        val REQUEST_CODE_POSITION = 12345
        val ARG_ITEM = "item"
        val ARG_ITEM_IDS = "item_ids"
        val ARG_POSITION = "position"
        val ARG_INIT_POSITION = "init_position"
    }

    private val disposables = CompositeDisposable()
    private lateinit var binding: ActivitySingleCardBinding

    private val items by lazy { intent.getSerializableExtra(ARG_ITEM_IDS) as List<Spot> }
    private val position by lazy { intent.getIntExtra(ARG_POSITION, 0) }
    private val initPosition by lazy { intent.getIntExtra(ARG_INIT_POSITION, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_card)
        title = SingleCardActivity.javaClass.simpleName

        supportPostponeEnterTransition()

        setResultPosition(position)
        setupCardStackView()
        setupButton()

        setupEnterSharedElementCallback()
        setupExitSharedElementCallback()
    }

    private fun setupEnterSharedElementCallback() {
//        window.enterTransition = TransitionInflater.from(this)
//                .inflateTransition(R.transition.transition_card_exit)
        window.sharedElementEnterTransition = CardTransitionSet().apply {
            duration = 800
            interpolator = LinearInterpolator()
        }
//        window.sharedElementExitTransition = CardTransitionSet().setDuration(1500)

//        window.sharedElementEnterTransition = TransitionInflater.from(this)
//                .inflateTransition(R.transition.transition_grid_to_card)
//        transition.addListener(object: Transition.TransitionListener {
//            override fun onTransitionStart(transition: Transition?) {
//                val position = manager.topPosition
//                binding.cardStackView.findViewWithTag<View>(items[position].url)?.let { view ->
//                    Log.d("★", "onTransitionStart")
//                    if(view is ImageView) {
//                        Log.d("★", "onTransitionStart ==>> ImageView")
//                        Picasso.get()
//                                .load(items[position].url)
//                                .noFade()
//                                .fit()
//                                .centerInside()
//                                .transform(RoundedCornersTransformation(8, 0))
//                                .error(android.R.color.darker_gray)
//                                .into(view)
//                    }
//                }
//            }
//            override fun onTransitionEnd(transition: Transition?) = Unit
//            override fun onTransitionResume(transition: Transition?) = Unit
//            override fun onTransitionPause(transition: Transition?) = Unit
//            override fun onTransitionCancel(transition: Transition?) = Unit
//        })
//        window.sharedElementEnterTransition = transition

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                binding.root.findViewWithTag<View>(items[position].url)?.let { view ->
                    Log.d("★", "SingleCardActivity sharedElements view[${view.id}]")
                    sharedElements?.let { elements ->
                        Log.d("★", "SingleCardActivity elements not null sharedElements view[${view.id}]")
                        elements.clear()
                        elements[items[position].url] = view
                    }
                }
            }

            override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
                super.onRejectSharedElements(rejectedSharedElements)
                Log.d("★", "onRejectSharedElements sharedElements[${rejectedSharedElements?.size ?: 0}]")

            }
        })
    }

    private fun setupExitSharedElementCallback() {
        window.exitTransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition_card_exit)

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                Log.d("★", "SingleCardActivity setExitSharedElementCallback# onMapSharedElements")
                binding.root.findViewWithTag<View>(items[position].url)?.let { view ->
                    sharedElements?.let { elements ->
                        elements.clear()
                        elements[items[position].url] = view
                    }
                }
            }
        })
    }

    private fun setResultPosition(position: Int) {
        val data = Intent()
        data.putExtra(ARG_POSITION, position)
        data.putExtra(ARG_INIT_POSITION, initPosition)
        setResult(Activity.RESULT_OK, data)
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun setupButton() {
    }

    override fun onActivityReenter(resultCode: Int, data: Intent) {
        val initPosition = data.getIntExtra(ARG_INIT_POSITION, -1)
        val position = data.getIntExtra(ARG_POSITION, -1)

        Log.d("★", "SingleCardActivity onActivityReenter initPosition[$initPosition] position[$position]")
        setResultPosition(position)
    }

    private fun initialize() {
        val spot = items[position]
        binding.cardName.text = "${spot.id}. ${spot.name}"
        binding.cardCity.text = spot.city

        binding.cardImage.tag = "${spot.url}"
        ViewCompat.setTransitionName(binding.cardImage, "${spot.url}")
//        holder.container.tag = "${spot.url}"
//        ViewCompat.setTransitionName(holder.container, "${spot.url}")
        Log.d("★", "SingleCardActivity potision[$position] viewId[${binding.cardImage.id}]")

//        val transformation = RoundedTransformationBuilder()
//                .cornerRadiusDp(8f)
//                .oval(false)
//                .build()

//        Picasso.Builder(onViewHolderListener as Context).memoryCache(Cache.NONE)

        val p = Picasso.get()
        p.isLoggingEnabled = true

        p.load(spot.url)
                .noFade()
                .fit()
                .centerInside()
//                .transform(transformation)
                .error(android.R.color.darker_gray)
                .into(binding.cardImage, object: Callback {
                    override fun onSuccess() {
                        supportStartPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        supportStartPostponedEnterTransition()
                    }
                })

        Log.d("★", "SingleCardActivity initialize end")

    }
}
