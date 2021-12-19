package com.karunesh.azureauth.business.interactors.blob

import com.karunesh.azureauth.business.data.abstraction.BlobStorageB
import com.karunesh.azureauth.business.domain.util.Result

class ListImages(
    private val blobStorageB: BlobStorageB
) {
    suspend fun outputUploadResults(id: String): Result<Array<String>> {
        return try {
            blobStorageB.listImages(id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
