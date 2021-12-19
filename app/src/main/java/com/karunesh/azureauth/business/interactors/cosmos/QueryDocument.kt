package com.karunesh.azureauth.business.interactors.cosmos

import com.azure.data.model.Query
import com.azure.data.model.service.ListResponse
import com.karunesh.azureauth.business.data.abstraction.CosmosStorageB
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument

class QueryDocument(
    private val cosmosStorageB: CosmosStorageB
) {
    suspend fun outputUploadResults(
        queryForNutritionData: QueryForNutritionData,
        queryResponse: (Result<ListResponse<out NutritionDataDocument>>) -> Unit
    ) {
        return try {
            val (collectionName,
                databaseName,
                query) = queryForNutritionData
            cosmosStorageB.queryNutritionData(collectionName, databaseName, query) {
                queryResponse(Result.Success(it))
            }
        } catch (e: Exception) {
            queryResponse(Result.Error(e))
        }
    }
}

data class QueryForNutritionData(
    var collectionName: String,
    var databaseName: String,
    var query: Query
)