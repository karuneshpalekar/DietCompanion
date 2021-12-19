package com.karunesh.azureauth.business.data.implementation

import com.karunesh.azureauth.business.data.abstraction.BlobStorageB
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.datasource.abstraction.BlobStorage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlobStorageBImpl
@Inject
constructor(
    private val blobStorage: BlobStorage
) : BlobStorageB {
    override suspend fun uploadImage(
        id: String,
        fileName: String,
        inputStream: InputStream,
        imageLength: Int
    ) {
        blobStorage.uploadImage(id, fileName, inputStream, imageLength)
    }

    override suspend fun listImages(id: String): Result<Array<String>> {
        return blobStorage.listImages(id)
    }

    override suspend fun getImage(
        id: String,
        name: String,
        imageStream: ByteArrayOutputStream,
        imageLength: Int
    ): Result<Boolean> {
        return blobStorage.getImage(id, name, imageStream, imageLength)
    }

}