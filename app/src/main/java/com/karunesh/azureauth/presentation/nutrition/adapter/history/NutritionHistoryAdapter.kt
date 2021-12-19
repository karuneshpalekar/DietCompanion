package com.karunesh.azureauth.presentation.nutrition.adapter.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karunesh.azureauth.R
import com.karunesh.azureauth.presentation.nutrition.NutritionDataDocument


class NutritionHistoryAdapter :
    RecyclerView.Adapter<NutritionHistoryAdapter.NutritionHistoryViewHolder>() {

    private var oldDietList = emptyList<NutritionDataDocument>()
    var listener: NutritionHistoryListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionHistoryViewHolder {
        return NutritionHistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.nutrition_history_recycler_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NutritionHistoryViewHolder, position: Int) {
        val productName = oldDietList[position]
        holder.bind(productName)
    }

    override fun getItemCount(): Int = oldDietList.size

    inner class NutritionHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nutritionName =
            itemView.findViewById<TextView>(R.id.nutrition_history_name)
        private val cardView =
            itemView.findViewById<CardView>(R.id.nutrition_history_card_view)
        private val nutritionDescription =
            itemView.findViewById<TextView>(R.id.nutrition_history_description)
        private val nutritionDate =
            itemView.findViewById<TextView>(R.id.nutrition_history_date)


        fun bind(product: NutritionDataDocument) {

            nutritionName.text = product.name
            nutritionDescription.text = product.description
            nutritionDate.text = product.date.toString()

            cardView.setOnClickListener {
                listener?.onRecyclerViewItemClicked(product)
            }

        }
    }


    fun getData(list: List<NutritionDataDocument>) {
        val diffUtil = NutritionHistoryDiffUtil(oldDietList, list)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldDietList = list
        diffResults.dispatchUpdatesTo(this)

    }

}