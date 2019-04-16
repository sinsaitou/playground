package com.playground.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.playground.MasterFragment
import com.playground.item.MainTabItem

class CustomPagerAdapter : FragmentPagerAdapter {

    private val mItems = mutableListOf<MainTabItem>()

    private val mCache = mutableMapOf<Int, Fragment>()

    constructor(fm: FragmentManager) : super(fm) {
        mItems.add(MainTabItem.SEARCH)
    }

    private fun createFragment(item: MainTabItem): Fragment {
        return when (item) {
            com.playground.item.MainTabItem.SEARCH -> MasterFragment()
        }
    }

    fun indexOfTabItem(item: MainTabItem): Int {
        return mItems.indexOf(item)
    }

    override fun getItem(position: Int): Fragment {
        val item = mItems[position]
        var f = getCacheItemAt(position) ?: createFragment(item)
        mCache[position] = f
        return f
    }

    override fun getCount() : Int {
        return mItems.size
    }

    private fun getCacheItemAt(position: Int): Fragment? {
        return this.mCache[position]
    }
}
