package com.karunesh.azureauth.presentation.mediastore

import android.graphics.Bitmap

interface MediaStoreDelegate {

    suspend fun getData(): List<ImageContent>?

    suspend fun saveImage(bitmap: Bitmap, fileName: String)
}