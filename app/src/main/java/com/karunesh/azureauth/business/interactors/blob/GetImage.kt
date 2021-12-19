package com.karunesh.azureauth.business.interactors.blob

import com.karunesh.azureauth.business.data.abstraction.BlobStorageB
import java.io.ByteArrayOutputStream


class GetImage(
    private val blobStorageB: BlobStorageB
) {
    suspend fun outputUploadResults(
        id: String,
        name: String,
        imageStream: ByteArrayOutputStream,
        imageLength: Int
    ) {
        try {
            blobStorageB.getImage(id, name, imageStream, imageLength)
        } catch (e: Exception) {
        }
    }
}