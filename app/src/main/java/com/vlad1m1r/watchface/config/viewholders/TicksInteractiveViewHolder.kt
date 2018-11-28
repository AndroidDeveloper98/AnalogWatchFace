package com.vlad1m1r.watchface.config.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.vlad1m1r.watchface.R
import com.vlad1m1r.watchface.utils.DataProvider

class TicksInteractiveViewHolder(itemView: View, private val dataProvider: DataProvider): RecyclerView.ViewHolder(itemView) {
    private val switch = itemView.findViewById<Switch>(R.id.settings_switch).apply {
        isChecked = dataProvider.hasTicksInInteractiveMode()
        setText(R.string.ticks_in_interactive_mode)
        setOnCheckedChangeListener { _, isChecked ->
            dataProvider.setHasTicksInInteractiveMode(isChecked)
        }
    }
}