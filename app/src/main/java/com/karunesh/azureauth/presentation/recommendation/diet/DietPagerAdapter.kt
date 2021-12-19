package com.karunesh.azureauth.presentation.recommendation.diet

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.karunesh.azureauth.presentation.recommendation.diet.AllDietFragment

const val DIET_LIST_PAGE_INDEX = 0
const val RECOMMENDED_DIET_PAGE_INDEX = 1

class DietPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        DIET_LIST_PAGE_INDEX to { AllDietFragment() },
        RECOMMENDED_DIET_PAGE_INDEX to { RecommendedDietFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}