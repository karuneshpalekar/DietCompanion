package com.karunesh.azureauth.business.interactors.blob

import android.util.Log
import com.karunesh.azureauth.business.data.abstraction.BlobStorageB
import com.karunesh.azureauth.business.domain.util.Result
import java.io.InputStream

class UploadImage(
    private val blobStorageB: BlobStorageB
) {
    suspend fun outputUploadResults(uploadImageDataClass: UploadImageDataClass): Result<Boolean> {
        return try {
            Log.d("Pipeline", "The outputUploadResult fun is called")
            val (id, fileName, inputStream, imageLength) = uploadImageDataClass
            blobStorageB.uploadImage(id, fileName, inputStream, imageLength)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

data class UploadImageDataClass(
    var id: String,
    var fileName: String,
    var inputStream: InputStream,
    var imageLength: Int
)