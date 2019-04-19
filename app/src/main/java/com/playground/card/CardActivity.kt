package com.playground.card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import com.playground.R
import com.playground.SingleDetailActivity
import com.playground.databinding.ActivityCardBinding
import com.playground.transitions.CustomTransition
import com.yuyakaido.android.cardstackview.*
import io.reactivex.disposables.CompositeDisposable

class CardActivity : AppCompatActivity(),
        CardStackListener,
        CardStackAdapter.OnViewHolderListener {

    companion object {
        val REQUEST_CODE_POSITION = 12345
        val ARG_ITEM = "item"
        val ARG_ITEM_IDS = "item_ids"
        val ARG_POSITION = "position"
        val ARG_INIT_POSITION = "init_position"
    }

    private val disposables = CompositeDisposable()
    private lateinit var binding: ActivityCardBinding

    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val items by lazy { intent.getSerializableExtra(ARG_ITEM_IDS) as List<Spot> }
    private val position by lazy { intent.getIntExtra(ARG_POSITION, 0) }
    private val initPosition by lazy { intent.getIntExtra(ARG_INIT_POSITION, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_card)
        title = CardActivity.javaClass.simpleName

        supportPostponeEnterTransition()

        setResultPosition(position)
        setupCardStackView()
        setupButton()

        setupEnterSharedElementCallback()
        setupExitSharedElementCallback()
    }

    private fun setupEnterSharedElementCallback() {
        window.enterTransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition_card_exit)
        window.sharedElementEnterTransition = TransitionSet().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            //addTransition(CustomTransition2(12f, 12f, 0f, 0f).addTarget(R.id.card_image))
            addTransition(CustomTransition(12f).addTarget(R.id.card_image))
            addTransition(ChangeBounds().addTarget(R.id.card_image))
            addTransition(ChangeTransform().addTarget(R.id.card_image))
            addTransition(ChangeClipBounds().addTarget(R.id.card_image))
            addTransition(ChangeImageTransform().addTarget(R.id.card_image))
        }

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                val position = manager.topPosition
                binding.cardStackView.findViewWithTag<View>(items[position].url)?.let { view ->
                    Log.d("★", "CardActivity setEnterSharedElementCallback sharedElements view[${view.id}] tag[${view.tag}]")
                    sharedElements.let { elements ->
                        elements.clear()
                        elements[items[position].url] = view
                    }
                }
            }

            override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
                super.onRejectSharedElements(rejectedSharedElements)
                Log.d("★", "CardActivity onRejectSharedElements sharedElements[${rejectedSharedElements?.size ?: 0}]")

            }
        })
    }

    private fun setupExitSharedElementCallback() {
        window.exitTransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition_card_exit)
        window.sharedElementExitTransition = TransitionSet().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            //addTransition(CustomTransition(0f).addTarget(R.id.card_image))
            addTransition(ChangeBounds().addTarget(R.id.card_image))
            addTransition(ChangeTransform().addTarget(R.id.card_image))
            addTransition(ChangeClipBounds().addTarget(R.id.card_image))
            addTransition(ChangeImageTransform().addTarget(R.id.card_image))
        }

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                Log.d("★", "CardActivity setExitSharedElementCallback# onMapSharedElements")
                val position = manager.topPosition
                binding.cardStackView.findViewWithTag<View>(items[position].url)?.let { view ->
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

    override fun onCardDragging(direction: Direction, ratio: Float) {
        //Log.d("★", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        //Log.d("★", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
    }

    override fun onCardRewound() {
        //Log.d("★", "onCardRewound: ${manager.topPosition}")
        setResultPosition(position)
    }

    override fun onCardCanceled() {
        //Log.d("★", "onCardCanceled: ${manager.topPosition}")
        setResultPosition(position)
    }

    override fun onCardAppeared(view: View, position: Int) {
        setResultPosition(position)
    }

    override fun onCardDisappeared(view: View, position: Int) {
    }

    override fun onBindCompleted(position: Int, spot: Spot) {
        if (position == 0) {
            supportStartPostponedEnterTransition()
            Log.d("★", "onBindCompleted $position")
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun onClickItem(position: Int, spot: Spot, imageView: ImageView) {
        Log.d("★", "onItemClick $position")
        val options1 = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, imageView,
                "${spot.url}")
        val intent = Intent(this, SingleDetailActivity::class.java).apply {
            val bundle = Bundle().apply {
                putSerializable(ARG_ITEM, spot)
                putExtra(ARG_POSITION, position)
                putExtra(ARG_INIT_POSITION, initPosition)
            }
            putExtras(bundle)
        }

        ActivityCompat.startActivityForResult(this@CardActivity, intent,
                REQUEST_CODE_POSITION, options1.toBundle())

    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun setupButton() {
        binding.skipButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(200)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }

        binding.rewindButton.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                    .setDirection(Direction.Bottom)
                    .setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .build()
            manager.setRewindAnimationSetting(setting)
            binding.cardStackView.rewind()
        }

        binding.likeButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(200)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }
    }
    override fun onActivityReenter(resultCode: Int, data: Intent) {
        val initPosition = data.getIntExtra(ARG_INIT_POSITION, -1)
        val position = data.getIntExtra(ARG_POSITION, -1)

        Log.d("★", "CardActivity onActivityReenter initPosition[$initPosition] position[$position]")
        setResultPosition(position)
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = CardStackAdapter(items, this)
        binding.cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
        Log.d("★", "CardActivity initialize end")
    }
}
