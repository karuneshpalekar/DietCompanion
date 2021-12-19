package com.karunesh.azureauth.di

import android.content.Context
import com.karunesh.azureauth.business.data.abstraction.BlobStorageB
import com.karunesh.azureauth.business.data.abstraction.CosmosStorageB
import com.karunesh.azureauth.business.data.implementation.BlobStorageBImpl
import com.karunesh.azureauth.business.data.implementation.CosmosStorageBImpl
import com.karunesh.azureauth.business.interactors.BlobInteractors
import com.karunesh.azureauth.business.interactors.CosmosInteractors
import com.karunesh.azureauth.business.interactors.blob.GetImage
import com.karunesh.azureauth.business.interactors.blob.ListImages
import com.karunesh.azureauth.business.interactors.blob.UploadImage
import com.karunesh.azureauth.business.interactors.cosmos.*
import com.karunesh.azureauth.datasource.abstraction.BlobStorage
import com.karunesh.azureauth.datasource.abstraction.CosmosStorage
import com.karunesh.azureauth.datasource.implementation.BlobStorageImpl
import com.karunesh.azureauth.datasource.implementation.CosmosStorageImpl
import com.karunesh.azureauth.presentation.mediastore.MediaStoreDelegate
import com.karunesh.azureauth.presentation.mediastore.MediaStoreDelegateImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.math.cos

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideMediaStoreImpl(
        @ApplicationContext context: Context
    ): MediaStoreDelegate {
        return MediaStoreDelegateImpl(context)
    }

    @Singleton
    @Provides
    fun provideBlobStorageImpl(
    ): BlobStorage {
        return BlobStorageImpl()
    }

    @Singleton
    @Provides
    fun provideCosmosStorageImpl(
    ): CosmosStorage {
        return CosmosStorageImpl()
    }

    @Singleton
    @Provides
    fun provideBlobStorageBImpl(
        blobStorage: BlobStorage
    ): BlobStorageB {
        return BlobStorageBImpl(blobStorage)
    }

    @Singleton
    @Provides
    fun provideCosmosStorageBImpl(
        cosmosStorage: CosmosStorage
    ): CosmosStorageB {
        return CosmosStorageBImpl(cosmosStorage)
    }

    @Singleton
    @Provides
    fun provideBlobInteraction(
        blobStorageB: BlobStorageB
    ): BlobInteractors {
        return BlobInteractors(
            UploadImage(blobStorageB),
            ListImages(blobStorageB),
            GetImage(blobStorageB)
        )
    }

    @Singleton
    @Provides
    fun provideCosmosInteraction(
        cosmosStorageB: CosmosStorageB
    ): CosmosInteractors {
        return CosmosInteractors(
            QueryDocument(cosmosStorageB),
            AllNutritionDocuments(cosmosStorageB),
            CreateCollection(cosmosStorageB),
            CreateDocument(cosmosStorageB),
            QueryDietDetailDocument(cosmosStorageB)
        )
    }

}