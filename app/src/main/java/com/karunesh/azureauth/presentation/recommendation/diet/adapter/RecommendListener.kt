package com.karunesh.azureauth.presentation.recommendation.diet.adapter

import com.karunesh.azureauth.presentation.recommendation.diet.DietItem

interface RecommendListener {

    fun onRecyclerViewItemClicked(product: DietItem)
}