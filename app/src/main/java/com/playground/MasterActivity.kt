package com.playground

import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.playground.card.CardActivity
import com.playground.card.Spot
import com.playground.databinding.ActivityMainBinding
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class MasterActivity : AppCompatActivity(),
        SimpleItemRecyclerViewAdapter.OnItemClickListener {

    companion object {
        val REQUEST_CODE_POSITION = 1000
        val ARG_ITEM = "item"
        val ARG_ITEM_IDS = "item_ids"
        val ARG_POSITION = "position"
        val ARG_INIT_POSITION = "init_position"
    }

    private val spots = createSpots()
    private var selectedPosition: Int = 0
    private val disposables = CompositeDisposable()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        title = MasterActivity.javaClass.simpleName
        setupExitSharedElementCallback()
        setupRecyclerView()
    }

    private fun setupExitSharedElementCallback() {
        window.exitTransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition_grid_exit)
//        window.sharedElementExitTransition = CustomTransitionSet2().apply {
//            interpolator = LinearInterpolator()
//        }

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>?, sharedElements: MutableMap<String, View?>?) {
                Log.d("★", "MasterActivity setExitSharedElementCallback#onMapSharedElements $selectedPosition")
                binding.itemList
                        .findViewHolderForAdapterPosition(selectedPosition)?.itemView?.let { itemView ->
                    //itemView.findViewById<ImageView>(R.id.item_image)?.let { imageView ->
                    itemView.findViewById<ImageView>(R.id.item_image)?.let { imageView ->
                        sharedElements?.let { elements ->
                            Log.d("★", "＋＋＋＋＋[${imageView.id}]")
                            elements.clear()
                            elements.put(spots[selectedPosition].url, imageView)
                        }
                    }
                }
            }

            override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
                super.onRejectSharedElements(rejectedSharedElements)
                Log.d("★", "MasterActivity setExitSharedElementCallback#onRejectSharedElements")
                rejectedSharedElements?.mapIndexed { index, view ->
                    Log.d("★", "==>>> onRejectSharedElements[${index}][${view}]")
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.itemList.layoutManager = GridLayoutManager(this, 3)
        binding.itemList.adapter = SimpleItemRecyclerViewAdapter(createSpots(), this)
    }

    override fun onActivityReenter(resultCode: Int, data: Intent) {
        val initPosition = data.getIntExtra(ARG_INIT_POSITION, -1)
        val position = data.getIntExtra(ARG_POSITION, -1)
        selectedPosition = position + initPosition
        supportPostponeEnterTransition()

        binding.itemList.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding.itemList.viewTreeObserver.removeOnPreDrawListener(this)
                supportStartPostponedEnterTransition()
                return true
            }
        })
        binding.itemList.scrollToPosition(selectedPosition)
    }


    override fun onItemClick(position: Int, spot: Spot, imageView: ImageView) {
    //override fun onItemClick(position: Int, spot: Spot, imageView: RoundedImageView) {

        selectedPosition = position
        val options1 = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, imageView,
                "${spot.url}")

        val cardList = spots.takeLast(spots.size - position)
//        val intent = Intent(this, SingleCardActivity::class.java).apply {
//            val bundle = Bundle().apply {
//                putSerializable(ARG_ITEM_IDS, ArrayList(cardList))
//                putExtra(ARG_POSITION, 0)
//                putExtra(ARG_INIT_POSITION, position)
//            }
//            putExtras(bundle)
//        }
        val intent = Intent(this, CardActivity::class.java).apply {
            val bundle = Bundle().apply {
                putSerializable(ARG_ITEM_IDS, ArrayList(cardList))
                putExtra(ARG_POSITION, 0)
                putExtra(ARG_INIT_POSITION, position)
            }
            putExtras(bundle)
        }

        ActivityCompat.startActivityForResult(this@MasterActivity, intent,
                REQUEST_CODE_POSITION, options1.toBundle())
    }

    override fun onDestroy() {
        disposables.dispose()
        binding.itemList.clearOnScrollListeners()
        super.onDestroy()
    }

    private fun createSpots(): List<Spot> {
        val spots = ArrayList<Spot>()
        spots.add(Spot(name = "Yasaka Shrine", city = "Kyoto", url = "https://source.unsplash.com/Xq1ntWruZQI/600x800"))
        spots.add(Spot(name = "Fushimi Inari Shrine", city = "Kyoto", url = "https://source.unsplash.com/NYyCqdBOKwc/600x800"))
        spots.add(Spot(name = "Bamboo Forest", city = "Kyoto", url = "https://source.unsplash.com/buF62ewDLcQ/600x800"))
        spots.add(Spot(name = "Brooklyn Bridge", city = "New York", url = "https://source.unsplash.com/THozNzxEP3g/600x800"))
        spots.add(Spot(name = "Empire State Building", city = "New York", url = "https://source.unsplash.com/USrZRcRS2Lw/600x800"))
        spots.add(Spot(name = "The statue of Liberty", city = "New York", url = "https://source.unsplash.com/PeFk7fzxTdk/600x800"))
        spots.add(Spot(name = "Louvre Museum", city = "Paris", url = "https://source.unsplash.com/LrMWHKqilUw/600x800"))
        spots.add(Spot(name = "Eiffel Tower", city = "Paris", url = "https://source.unsplash.com/HN-5Z6AmxrM/600x800"))
        spots.add(Spot(name = "Big Ben", city = "London", url = "https://source.unsplash.com/CdVAUADdqEc/600x800"))
        spots.add(Spot(name = "Great Wall of China", city = "China", url = "https://source.unsplash.com/AWh9C-QjhE4/600x800"))
        return spots
    }

}
