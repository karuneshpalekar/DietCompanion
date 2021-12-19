package com.karunesh.azureauth.presentation.nutrition.adapter.history

import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument

interface NutritionHistoryListener {
    fun onRecyclerViewItemClicked(product: NutritionDataDocument)
}