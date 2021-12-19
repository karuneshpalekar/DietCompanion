package com.karunesh.azureauth.business.interactors.cosmos

import com.azure.data.model.service.ListResponse
import com.karunesh.azureauth.business.data.abstraction.CosmosStorageB
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.presentation.recommendation.diet.DetailsDocument

class QueryDietDetailDocument(
    private val cosmosStorageB: CosmosStorageB
) {
    suspend fun outputUploadResults(
        queryForNutritionData: QueryForNutritionData,
        queryResponse: (Result<ListResponse<out DetailsDocument>>) -> Unit
    ) {
        return try {
            val (collectionName,
                databaseName,
                query) = queryForNutritionData
            cosmosStorageB.queryDietDetail(collectionName, databaseName, query) {
                queryResponse(Result.Success(it))
            }
        } catch (e: Exception) {
            queryResponse(Result.Error(e))
        }
    }
}
