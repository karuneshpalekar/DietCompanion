package com.karunesh.azureauth.presentation.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azure.data.model.Query
import com.karunesh.azureauth.business.interactors.CosmosInteractors
import com.karunesh.azureauth.business.interactors.cosmos.CreateCollectionDataClass
import com.karunesh.azureauth.business.interactors.cosmos.CreateDocumentDataClass
import com.karunesh.azureauth.business.interactors.cosmos.QueryForAllNutritionData
import com.karunesh.azureauth.business.interactors.cosmos.QueryForNutritionData
import com.karunesh.azureauth.presentation.nutrition.state.NutritionHistoryState
import com.karunesh.azureauth.presentation.nutrition.state.NutritionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel
@Inject
constructor(
    private val cosmosInteractors: CosmosInteractors
) : ViewModel() {

    private val _nutritionData: MutableSharedFlow<NutritionState> = MutableSharedFlow()
    val nutritionData: SharedFlow<NutritionState> get() = _nutritionData

    private val _nutritionHistory: MutableSharedFlow<NutritionHistoryState> = MutableSharedFlow()
    val nutritionHistory: SharedFlow<NutritionHistoryState> get() = _nutritionHistory

    suspend fun createCollection(
        collectionName: String,
        databaseName: String,
        partitionKey: String,
    ) {
        val createCollectionDataClass = CreateCollectionDataClass(
            collectionName, databaseName, partitionKey
        )
        cosmosInteractors.createCollection.outputUploadResults(createCollectionDataClass) {

        }
    }

    suspend fun queryDocument(
        collectionName: String,
        databaseName: String,
        query: Query
    ) {
        val queryForNutritionData = QueryForNutritionData(
            collectionName, databaseName, query
        )
        cosmosInteractors.queryDocument.outputUploadResults(queryForNutritionData) { response ->
            viewModelScope.launch {
                _nutritionData.emit(NutritionState.NutritionDataList(response))
            }
        }

    }

    suspend fun getDocuments(
        collectionName: String,
        databaseName: String
    ) {
        val queryForNutritionData = QueryForAllNutritionData(
            collectionName, databaseName
        )
        cosmosInteractors.allNutritionDocuments.outputUploadResults(queryForNutritionData) {
            viewModelScope.launch {
                _nutritionHistory.emit(NutritionHistoryState.NutritionDataList(it))
            }
        }
    }

    suspend fun createDocument(
        document: NutritionDataDocument,
        collectionName: String,
        databaseName: String,
        partitionKey: String
    ) {
        val createDocumentDataClass = CreateDocumentDataClass(
            document, collectionName, databaseName, partitionKey
        )
        cosmosInteractors.createDocument.outputUploadResults(createDocumentDataClass) {
            viewModelScope.launch {
                _nutritionData.emit(NutritionState.DocumentAddCallback(it))
            }
        }
    }

}

