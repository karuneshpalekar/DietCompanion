package com.karunesh.azureauth.presentation.recommendation.diet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.karunesh.azureauth.R
import com.karunesh.azureauth.presentation.recommendation.diet.DietItem


class AllDietAdapter : RecyclerView.Adapter<AllDietAdapter.AllDietViewHolder>() {


    private var oldDietList = emptyList<DietItem>()
    var listener: AllDietListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDietViewHolder {
        return AllDietViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.all_diet_recycler_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AllDietViewHolder, position: Int) {
        val productName = oldDietList[position]
        holder.bind(productName)
    }

    override fun getItemCount(): Int = oldDietList.size

    inner class AllDietViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dietName =
            itemView.findViewById<TextView>(R.id.diet_name)
        private val cardView =
            itemView.findViewById<MaterialCardView>(R.id.all_diet_card_view)
        private val dietType =
            itemView.findViewById<TextView>(R.id.diet_type)
        private val checkBox = itemView.findViewById<AppCompatCheckBox>(R.id.all_diet_check_box)

        fun bind(product: DietItem) {

            dietName.text = product.title
            dietType.text = product.dietType
            cardView.setOnClickListener {
                listener?.onRecyclerViewItemCardViewClicked(product)
            }

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


    fun getData(list: MutableList<DietItem>) {
        val diffUtil = DietAdapterDiffUtil(oldDietList, list)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldDietList = list
        diffResults.dispatchUpdatesTo(this)

    }

}