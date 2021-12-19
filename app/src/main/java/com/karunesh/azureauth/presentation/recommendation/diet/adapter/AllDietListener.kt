package com.karunesh.azureauth.presentation.recommendation.diet.adapter

import com.karunesh.azureauth.presentation.recommendation.diet.DietItem

interface AllDietListener {

    fun onRecyclerViewItemCardViewClicked(product: DietItem)
    fun onRecyclerViewItemClicked(product: DietItem)
    fun onRecyclerViewItemClickRemove(product: DietItem)
}