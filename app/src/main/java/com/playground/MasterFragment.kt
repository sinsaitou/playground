package com.playground

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.playground.card.CardActivity
import com.playground.card.Spot
import com.playground.databinding.FragmentMainBinding
import com.playground.transitions.MasterTransitionSet
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class MasterFragment : Fragment(),
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

    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupExitSharedElementCallback()
        setupRecyclerView()
    }

    private fun setupExitSharedElementCallback() {
        exitTransition = TransitionInflater.from(context)
                .inflateTransition(R.transition.transition_grid_exit)
        requireActivity().window.sharedElementExitTransition = MasterTransitionSet().apply {
            interpolator = LinearInterpolator()
        }

        requireActivity().setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>?, sharedElements: MutableMap<String, View?>?) {
                Log.d("★", "MasterFragment setExitSharedElementCallback#onMapSharedElements $selectedPosition")
                binding.itemList
                        .findViewHolderForAdapterPosition(selectedPosition)?.itemView?.let { itemView ->
                    //itemView.findViewById<ImageView>(R.id.item_image)?.let { imageView ->
                    itemView.findViewById<ImageView>(R.id.item_image)?.let { imageView ->
                        sharedElements?.let { elements ->
                            Log.d("★", "＋＋＋＋＋[${imageView.id}][${imageView.tag}]")
                            elements.clear()
                            elements.put(spots[selectedPosition].url, imageView)
                        }
                    }
                }
            }

            override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
                super.onRejectSharedElements(rejectedSharedElements)
                Log.d("★", "MasterFragment setExitSharedElementCallback#onRejectSharedElements")
                rejectedSharedElements?.mapIndexed { index, view ->
                    Log.d("★", "==>>> onRejectSharedElements[${index}][${view}]")
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.itemList.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.itemList.adapter = SimpleItemRecyclerViewAdapter(createSpots(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("★", "onActivityResult")
    }

    fun onReenter(data: Intent) {
        Log.d("★", "onReenter")

        val initPosition = data.getIntExtra(ARG_INIT_POSITION, -1)
        val position = data.getIntExtra(ARG_POSITION, -1)
        selectedPosition = position + initPosition
        Log.d("★","initPosition[$initPosition], position[$position]")

        binding.itemList.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding.itemList.viewTreeObserver.removeOnPreDrawListener(this)
                requireActivity().supportStartPostponedEnterTransition()
                return true
            }
        })
        binding.itemList.scrollToPosition(selectedPosition)
    }

    override fun onItemClick(position: Int, spot: Spot, imageView: ImageView) {

        selectedPosition = position
        val options1 = makeSceneTransitionAnimation(
                requireActivity(), imageView,
                "${spot.url}")

        val cardList = spots.takeLast(spots.size - position)
        val intent = Intent(requireActivity(), CardActivity::class.java).apply {
            val bundle = Bundle().apply {
                putSerializable(ARG_ITEM_IDS, ArrayList(cardList))
                putExtra(ARG_POSITION, 0)
                putExtra(ARG_INIT_POSITION, position)
            }
            putExtras(bundle)
        }

        startActivityForResult(intent, REQUEST_CODE_POSITION, options1.toBundle())
//        startActivityForResult(intent, REQUEST_CODE_POSITION)

        //binding.itemList.adapter = SimpleItemRecyclerViewAdapter(createSpots(), this)

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
