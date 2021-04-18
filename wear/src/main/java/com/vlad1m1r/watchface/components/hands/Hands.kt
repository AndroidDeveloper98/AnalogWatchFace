package com.vlad1m1r.watchface.components.hands

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import com.vlad1m1r.watchface.data.ColorStorage
import com.vlad1m1r.watchface.model.Mode
import com.vlad1m1r.watchface.model.Point
import com.vlad1m1r.watchface.utils.WatchView
import com.vlad1m1r.watchface.utils.hoursRotation
import com.vlad1m1r.watchface.utils.minutesRotation
import com.vlad1m1r.watchface.utils.secondsRotation
import java.util.*

class Hands(
    context: Context,
    private val colorStorage: ColorStorage
) : WatchView {

    private val paintDataProvider = PaintDataProvider(context, colorStorage)

    private var showSecondsHand = true
    private var showMinutesHand = true
    private var showHoursHand = true

    private var center = Point()

    private lateinit var drawHourHand: DrawHand
    private lateinit var drawMinuteHand: DrawHand
    private lateinit var drawSecondsHand: DrawHand
    private lateinit var drawCircle: DrawCircle

    init {
        initializeHands()
    }

    private fun initializeHands() {
        drawHourHand = DrawHand(
            paintDataProvider.getHourHandData()
        )

        drawMinuteHand = DrawHand(
            paintDataProvider.getMinuteHandData()
        )

        drawSecondsHand = DrawHand(
            paintDataProvider.getSecondHandData()
        )

        drawCircle = DrawCircle(
            paintDataProvider.getCircleData()
        )
    }

    override fun setCenter(center: Point) {
        this.center = center
    }

    fun draw(canvas: Canvas, calendar: Calendar) {

        val hoursRotation = calendar.hoursRotation()
        val minutesRotation = calendar.minutesRotation()

        if(showMinutesHand) {
            drawMinuteHand(canvas, minutesRotation, center, center.x)
        }

        if(showHoursHand) {
            drawHourHand(canvas, hoursRotation, center, center.x)
        }

        if (showSecondsHand) {
            val secondsRotation = calendar.secondsRotation()
            drawSecondsHand(canvas, secondsRotation, center, center.x)
        }

        if(showSecondsHand || showHoursHand || showMinutesHand) {
            drawCircle(canvas, center)
        }
    }

    fun setMode(mode: Mode) {
        val secondColor = colorStorage.getSecondsHandColor()
        val minuteColor = colorStorage.getMinutesHandColor()
        val hourColor = colorStorage.getHoursHandColor()

        showSecondsHand = !mode.isAmbient && secondColor != Color.TRANSPARENT
        showMinutesHand = minuteColor != Color.TRANSPARENT
        showHoursHand = hourColor != Color.TRANSPARENT

        drawHourHand.setInAmbientMode(mode.isAmbient)
        drawMinuteHand.setInAmbientMode(mode.isAmbient)
        drawSecondsHand.setInAmbientMode(mode.isAmbient)

        drawCircle.setInAmbientMode(mode.isAmbient)
        drawCircle.setInBurnInProtectionMode(mode.isBurnInProtection && mode.isAmbient)
    }

    fun invalidate() {
        initializeHands()
    }
}