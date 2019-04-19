package com.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.playground.card.Spot
import com.playground.databinding.FragmentSingleDetailBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class SingleDetailFragment : Fragment() {

    companion object {
        val ARG_ITEM = "item"

        fun newInstance(spot: Spot): Fragment {
            return SingleDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ITEM, spot)
                }
            }
        }
    }

    private val binding by lazy { FragmentSingleDetailBinding.inflate(LayoutInflater.from(context)) }
    private val spot by lazy { arguments?.getSerializable(ARG_ITEM) as Spot }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSpot()
    }


    private fun setupSpot() {

        binding.detailImage.tag = "${spot.url}"
        ViewCompat.setTransitionName(binding.detailImage, "${spot.url}")

        Picasso.get()
                .load(spot.url)
                .noFade()
                .error(android.R.color.darker_gray)
                .into(binding.detailImage, object: Callback {
                    override fun onSuccess() {
                        requireActivity().startPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        requireActivity().startPostponedEnterTransition()
                    }
                })

        binding.detailName.text = "${spot.id}. ${spot.name}"
        binding.detailCity.text = spot.city
    }
}
