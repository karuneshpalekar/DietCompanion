package com.karunesh.azureauth.presentation.recommendation.diet.adapter

import androidx.recyclerview.widget.DiffUtil
import com.karunesh.azureauth.presentation.recommendation.diet.DietItem


class DietAdapterDiffUtil(
    private val oldList: List<DietItem>,
    private val newList: List<DietItem>
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