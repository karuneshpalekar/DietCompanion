package com.karunesh.azureauth.business.interactors

import com.karunesh.azureauth.business.interactors.cosmos.*

data class CosmosInteractors(
    val queryDocument: QueryDocument,
    val allNutritionDocuments: AllNutritionDocuments,
    val createCollection: CreateCollection,
    val createDocument: CreateDocument,
    val queryDietDetailDocument: QueryDietDetailDocument
)