package com.karunesh.azureauth.presentation.nutrition.adapter

import androidx.recyclerview.widget.DiffUtil
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument


class AllNutritionDiffUtil(
    private val oldList: List<NutritionDataDocument>,
    private val newList: List<NutritionDataDocument>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when (oldList[oldItemPosition].id) {
            newList[newItemPosition].id -> {
                true
            }
            else -> false
        }

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