package com.karunesh.azureauth.datasource.implementation

import android.util.Log
import com.karunesh.azureauth.BuildConfig
import com.karunesh.azureauth.business.domain.util.Result
import com.karunesh.azureauth.datasource.abstraction.BlobStorage
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlobStorageImpl
@Inject
constructor() : BlobStorage {
    override suspend fun uploadImage(
        id: String,
        imageName: String,
        inputStream: InputStream,
        imageLength: Int
    ) {
        withContext(Dispatchers.IO) {
            try {

                val container = getContainer(id)
                container?.createIfNotExists()
                val imageBlob = container?.getBlockBlobReference(imageName)
                imageBlob?.upload(inputStream, imageLength.toLong())
            } catch (e: Exception) {
            }
        }
    }


    override suspend fun listImages(id: String): Result<Array<String>> {
        try {
            val container = getContainer(id)

            val blobs = container?.listBlobs()

            val blobNames = LinkedList<String>()
            if (blobs != null) {
                for (blob in blobs) {
                    blobNames.add((blob as CloudBlockBlob).name)
                }
            }

            return Result.Success(blobNames.toTypedArray())
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun getImage(
        id: String,
        name: String,
        imageStream: ByteArrayOutputStream,
        imageLength: Int
    ): Result<Boolean> {
        return try {

            val container = getContainer(id)

            val blob = container?.getBlockBlobReference(name)

            if (blob?.exists() == true) {
                blob.downloadAttributes()
                blob.download(imageStream)
            }
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    @Throws(Exception::class)
    private fun getContainer(id: String): CloudBlobContainer? {
        val accountName = "karunesh"
        val accountKey = BuildConfig.BLOB_KEY
        val credentials =
            StorageCredentialsAccountAndKey(accountName, accountKey)
        val account = CloudStorageAccount(credentials, true)
        val blobClient = account.createCloudBlobClient()
        return blobClient.getContainerReference(id)
    }

}