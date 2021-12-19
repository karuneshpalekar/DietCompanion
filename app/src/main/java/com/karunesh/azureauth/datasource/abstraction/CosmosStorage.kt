package com.karunesh.azureauth.datasource.abstraction

import com.azure.data.model.Database
import com.azure.data.model.DocumentCollection
import com.azure.data.model.Query
import com.azure.data.model.service.ListResponse
import com.azure.data.model.service.Response
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument
import com.karunesh.azureauth.presentation.recommendation.diet.DetailsDocument

interface CosmosStorage {

    suspend fun createDatabase(
        databaseName: String
    ): Response<Database>?

    suspend fun getDatabase(): ListResponse<Database>?

    suspend fun createCollection(
        collectionName: String,
        databaseName: String,
        partitionKey: String,
        response: (Response<DocumentCollection>) -> Unit
    )

    suspend fun createDocument(
        document: NutritionDataDocument,
        collectionName: String,
        databaseName: String,
        partitionKey: String,
        response: (Response<NutritionDataDocument>) -> Unit
    )

    suspend fun queryNutritionData(
        collectionName: String,
        databaseName: String,
        query: Query,
        queryResponse: (ListResponse<out NutritionDataDocument>) -> Unit
    )

    suspend fun queryAllNutritionData(
        collectionName: String,
        databaseName: String,
        queryResponse: (ListResponse<out NutritionDataDocument>) -> Unit
    )

    suspend fun queryDietDetail(
        collectionName: String,
        databaseName: String,
        query: Query,
        queryResponse: (ListResponse<out DetailsDocument>) -> Unit
    )
}