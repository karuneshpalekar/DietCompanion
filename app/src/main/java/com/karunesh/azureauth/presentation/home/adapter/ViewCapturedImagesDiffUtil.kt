package com.karunesh.azureauth.presentation.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.karunesh.azureauth.presentation.mediastore.ImageContent


class ViewCapturedImagesDiffUtil(
    private val oldList: List<ImageContent>,
    private val newList: List<ImageContent>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            else -> true
        }
    }
}