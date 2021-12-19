package com.karunesh.azureauth.presentation.mediastore

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.karunesh.azureauth.R
import com.karunesh.azureauth.utils.sdk29AndUp
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

const val IMAGE_EXTENSIONS = ".jpg"

class MediaStoreDelegateImpl @Inject
constructor(
    @ApplicationContext private val context: Context
) : MediaStoreDelegate {
    override suspend fun saveImage(bitmap: Bitmap, fileName: String) {
        try {
            val collection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val dirDest = File(
                Environment.DIRECTORY_PICTURES,
                context.getString(R.string.app_name)
            )
            val date = System.currentTimeMillis()

            val path = sdk29AndUp {
                MediaStore.MediaColumns.RELATIVE_PATH
            } ?: MediaStore.MediaColumns.DATA

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/$IMAGE_EXTENSIONS")
                put(MediaStore.MediaColumns.DATE_ADDED, date)
                put(MediaStore.MediaColumns.DATE_MODIFIED, date)
                put(MediaStore.MediaColumns.SIZE, bitmap.byteCount)
                put(MediaStore.MediaColumns.WIDTH, bitmap.width)
                put(MediaStore.MediaColumns.HEIGHT, bitmap.height)
                put(path, "$dirDest")
                sdk29AndUp {
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }


            val imageUri = context.contentResolver.insert(collection, contentValues)


            withContext(Dispatchers.IO) {

                imageUri?.let { uri ->
                    context.contentResolver.openOutputStream(uri, "w").use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }
                    contentValues.clear()
                    sdk29AndUp {
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    }
                    context.contentResolver.update(uri, contentValues, null, null)
                }
            }


        } catch (e: FileNotFoundException) {
            null
        }
    }

    override suspend fun getData(): List<ImageContent>? = withContext(Dispatchers.IO) {
        try {
            val collection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val selection = sdk29AndUp {
                MediaStore.Images.ImageColumns.RELATIVE_PATH + " like ? "
            } ?: MediaStore.Images.Media.DATA + " like ? "

            val arg = sdk29AndUp {
                MediaStore.Images.ImageColumns.RELATIVE_PATH
            } ?: MediaStore.Images.Media.DATA
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                arg
            )

            val selectionArgs = arrayOf("%Diet Companion%")

            val sortOrder = MediaStore.MediaColumns.DATE_ADDED + " COLLATE NOCASE DESC"

            val itemList: MutableList<ImageContent> = mutableListOf()


            context.contentResolver?.query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val relativePathColumn = sdk29AndUp {
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
                } ?: cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val relativePath = cursor.getString(relativePathColumn)


                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    itemList.add(
                        ImageContent(
                            contentUri = contentUri,
                            id = id,
                            name = displayName,
                            relativePath = relativePath
                        )
                    )


                }
                cursor.close()
            }

            itemList
        } catch (e: Exception) {

            null
        }
    }
}