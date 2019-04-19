package com.playground

import android.app.Activity
import android.app.SharedElementCallback
import android.content.Intent
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.playground.card.Spot
import com.playground.databinding.ActivitySingleDetailBinding
import com.playground.transitions.DetailTransitionSet

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

        postponeEnterTransition()

        setResultPosition()
        setupFragment()
        setupEnterSharedElementCallback()
    }


    private fun setResultPosition() {
        val data = Intent()
        val bundle = Bundle()
        bundle.putInt(ARG_POSITION, position)
        bundle.putInt(ARG_INIT_POSITION, initPosition)
        data.putExtras(bundle)

        setResult(Activity.RESULT_OK, data)
    }

    override fun onBackPressed() {
        finishAfterTransition()
        super.onBackPressed()
    }

    private fun setupFragment() {
        val singleDetailFragment = SingleDetailFragment.newInstance(spot)
        supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                        R.id.container,
                        singleDetailFragment,
                        SingleDetailFragment::class.java.simpleName)
                .commit()

    }

    private fun setupEnterSharedElementCallback() {
        window.enterTransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition_card_to_detail)
        window.sharedElementEnterTransition = DetailTransitionSet()

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                Log.d("★", "SingleDetailFragment setEnterSharedElementCallback# onMapSharedElements")
                binding.root.findViewWithTag<View>(spot.url)?.let { imageView ->
                    sharedElements?.let { elements ->
                        Log.d("★", "SingleDetailFragment setEnterSharedElementCallback elements[$elements]")
                        elements.clear()
                        elements[spot.url] = imageView
                    }
                }
            }

            override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
                super.onRejectSharedElements(rejectedSharedElements)
                Log.d("★", "onRejectSharedElements Detail rejectedSharedElements[${rejectedSharedElements?.size}]")
            }

            override fun onSharedElementStart(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?,
                                              sharedElementSnapshots: MutableList<View>?) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots)
                Log.d("★", "onSharedElementStart Detail sharedElementNames[${sharedElementNames?.size}] sharedElements[${sharedElements?.size}]")
            }

            override fun onSharedElementEnd(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?,
                                            sharedElementSnapshots: MutableList<View>?) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                Log.d("★", "onSharedElementEnd Detail sharedElementNames[${sharedElementNames?.size}] sharedElements[${sharedElements?.size}]")
            }

            override fun onSharedElementsArrived(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?,
                                                 listener: OnSharedElementsReadyListener?) {
                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener)
                Log.d("★", "onSharedElementsArrived Detail sharedElementNames[${sharedElementNames?.size}] sharedElements[${sharedElements?.size}]")
            }

            override fun onCaptureSharedElementSnapshot(sharedElement: View?, viewToGlobalMatrix: Matrix?,
                                                        screenBounds: RectF?): Parcelable {
                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds)
                Log.d("★", "onCaptureSharedElementSnapshot Detail sharedElement[${sharedElement}] Matrix[${viewToGlobalMatrix}] RectF[$screenBounds]")
            }




        })
    }

}
