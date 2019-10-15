package com.gittrends.ui.trendinglist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.entity.TrendingItem
import com.gittrends.ui.widgets.CircleTransform
import com.squareup.picasso.Picasso

class TrendingItemViewHolder(val itemBinder: com.gittrends.databinding.TrendingRowItemBinding) :
    RecyclerView.ViewHolder(itemBinder.root) {

    fun bind(model: TrendingItem, picasso: Picasso) {
        itemBinder.model = model
        if (model.language.isNullOrEmpty()) {
            itemBinder.ivLangIcon.visibility = View.GONE
            itemBinder.tvLanguage.visibility = View.GONE
        }

        picasso.load(model.authorUrl)
            .transform(CircleTransform())
            .into(itemBinder.ivProfileImage)

        itemBinder.clDetailLayout.visibility = if (model.collapse) View.GONE else View.VISIBLE
        itemBinder.vShadow.visibility = if (model.collapse) View.GONE else View.VISIBLE
        itemBinder.vDivider.visibility = if (model.collapse) View.VISIBLE else View.GONE
    }
}

