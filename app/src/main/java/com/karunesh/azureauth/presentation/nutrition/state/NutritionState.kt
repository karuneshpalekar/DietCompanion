package com.karunesh.azureauth.presentation.nutrition.state

import com.azure.data.model.service.ListResponse
import com.azure.data.model.service.Response
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument


sealed class NutritionState {

    data class NutritionDataList(var listOfNutritionData: Result<ListResponse<out NutritionDataDocument>>? = null) :
        NutritionState()

    data class DocumentAddCallback(var callback: Result<Response<NutritionDataDocument>>? = null) :
        NutritionState()
}
