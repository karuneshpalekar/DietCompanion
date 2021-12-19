package com.karunesh.azureauth.datasource.implementation

import com.azure.data.AzureData
import com.azure.data.model.Database
import com.azure.data.model.DocumentCollection
import com.azure.data.model.Query
import com.azure.data.model.service.ListResponse
import com.azure.data.model.service.Response
import com.karunesh.azureauth.datasource.abstraction.CosmosStorage
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument
import com.karunesh.azureauth.presentation.recommendation.diet.DetailsDocument
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CosmosStorageImpl
@Inject
constructor() : CosmosStorage {
    override suspend fun createDatabase(databaseName: String): Response<Database>? {
        var databaseCreationResponse: Response<Database>? = null
        AzureData.createDatabase(databaseId = databaseName) { response: Response<Database> ->
            databaseCreationResponse = response
        }
        return databaseCreationResponse
    }

    override suspend fun getDatabase(): ListResponse<Database>? {
        var existingDatabaseResponse: ListResponse<Database>? = null
        AzureData.getDatabases { listResponse ->
            existingDatabaseResponse = listResponse
        }
        return existingDatabaseResponse
    }

    override suspend fun createCollection(
        collectionName: String,
        databaseName: String,
        partitionKey: String,
        response: (Response<DocumentCollection>) -> Unit
    ) {
        try {
            AzureData.createCollection(
                collectionId = collectionName,
                databaseId = databaseName,
                partitionKey = partitionKey
            ) { queryResponse ->
                response(queryResponse)
            }
        } catch (e: Exception) {

        }
    }

    override suspend fun createDocument(
        document: NutritionDataDocument,
        collectionName: String,
        databaseName: String,
        partitionKey: String,
        response: (Response<NutritionDataDocument>) -> Unit
    ) {
        try {
            AzureData.createDocument(
                document = document,
                collectionId = collectionName,
                databaseId = databaseName,
                partitionKey = partitionKey
            ) { queryResponse ->
                response(queryResponse)
            }
        } catch (e: Exception) {

        }
    }

    override suspend fun queryNutritionData(
        collectionName: String,
        databaseName: String,
        query: Query,
        queryResponse: (ListResponse<out NutritionDataDocument>) -> Unit
    ) {
        AzureData.queryDocuments(
            collectionId = collectionName,
            databaseId = databaseName,
            query = query,
            documentClass = NutritionDataDocument::class.java
        ) { response ->
            queryResponse(response)
        }
    }

    override suspend fun queryAllNutritionData(
        collectionName: String,
        databaseName: String,
        queryResponse: (ListResponse<out NutritionDataDocument>) -> Unit
    ) {
        AzureData.getDocuments(
            collectionId = collectionName,
            databaseId = databaseName,
            maxPerPage = 30,
            documentClass = NutritionDataDocument::class.java
        ) { listResponse ->
            queryResponse(listResponse)
        }
    }

    override suspend fun queryDietDetail(
        collectionName: String,
        databaseName: String,
        query: Query,
        queryResponse: (ListResponse<out DetailsDocument>) -> Unit
    ) {
        AzureData.queryDocuments(
            collectionId = "collection",
            databaseId = "diets",
            query = query,
            documentClass = DetailsDocument::class.java
        ) { response ->
            queryResponse(response)
        }
    }

}