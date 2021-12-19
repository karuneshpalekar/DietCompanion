package com.karunesh.azureauth.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karunesh.azureauth.R
import com.karunesh.azureauth.presentation.mediastore.ImageContent


class ViewCapturedImagesAdapter : RecyclerView.Adapter<ViewCapturedImagesAdapter.HomeViewHolder>() {

    private var oldDietList = emptyList<ImageContent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.captured_image_recycler_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val productName = oldDietList[position]
        holder.bind(productName)
    }

    override fun getItemCount(): Int = oldDietList.size

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemImage =
            itemView.findViewById<ImageView>(R.id.camera_item_image)

        fun bind(product: ImageContent) {
            Glide.with(itemImage)
                .load(product.contentUri)
                .into(itemImage)
        }
    }


    fun getData(list: List<ImageContent>) {
        val diffUtil = ViewCapturedImagesDiffUtil(oldDietList, list)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldDietList = list
        diffResults.dispatchUpdatesTo(this)

    }

}