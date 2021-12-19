package com.karunesh.azureauth.business.interactors.cosmos

import com.azure.data.model.DocumentCollection
import com.azure.data.model.service.Response
import com.karunesh.azureauth.business.data.abstraction.CosmosStorageB
import com.karunesh.azureauth.business.domain.util.Result

class CreateCollection(
    private val cosmosStorageB: CosmosStorageB
) {
    suspend fun outputUploadResults(
        createCollectionDataClass: CreateCollectionDataClass,
        queryResponse: (Result<Response<DocumentCollection>>) -> Unit
    ) {
        return try {
            val (collectionName,
                databaseName,
                partitionKey) = createCollectionDataClass
            cosmosStorageB.createCollection(collectionName, databaseName, partitionKey) {
                queryResponse(Result.Success(it))
            }
        } catch (e: Exception) {
            queryResponse(Result.Error(e))
        }
    }
}

data class CreateCollectionDataClass(
    var collectionName: String,
    var databaseName: String,
    var partitionKey: String
)