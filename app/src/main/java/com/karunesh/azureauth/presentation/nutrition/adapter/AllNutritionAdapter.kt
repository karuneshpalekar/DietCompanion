package com.karunesh.azureauth.presentation.nutrition.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.karunesh.azureauth.R
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument


class AllNutritionAdapter : RecyclerView.Adapter<AllNutritionAdapter.AllNutritionViewHolder>() {

    private var oldDietList = emptyList<NutritionDataDocument>()
    var listener: AllNutritionListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllNutritionViewHolder {
        return AllNutritionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.all_nutrition_recycler_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AllNutritionViewHolder, position: Int) {
        val productName = oldDietList[position]
        holder.bind(productName)
    }

    override fun getItemCount(): Int = oldDietList.size

    inner class AllNutritionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nutritionName =
            itemView.findViewById<TextView>(R.id.nutrition_name)
        private val nutritionDescription =
            itemView.findViewById<TextView>(R.id.nutrition_description)
        private val checkBox =
            itemView.findViewById<AppCompatCheckBox>(R.id.nutrition_check_box)


        fun bind(product: NutritionDataDocument) {

            nutritionName.text = product.name
            nutritionDescription.text = product.description
            checkBox.isChecked = product.selected == true


            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    listener?.onRecyclerViewItemClicked(product)
                } else {
                    listener?.onRecyclerViewItemClickRemove(product)
                }
            }

        }
    }


    fun getData(list: MutableList<NutritionDataDocument>) {
        val diffUtil = AllNutritionDiffUtil(oldDietList, list)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldDietList = list
        diffResults.dispatchUpdatesTo(this)

    }

}