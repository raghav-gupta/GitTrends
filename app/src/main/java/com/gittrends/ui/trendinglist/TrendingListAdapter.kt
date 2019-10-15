package com.gittrends.ui.trendinglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.entity.TrendingItem
import com.gittrends.R
import com.squareup.picasso.Picasso
import javax.inject.Inject
import kotlin.properties.Delegates

class TrendingListAdapter @Inject constructor(
    val inflater: LayoutInflater, val picasso: Picasso
) : RecyclerView.Adapter<TrendingItemViewHolder>() {

    private var lastSetPos = -1
    var items: List<TrendingItem> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingItemViewHolder {
        return TrendingItemViewHolder(
            DataBindingUtil.inflate(
                inflater,
                R.layout.trending_row_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TrendingItemViewHolder, position: Int) {
        holder.bind(items.get(position), picasso)
        holder.itemBinder.root.setOnClickListener {

            val adapterPosition = holder.adapterPosition

            //Collapse Previous opened Item
            if (adapterPosition != lastSetPos && lastSetPos >= 0) {
                updateItemCollapseStatus(lastSetPos, true)
                notifyItemChanged(lastSetPos)
            }

            // Toggle Current Item
            val isCollapse = !items.get(adapterPosition).collapse
            updateItemCollapseStatus(adapterPosition, isCollapse)
            notifyItemChanged(adapterPosition)

            //Save opened index
            lastSetPos = adapterPosition
        }
    }

    private fun updateItemCollapseStatus(pos: Int, status: Boolean) {
        items.get(pos).collapse = status

    }
}