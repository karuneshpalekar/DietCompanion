package com.karunesh.azureauth.business.data.implementation

import com.azure.data.model.Database
import com.azure.data.model.DocumentCollection
import com.azure.data.model.Query
import com.azure.data.model.service.ListResponse
import com.azure.data.model.service.Response
import com.karunesh.azureauth.business.data.abstraction.CosmosStorageB
import com.karunesh.azureauth.datasource.abstraction.CosmosStorage
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument
import com.karunesh.azureauth.presentation.recommendation.diet.DetailsDocument
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CosmosStorageBImpl
@Inject
constructor(
    private val cosmosStorage: CosmosStorage
) : CosmosStorageB {
    override suspend fun createDatabase(databaseName: String): Response<Database>? {
        return cosmosStorage.createDatabase(databaseName)
    }

    override suspend fun getDatabase(): ListResponse<Database>? {
        return cosmosStorage.getDatabase()
    }

    override suspend fun createCollection(
        collectionName: String,
        databaseName: String,
        partitionKey: String,
        response: (Response<DocumentCollection>) -> Unit
    ) {
        cosmosStorage.createCollection(
            collectionName,
            databaseName,
            partitionKey
        ) { queryResponse ->
            response(queryResponse)
        }
    }

    override suspend fun createDocument(
        document: NutritionDataDocument,
        collectionName: String,
        databaseName: String,
        partitionKey: String,
        response: (Response<NutritionDataDocument>) -> Unit
    ) {
        cosmosStorage.createDocument(document, collectionName, databaseName, partitionKey) {
            response(it)
        }
    }

    override suspend fun queryNutritionData(
        collectionName: String,
        databaseName: String,
        query: Query,
        queryResponse: (ListResponse<out NutritionDataDocument>) -> Unit
    ) {
        cosmosStorage.queryNutritionData(collectionName, databaseName, query) {
            queryResponse(it)
        }
    }

    override suspend fun queryAllNutritionData(
        collectionName: String,
        databaseName: String,
        queryResponse: (ListResponse<out NutritionDataDocument>) -> Unit
    ) {
        cosmosStorage.queryAllNutritionData(collectionName, databaseName) {
            queryResponse(it)
        }
    }

    override suspend fun queryDietDetail(
        collectionName: String,
        databaseName: String,
        query: Query,
        queryResponse: (ListResponse<out DetailsDocument>) -> Unit
    ) {
        cosmosStorage.queryDietDetail(collectionName, databaseName, query) {
            queryResponse(it)
        }
    }

}