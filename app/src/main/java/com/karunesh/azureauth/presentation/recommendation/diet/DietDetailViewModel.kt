package com.karunesh.azureauth.presentation.recommendation.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azure.data.model.Query
import com.azure.data.model.service.ListResponse
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.business.interactors.CosmosInteractors
import com.karunesh.azureauth.business.interactors.cosmos.QueryForNutritionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietDetailViewModel
@Inject
constructor(
    private val cosmosInteractors: CosmosInteractors
) : ViewModel() {

    private val _dietDetailState: MutableSharedFlow<DietDetailState> = MutableSharedFlow()
    val dietDetailState: SharedFlow<DietDetailState> get() = _dietDetailState

    suspend fun queryDetailDiet(
        collectionName: String,
        databaseName: String,
        query: Query
    ) {
        val queryForNutritionData = QueryForNutritionData(
            collectionName, databaseName, query
        )
        cosmosInteractors.queryDietDetailDocument.outputUploadResults(queryForNutritionData) {
            viewModelScope.launch {
                _dietDetailState.emit(DietDetailState.DietDetailList(it))
            }
        }
    }

}

sealed class DietDetailState {
    data class DietDetailList(var dietDetailList: Result<ListResponse<out DetailsDocument>>) :
        DietDetailState()

}