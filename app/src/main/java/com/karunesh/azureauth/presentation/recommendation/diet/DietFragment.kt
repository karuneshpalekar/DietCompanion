package com.karunesh.azureauth.presentation.recommendation.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.karunesh.azureauth.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DietFragment : Fragment() {

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager2
    private val args: DietFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_diet, container, false)
        tabs = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.diet_view_pager)
        viewPager.adapter = DietPagerAdapter(this)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()


        return view
    }




    private fun getTabTitle(position: Int): String? {
        return when (position) {
            DIET_LIST_PAGE_INDEX -> getString(R.string.diet_list_title)
            RECOMMENDED_DIET_PAGE_INDEX -> getString(R.string.recommended_list_title)
            else -> null
        }
    }


}