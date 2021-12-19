package com.karunesh.azureauth.presentation.nutrition.state

import com.azure.data.model.service.ListResponse
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument

sealed class NutritionHistoryState {

    data class NutritionDataList(var listOfNutritionData: Result<ListResponse<out NutritionDataDocument>>? = null) :
        NutritionHistoryState()

}