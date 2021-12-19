package com.karunesh.azureauth.business.interactors.cosmos

import com.azure.data.model.service.ListResponse
import com.karunesh.azureauth.business.data.abstraction.CosmosStorageB
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument

class AllNutritionDocuments(
    private val cosmosStorageB: CosmosStorageB
) {
    suspend fun outputUploadResults(
        queryForAllNutritionData: QueryForAllNutritionData,
        queryResponse: (Result<ListResponse<out NutritionDataDocument>>) -> Unit
    ) {
        return try {
            val (collectionName,
                databaseName) = queryForAllNutritionData
            cosmosStorageB.queryAllNutritionData(collectionName, databaseName) {
                queryResponse(Result.Success(it))
            }
        } catch (e: Exception) {
            queryResponse(Result.Error(e))
        }
    }
}

data class QueryForAllNutritionData(
    var collectionName: String,
    var databaseName: String
)