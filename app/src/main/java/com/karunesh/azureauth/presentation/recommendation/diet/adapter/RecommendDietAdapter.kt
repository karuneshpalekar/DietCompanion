package com.karunesh.azureauth.presentation.recommendation.diet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karunesh.azureauth.R
import com.karunesh.azureauth.presentation.recommendation.diet.DietItem


class RecommendDietAdapter : RecyclerView.Adapter<RecommendDietAdapter.RecommendDietViewHolder>() {


    private var oldDietList = emptyList<DietItem>()
    var listener: RecommendListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendDietViewHolder {
        return RecommendDietViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recommend_diet_recycler_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecommendDietViewHolder, position: Int) {
        val productName = oldDietList[position]
        holder.bind(productName)
    }

    override fun getItemCount(): Int = oldDietList.size

    inner class RecommendDietViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dietName =
            itemView.findViewById<TextView>(R.id.recommendation_diet_name)
        private val cardView =
            itemView.findViewById<CardView>(R.id.recommendation_diet_card_view)
        private val dietType =
            itemView.findViewById<TextView>(R.id.recommendation_diet_type)

        fun bind(product: DietItem) {

            dietName.text = product.title
            dietType.text = product.dietType

            cardView.setOnClickListener {
                listener?.onRecyclerViewItemClicked(product)
            }
        }
    }


    fun getData(list: List<DietItem>) {
        val diffUtil = DietAdapterDiffUtil(oldDietList, list)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldDietList = list
        diffResults.dispatchUpdatesTo(this)

    }

}