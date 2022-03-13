package com.vlad1m1r.watchface.settings.hands

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.vlad1m1r.watchface.R
import com.vlad1m1r.watchface.data.DataStorage
import com.vlad1m1r.watchface.settings.Navigator
import com.vlad1m1r.watchface.settings.base.viewholders.SettingsViewHolder
import com.vlad1m1r.watchface.settings.base.viewholders.SettingsWithSwitchViewHolder
import com.vlad1m1r.watchface.settings.base.viewholders.TitleViewHolder
import com.vlad1m1r.watchface.settings.hands.hand.HandType
import java.lang.IllegalArgumentException

private const val TYPE_TITLE = 0
private const val TYPE_HOUR_HAND = 1
private const val TYPE_MINUTE_HAND = 2
private const val TYPE_SECOND_HAND = 3
private const val TYPE_CENTRAL_CIRCLE = 4
private const val TYPE_KEEP_COLOR_IN_AMBIENT_MODE = 5

class HandsAdapter(
    private val navigator: Navigator,
    private val dataStorage: DataStorage,
    @StringRes private val title: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> TitleViewHolder(parent)
            TYPE_HOUR_HAND,
            TYPE_MINUTE_HAND,
            TYPE_SECOND_HAND,
            TYPE_CENTRAL_CIRCLE -> SettingsViewHolder(parent)
            TYPE_KEEP_COLOR_IN_AMBIENT_MODE -> SettingsWithSwitchViewHolder(parent)
            else -> {
                throw IllegalArgumentException("viewType: $viewType is not supported")
            }
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun getItemViewType(position: Int) =
        when (position) {
            0 -> TYPE_TITLE
            1 -> TYPE_HOUR_HAND
            2 -> TYPE_MINUTE_HAND
            3 -> TYPE_SECOND_HAND
            4 -> TYPE_CENTRAL_CIRCLE
            5 -> TYPE_KEEP_COLOR_IN_AMBIENT_MODE
            else -> throw IllegalArgumentException("Unsupported View Type position: $position")
        }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            TYPE_TITLE ->
                (viewHolder as TitleViewHolder).bind(
                    title
                )
            TYPE_HOUR_HAND -> {
                (viewHolder as SettingsViewHolder).bind(
                    R.string.wear_hours_hand
                ) {
                    val activity = viewHolder.itemView.context as Activity
                    navigator.goToHandActivity(activity, R.string.wear_hours_hand, HandType.HOURS)
                }
            }
            TYPE_MINUTE_HAND -> {
                (viewHolder as SettingsViewHolder).bind(
                    R.string.wear_minutes_hand
                ) {
                    val activity = viewHolder.itemView.context as Activity
                    navigator.goToHandActivity(activity, R.string.wear_minutes_hand, HandType.MINUTES)
                }
            }
            TYPE_SECOND_HAND -> {
                (viewHolder as SettingsViewHolder).bind(
                    R.string.wear_seconds_hand
                ) {
                    val activity = viewHolder.itemView.context as Activity
                    navigator.goToHandActivity(activity, R.string.wear_seconds_hand, HandType.SECONDS)
                }
            }
            TYPE_CENTRAL_CIRCLE -> {
                (viewHolder as SettingsViewHolder).bind(
                    R.string.wear_central_circle
                ) {
                    val activity = viewHolder.itemView.context as Activity
                    navigator.goToCentralCircleActivity(activity, R.string.wear_central_circle)
                }
            }
            TYPE_KEEP_COLOR_IN_AMBIENT_MODE -> {
                (viewHolder as SettingsWithSwitchViewHolder).bind(
                    R.string.wear_keep_hands_color_in_ambient_mode,
                    dataStorage.shouldKeepHandColorInAmbientMode()
                ) {
                    dataStorage.setShouldKeepHandColorInAmbientMode(it)
                }
            }
        }
    }
}