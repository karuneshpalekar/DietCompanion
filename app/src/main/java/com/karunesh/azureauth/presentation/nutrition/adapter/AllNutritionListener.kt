package com.karunesh.azureauth.presentation.nutrition.adapter

import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument

interface AllNutritionListener {

    fun onRecyclerViewItemClicked(product: NutritionDataDocument)
    fun onRecyclerViewItemClickRemove(product: NutritionDataDocument)
}