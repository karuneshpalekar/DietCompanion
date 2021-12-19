package com.karunesh.azureauth.business.interactors.cosmos

import com.azure.data.model.service.Response
import com.karunesh.azureauth.business.data.abstraction.CosmosStorageB
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument


class CreateDocument(
    private val cosmosStorageB: CosmosStorageB
) {
    suspend fun outputUploadResults(
        createDocumentDataClass: CreateDocumentDataClass,
        queryResponse: (Result<Response<NutritionDataDocument>>) -> Unit
    ) {
        return try {
            val (document,
                collectionName,
                databaseName,
                partitionKey) = createDocumentDataClass
            cosmosStorageB.createDocument(document, collectionName, databaseName, partitionKey) {
                queryResponse(Result.Success(it))
            }
        } catch (e: Exception) {
            queryResponse(Result.Error(e))
        }
    }
}

data class CreateDocumentDataClass(
    var document: NutritionDataDocument,
    var collectionName: String,
    var databaseName: String,
    var partitionKey: String
)